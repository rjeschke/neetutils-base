/*
 * Copyright (C) 2013 René Jeschke <rene_jeschke@yahoo.de>
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
package com.github.rjeschke.neetutils;

public class Timer implements Runnable
{
    private final long           delay;
    private final Timer.Callback callback;
    private volatile boolean     running = true;

    private Timer(final double freq, final Timer.Callback callback)
    {
        this.delay = (long)(1000000000.0 / freq);
        this.callback = callback;
    }

    // TODO here's stuff missing
    public static Timer start(double freq, Timer.Callback callback)
    {
        final Timer t = new Timer(freq, callback);
        final Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();
        return t;
    }

    public void stop()
    {
        this.running = false;
    }

    public static interface Callback
    {
        public void timerCallback();
    }

    @Override
    public void run()
    {
        final Object sleeper = new Object();
        long t0;
        long toWait = 0;
        t0 = System.nanoTime();
        try
        {
            while (this.running)
            {
                this.callback.timerCallback();
                final long t1 = System.nanoTime() - t0;
                synchronized (sleeper)
                {
                    toWait += this.delay - t1;
                    final long millis = toWait / 1000000L;
                    if (millis >= 0)
                    {
                        sleeper.wait(millis);
                        toWait -= millis * 1000000L;
                    }
                }
                t0 += this.delay;
            }
        }
        catch (InterruptedException e)
        {
            // exit
        }
    }

}
