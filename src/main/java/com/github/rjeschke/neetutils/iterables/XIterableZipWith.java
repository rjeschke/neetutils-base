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

import com.github.rjeschke.neetutils.fn.FnCombine;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
class XIterableZipWith<A, B, C> extends AbstractXIterable<C>
{
    private final Iterable<A>                        iterableA;
    private final Iterable<B>                        iterableB;
    private final FnCombine<? super A, ? super B, C> combine;

    public XIterableZipWith(final Iterable<A> iterableA, final Iterable<B> iterableB, final FnCombine<? super A, ? super B, C> combine)
    {
        this.iterableA = iterableA;
        this.iterableB = iterableB;
        this.combine = combine;
    }

    @Override
    public Iterator<C> iterator()
    {
        return new XIterableZipWith.XIterator<>(this.iterableA.iterator(), this.iterableB.iterator(), this.combine);
    }

    private final static class XIterator<A, B, C> implements Iterator<C>
    {
        private final Iterator<A>                        iteratorA;
        private final Iterator<B>                        iteratorB;
        private final FnCombine<? super A, ? super B, C> combine;

        public XIterator(final Iterator<A> iteratorA, final Iterator<B> iteratorB, final FnCombine<? super A, ? super B, C> combine)
        {
            this.iteratorA = iteratorA;
            this.iteratorB = iteratorB;
            this.combine = combine;
        }

        @Override
        public boolean hasNext()
        {
            return this.iteratorA.hasNext() && this.iteratorB.hasNext();
        }

        @Override
        public C next()
        {
            return this.combine.applyCombine(this.iteratorA.next(), this.iteratorB.next());
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
