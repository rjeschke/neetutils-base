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
package com.github.rjeschke.neetutils.dispose;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import com.github.rjeschke.neetutils.dispose.ReferenceList.Node;

public class Disposer extends WeakReference<Object>
{
    private final Disposable             disposable;
    private final Node<Disposer>         disposer;
    private boolean                      disposed = false;
    final static ReferenceQueue<Object>  refQueue;
    final static ReferenceList<Disposer> disposers;

    static
    {
        refQueue = new ReferenceQueue<>();
        disposers = new ReferenceList<>();

        final Thread thread = new Thread(new Cleaner());
        thread.setDaemon(true);
        thread.start();
    }

    public Disposer(Object referent, Disposable disposable)
    {
        super(referent, refQueue);
        this.disposable = disposable;
        synchronized (disposers)
        {
            this.disposer = disposers.add(this);
        }
    }

    public void dispose()
    {
        if (!this.disposed)
        {
            synchronized (disposers)
            {
                disposers.remove(this.disposer);
            }
            this.disposed = true;
            this.disposable.dispose();
        }
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }

    private static final class Cleaner implements Runnable
    {
        public Cleaner()
        {
            // empty
        }

        @Override
        public void run()
        {
            final ReferenceQueue<Object> refQueue = Disposer.refQueue;
            for (;;)
            {
                try
                {
                    ((Disposer)refQueue.remove()).dispose();
                }
                catch (Throwable t)
                {
                    //
                }
            }
        }
    }
}
