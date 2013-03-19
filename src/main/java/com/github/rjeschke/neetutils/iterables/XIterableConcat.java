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

class XIterableConcat<A> extends AbstractXIterable<A>
{
    private final Iterable<? extends A> iterableA;
    private final Iterable<? extends A> iterableB;

    public XIterableConcat(final Iterable<? extends A> iterableA, final Iterable<? extends A> iterableB)
    {
        this.iterableA = iterableA;
        this.iterableB = iterableB;
    }

    @Override
    public Iterator<A> iterator()
    {
        return new XIterableConcat.XIterator<>(this.iterableA.iterator(), this.iterableB.iterator());
    }

    private final static class XIterator<A> implements Iterator<A>
    {
        private final Iterator<? extends A> iteratorA;
        private final Iterator<? extends A> iteratorB;
        private boolean                     first = true;

        public XIterator(final Iterator<? extends A> iteratorA, final Iterator<? extends A> iteratorB)
        {
            this.iteratorA = iteratorA;
            this.iteratorB = iteratorB;
        }

        @Override
        public boolean hasNext()
        {
            if (this.first)
            {
                if (this.iteratorA.hasNext())
                {
                    return true;
                }
                this.first = false;
            }
            return this.iteratorB.hasNext();
        }

        @Override
        public A next()
        {
            return this.first ? this.iteratorA.next() : this.iteratorB.next();
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
