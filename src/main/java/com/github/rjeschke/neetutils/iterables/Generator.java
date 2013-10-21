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
package com.github.rjeschke.neetutils.iterables;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.SynchronousQueue;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 */
public abstract class Generator<A> implements Iterable<A>
{
    final ThreadLocal<GeneratorIterator<A>> generatorIterator = new ThreadLocal<>();

    protected final void yield(final A element)
    {
        this.generatorIterator.get().push(element);
    }

    public abstract void generate();

    @Override
    public final Iterator<A> iterator()
    {
        final GeneratorIterator<A> gi = new GeneratorIterator<>(this);
        final Thread thread = new Thread(gi);
        thread.setDaemon(true);
        thread.start();
        return gi;
    }

    private final static class GeneratorIterator<A> implements Iterator<A>, Runnable
    {
        private final SynchronousQueue<State<A>> queue = new SynchronousQueue<>();
        private final Generator<A>               generator;
        private State<A>                         state;

        public GeneratorIterator(final Generator<A> generator)
        {
            this.generator = generator;
        }

        public void push(final A element)
        {
            try
            {
                this.queue.put(new State<>(element, true));
            }
            catch (final InterruptedException e)
            {
                //
            }
        }

        public void close()
        {
            try
            {
                this.queue.put(new State<A>(null, false));
            }
            catch (final InterruptedException e)
            {
                //
            }
        }

        @Override
        public boolean hasNext()
        {
            try
            {
                this.state = this.queue.take();
            }
            catch (final InterruptedException e)
            {
                return false;
            }
            return this.state.hasNext;
        }

        @Override
        public A next()
        {
            if (!this.state.hasNext)
            {
                throw new NoSuchElementException();
            }
            return this.state.element;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("Generators are read-only.");
        }

        @Override
        public void run()
        {
            this.generator.generatorIterator.set(this);

            this.generator.generate();

            this.close();
        }

        private final static class State<A>
        {
            public final A       element;
            public final boolean hasNext;

            public State(final A element, final boolean hasNext)
            {
                this.element = element;
                this.hasNext = hasNext;
            }
        }
    }
}
