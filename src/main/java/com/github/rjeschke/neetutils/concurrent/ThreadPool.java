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
 */
public class ThreadPool
{
    private final int numThreads;
    private final int queueLimit;
    private final ConcurrentLinkedQueue<ThreadWorker> workers = new ConcurrentLinkedQueue<ThreadWorker>();
    private final ConcurrentLinkedQueue<Runnable> jobs = new ConcurrentLinkedQueue<Runnable>();
    private final Thread[] threads;

    private ThreadPool(int threads, int queueLimit)
    {
        this.numThreads = threads;
        this.queueLimit = Math.max(0, queueLimit);
        this.threads = new Thread[threads];
    }

    public final static int availableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }
    
    final static int defaultThreadcount(int threads)
    {
        return threads < 1 ? Runtime.getRuntime().availableProcessors() : threads;
    }
    
    public static ThreadPool start(int threads, int queueLimit)
    {
        final ThreadPool jobber = new ThreadPool(defaultThreadcount(threads), queueLimit);

        for(int i = 0; i < jobber.threads.length; i++)
        {
            final ThreadWorker w = new ThreadWorker(jobber);
            final Thread t = new Thread(w);
            t.setDaemon(true);
            jobber.workers.offer(w);
            t.start();
            jobber.threads[i] = t;
        }

        return jobber;
    }

    public int threadCount()
    {
        return this.numThreads;
    }

    public void enqueue(Runnable job)
    {
        if(job == null)
            throw new NullPointerException("A null Runnable is not permitted");

        final ThreadWorker w = this.workers.poll();
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

    void reuseOrEnqueue(ThreadWorker w)
    {
        final Runnable job = this.jobs.poll();
        if(job != null)
            w.setWorkLoad(job);
        else
            this.workers.offer(w);
    }

    public boolean hasWork()
    {
        return !this.jobs.isEmpty();
    }

    public void join()
    {
        while(!this.jobs.isEmpty())
            SysUtils.sleep(10);
    }

    public void stop()
    {
        final StopWorker stop = new StopWorker();
        for(int i = 0; i < this.numThreads; i++)
            this.enqueue(stop);

        for(int i = 0; i < this.numThreads; i++)
            SysUtils.threadJoin(this.threads[i]);
    }

    static class ThreadWorker implements Runnable
    {
        private final Semaphore sync = new Semaphore(1);
        private final ThreadPool pool;
        private volatile Runnable workload = null;

        public ThreadWorker(ThreadPool pool)
        {
            this.sync.acquireUninterruptibly();
            this.pool = pool;
        }

        protected void setWorkLoad(Runnable job)
        {
            this.workload = job;
            this.sync.release();
        }

        @Override
        public void run()
        {
            for(;;)
            {
                try
                {
                    this.sync.acquireUninterruptibly();
                    if(this.workload instanceof StopWorker)
                        break;
                    this.workload.run();
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
                
                this.pool.reuseOrEnqueue(this);
            }
        }
    }

    static class StopWorker implements Runnable
    {
        public StopWorker()
        {
            //
        }

        @Override
        public void run()
        {
            // empty
        }
    }
}
