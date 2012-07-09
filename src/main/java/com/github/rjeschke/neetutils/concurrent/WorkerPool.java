/*
 * Copyright (C) 2012 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rjeschke.neetutils.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import com.github.rjeschke.neetutils.SysUtils;

/**
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 * @param <T>
 */
public class WorkerPool<T> implements Runnable
{
    private final int numThreads;
    private final int queueLimit;
    private final boolean serialCallbacks;
    private final WorkerCallback<T> callback;
    private final ConcurrentLinkedQueue<ThreadWorker<T>> workers = new ConcurrentLinkedQueue<ThreadWorker<T>>();
    private final ConcurrentLinkedQueue<Job<T>> jobs = new ConcurrentLinkedQueue<Job<T>>();
    private final ConcurrentLinkedQueue<WorkerResult<T>> results = new ConcurrentLinkedQueue<WorkerResult<T>>();
    private final Semaphore resultSync = new Semaphore(1);
    private final Thread[] threads;
    private Thread callbackThread = null;

    private WorkerPool(WorkerCallback<T> callback, int threads, int queueLimit, boolean serialCallbacks)
    {
        this.callback = callback;
        this.numThreads = threads;
        this.serialCallbacks = serialCallbacks;
        this.queueLimit = Math.max(0, queueLimit);
        this.threads = new Thread[threads];
    }

    public static <T> WorkerPool<T> start(WorkerCallback<T> callback, int threads, int queueLimit,
            boolean serialCallbacks)
    {
        final WorkerPool<T> jobber = new WorkerPool<T>(callback, ThreadPool.defaultThreadcount(threads), queueLimit, serialCallbacks);

        for(int i = 0; i < jobber.threads.length; i++)
        {
            final ThreadWorker<T> w = new ThreadWorker<T>(jobber);
            final Thread t = new Thread(w);
            t.setDaemon(true);
            jobber.workers.offer(w);
            t.start();
            jobber.threads[i] = t;
        }

        if(serialCallbacks)
        {
            jobber.resultSync.acquireUninterruptibly();
            final Thread t = new Thread(jobber);
            t.setDaemon(true);
            t.start();
            jobber.callbackThread = t;
        }

        return jobber;
    }

    public int threadCount()
    {
        return this.numThreads;
    }

    public void enqueue(Worker<T> worker, T object)
    {
        if(worker == null)
            throw new NullPointerException("A null Worker is not permitted");

        final ThreadWorker<T> w = this.workers.poll();
        final Job<T> job = new Job<T>(worker, object);
        if(w != null)
        {
            w.setWorkLoad(job);
        }
        else
        {
            if(this.queueLimit != 0 && this.jobs.size() >= this.queueLimit)
            {
                final int ql = Math.max(this.queueLimit >> 1, 1);
                while(this.jobs.size() > ql)
                    SysUtils.fineSleep(5);
            }

            this.jobs.offer(job);
        }
    }

    private void reuseOrEnqueue(ThreadWorker<T> w)
    {
        final Job<T> job = this.jobs.poll();
        if(job != null)
            w.setWorkLoad(job);
        else
            this.workers.offer(w);
    }

    void doCallback(ThreadWorker<T> threadWorker, WorkerStatus status, Worker<T> worker, T object)
    {
        if(this.serialCallbacks)
        {
            this.results.offer(new WorkerResult<T>(worker, object, status));
            this.resultSync.release();
        }
        else
        {
            try
            {
                this.callback.workerCallback(this, status, worker, object);
            }
            catch (Throwable t)
            {
                //
            }
        }
        this.reuseOrEnqueue(threadWorker);
    }

    public boolean hasWork()
    {
        return !this.jobs.isEmpty();
    }

    public void join()
    {
        while(!this.jobs.isEmpty() || (this.serialCallbacks && !this.results.isEmpty()))
            SysUtils.sleep(10);
    }

    public void stop(boolean join)
    {
        if(join)
            this.join();

        final StopWorker<T> stop = new StopWorker<T>();
        for(int i = 0; i < this.numThreads; i++)
            this.enqueue(stop, null);

        for(int i = 0; i < this.numThreads; i++)
            SysUtils.threadJoin(this.threads[i]);

        if(this.callbackThread != null)
        {
            this.results.offer(new WorkerResult<T>(null, null, WorkerStatus.OK));
            this.resultSync.release();
            SysUtils.threadJoin(this.callbackThread);
        }
    }

    @Override
    public void run()
    {
        for(;;)
        {
            try
            {
                this.resultSync.acquireUninterruptibly();
                final WorkerResult<T> r = this.results.poll();
                if(r.worker == null)
                    break;
                this.callback.workerCallback(this, r.status, r.worker, r.object);
            }
            catch (Throwable t)
            {
                //
            }
        }
    }

    static class WorkerResult<T>
    {
        final Worker<T> worker;
        final WorkerStatus status;
        final T object;

        public WorkerResult(Worker<T> worker, T object, WorkerStatus status)
        {
            this.worker = worker;
            this.object = object;
            this.status = status;
        }
    }

    static class ThreadWorker<T> implements Runnable
    {
        private final Semaphore sync = new Semaphore(1);
        private final WorkerPool<T> pool;
        private volatile Job<T> workload = null;

        public ThreadWorker(WorkerPool<T> pool)
        {
            this.sync.acquireUninterruptibly();
            this.pool = pool;
        }

        protected void setWorkLoad(Job<T> job)
        {
            this.workload = job;
            this.sync.release();
        }

        @Override
        public void run()
        {
            for(;;)
            {
                boolean ok = true;
                Throwable ta = null;
                try
                {
                    this.sync.acquireUninterruptibly();
                    if(this.workload.worker instanceof StopWorker)
                        break;
                    this.workload.worker.run(this.workload.object);
                }
                catch (Throwable t)
                {
                    ta = t;
                    ok = false;
                }

                this.pool.doCallback(this, ok ? WorkerStatus.OK : new WorkerStatus(ta), this.workload.worker,
                        this.workload.object);
            }
        }
    }

    static class Job<T>
    {
        public final Worker<T> worker;
        public final T object;

        public Job(Worker<T> worker, T object)
        {
            this.worker = worker;
            this.object = object;
        }
    }

    static class StopWorker<T> implements Worker<T>
    {
        public StopWorker()
        {
            //
        }

        @Override
        public void run(T object)
        {
            // empty
        }
    }
}
