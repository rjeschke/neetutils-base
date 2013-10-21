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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 */
class XIterableDrop<A> extends AbstractXIterable<A>
{
    private final Iterable<A> iterable;
    private final int         amount;

    public XIterableDrop(final Iterable<A> iterable, final int amount)
    {
        this.iterable = iterable;
        this.amount = amount;
    }

    @Override
    public Iterator<A> iterator()
    {
        return new XIterableDrop.XIterator<>(this.iterable.iterator(), this.amount);
    }

    private final static class XIterator<A> implements Iterator<A>
    {
        private final Iterator<A> iterator;
        private final int         amount;
        private int               count;
        private A                 element;
        private boolean           hasElement;

        public XIterator(final Iterator<A> iterator, final int amount)
        {
            this.iterator = iterator;
            this.amount = amount;
        }

        @Override
        public boolean hasNext()
        {
            if (this.hasElement)
            {
                return true;
            }

            if (this.count < this.amount)
            {
                while (this.iterator.hasNext() && this.count < this.amount)
                {
                    this.count++;
                    this.iterator.next();
                }
            }

            if (this.iterator.hasNext())
            {
                this.element = this.iterator.next();
                this.hasElement = true;
            }

            return this.hasElement;
        }

        @Override
        public A next()
        {
            if (!this.hasElement)
            {
                throw new NoSuchElementException();
            }

            final A e = this.element;

            this.hasElement = false;

            return e;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
