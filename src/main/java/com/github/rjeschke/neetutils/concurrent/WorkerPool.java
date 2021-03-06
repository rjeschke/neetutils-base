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
public class WorkerPool<T> implements Runnable, RequeueWatcherCallback<WorkerPool.Job<T>, WorkerPool.ThreadWorker<T>>
{
    private final int                                                     numThreads;
    private final int                                                     queueLimit;
    private final boolean                                                 serialCallbacks;
    private final WorkerCallback<T>                                       callback;
    private final ConcurrentLinkedQueue<ThreadWorker<T>>                  workers        = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Job<T>>                           jobs           = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<WorkerResult<T>>                  results        = new ConcurrentLinkedQueue<>();
    private final Semaphore                                               resultSync     = new Semaphore(1);
    private final Thread[]                                                threads;
    private Thread                                                        callbackThread = null;
    private RequeueWatcher<WorkerPool.Job<T>, WorkerPool.ThreadWorker<T>> watcher;

    private WorkerPool(final WorkerCallback<T> callback, final int threads, final int queueLimit, final boolean serialCallbacks)
    {
        this.callback = callback;
        this.numThreads = threads;
        this.serialCallbacks = serialCallbacks;
        this.queueLimit = Math.max(0, queueLimit);
        this.threads = new Thread[threads];
    }

    public static <T> WorkerPool<T> start(final WorkerCallback<T> callback, final int threads, final int queueLimit, final boolean serialCallbacks)
    {
        final WorkerPool<T> jobber = new WorkerPool<>(callback, ThreadPool.defaultThreadcount(threads), queueLimit, serialCallbacks);

        for (int i = 0; i < jobber.threads.length; i++)
        {
            final ThreadWorker<T> w = new ThreadWorker<>(jobber);
            final Thread t = new Thread(w);
            t.setDaemon(true);
            jobber.workers.offer(w);
            t.start();
            jobber.threads[i] = t;
        }

        if (serialCallbacks)
        {
            jobber.resultSync.acquireUninterruptibly();
            final Thread t = new Thread(jobber);
            t.setDaemon(true);
            t.start();
            jobber.callbackThread = t;
        }

        jobber.watcher = RequeueWatcher.start(jobber, jobber.jobs, jobber.workers);

        return jobber;
    }

    public final static int availableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public int threadCount()
    {
        return this.numThreads;
    }

    public void enqueue(final Worker<T> worker, final T object)
    {
        if (worker == null) throw new NullPointerException("A null Worker is not permitted");

        final ThreadWorker<T> w = this.workers.poll();
        final Job<T> job = new Job<>(worker, object);
        if (w != null)
        {
            w.setWorkLoad(job);
        }
        else
        {
            if (this.queueLimit != 0 && this.jobs.size() >= this.queueLimit)
            {
                final int ql = Math.max(this.queueLimit >> 1, 1);
                while (this.jobs.size() > ql)
                    SysUtils.fineSleep(5);
            }

            this.jobs.offer(job);
        }
    }

    private void reuseOrEnqueue(final ThreadWorker<T> w)
    {
        final Job<T> job = this.jobs.poll();
        if (job != null)
            w.setWorkLoad(job);
        else
            this.workers.offer(w);
    }

    void doCallback(final ThreadWorker<T> threadWorker, final WorkerStatus status, final Worker<T> worker, final T object)
    {
        if (this.serialCallbacks)
        {
            this.results.offer(new WorkerResult<>(worker, object, status));
            this.resultSync.release();
        }
        else
        {
            try
            {
                this.callback.workerCallback(this, status, worker, object);
            }
            catch (final Throwable t)
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
        while (!this.jobs.isEmpty() || (this.serialCallbacks && !this.results.isEmpty()))
            SysUtils.sleep(10);
    }

    public void stop()
    {
        final StopWorker<T> stop = new StopWorker<>();

        this.join();

        for (int i = 0; i < this.numThreads; i++)
            this.enqueue(stop, null);

        for (int i = 0; i < this.numThreads; i++)
            SysUtils.threadJoin(this.threads[i]);

        if (this.callbackThread != null)
        {
            this.results.offer(new WorkerResult<T>(null, null, WorkerStatus.OK));
            this.resultSync.release();
            SysUtils.threadJoin(this.callbackThread);
        }

        this.watcher.stop();
    }

    @Override
    public void run()
    {
        for (;;)
        {
            try
            {
                this.resultSync.acquireUninterruptibly();
                final WorkerResult<T> r = this.results.poll();
                if (r.worker == null) break;
                this.callback.workerCallback(this, r.status, r.worker, r.object);
            }
            catch (final Throwable t)
            {
                //
            }
        }
    }

    static class WorkerResult<T>
    {
        final Worker<T>    worker;
        final WorkerStatus status;
        final T            object;

        public WorkerResult(final Worker<T> worker, final T object, final WorkerStatus status)
        {
            this.worker = worker;
            this.object = object;
            this.status = status;
        }
    }

    static class ThreadWorker<T> implements Runnable
    {
        private final Semaphore     sync     = new Semaphore(1);
        private final WorkerPool<T> pool;
        private volatile Job<T>     workload = null;

        public ThreadWorker(final WorkerPool<T> pool)
        {
            this.sync.acquireUninterruptibly();
            this.pool = pool;
        }

        protected void setWorkLoad(final Job<T> job)
        {
            this.workload = job;
            this.sync.release();
        }

        @Override
        public void run()
        {
            for (;;)
            {
                boolean ok = true;
                Throwable ta = null;
                try
                {
                    this.sync.acquireUninterruptibly();
                    if (this.workload.worker instanceof StopWorker) break;
                    this.workload.worker.run(this.workload.object);
                }
                catch (final Throwable t)
                {
                    ta = t;
                    ok = false;
                }

                this.pool.doCallback(this, ok ? WorkerStatus.OK : new WorkerStatus(ta), this.workload.worker, this.workload.object);
            }
        }
    }

    static class Job<T>
    {
        public final Worker<T> worker;
        public final T         object;

        public Job(final Worker<T> worker, final T object)
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
        public void run(final T object)
        {
            // empty
        }
    }

    @Override
    public void requeue(final ThreadWorker<T> worker, final Job<T> job)
    {
        worker.setWorkLoad(job);
    }
}
