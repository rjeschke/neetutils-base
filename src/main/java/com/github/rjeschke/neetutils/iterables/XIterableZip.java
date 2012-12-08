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

import com.github.rjeschke.neetutils.collections.Tuple;

class XIterableZip<A, B> extends AbstractXIterable<Tuple<A, B>>
{
    private final Iterable<A> iterableA;
    private final Iterable<B> iterableB;

    public XIterableZip(final Iterable<A> iterableA, final Iterable<B> iterableB)
    {
        this.iterableA = iterableA;
        this.iterableB = iterableB;
    }

    @Override
    public Iterator<Tuple<A, B>> iterator()
    {
        return new XIterableZip.XIterator<A, B>(this.iterableA.iterator(), this.iterableB.iterator());
    }

    private final static class XIterator<A, B> implements Iterator<Tuple<A, B>>
    {
        private final Iterator<A> iteratorA;
        private final Iterator<B> iteratorB;

        public XIterator(final Iterator<A> iteratorA, final Iterator<B> iteratorB)
        {
            this.iteratorA = iteratorA;
            this.iteratorB = iteratorB;
        }

        @Override
        public boolean hasNext()
        {
            return this.iteratorA.hasNext() && this.iteratorB.hasNext();
        }

        @Override
        public Tuple<A, B> next()
        {
            return Tuple.of(this.iteratorA.next(), this.iteratorB.next());
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
