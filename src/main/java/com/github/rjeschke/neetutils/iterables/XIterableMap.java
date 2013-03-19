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

import com.github.rjeschke.neetutils.fn.FnMapping;

class XIterableMap<A, B> extends AbstractXIterable<B>
{
    private final Iterable<? extends A> iterable;
    private final FnMapping<A, B>       mapping;

    public XIterableMap(final Iterable<? extends A> iterable, final FnMapping<A, B> mapping)
    {
        this.iterable = iterable;
        this.mapping = mapping;
    }

    @Override
    public Iterator<B> iterator()
    {
        return new XIterableMap.XIterator<>(this.iterable.iterator(), this.mapping);
    }

    private final static class XIterator<A, B> implements Iterator<B>
    {
        private final Iterator<? extends A> iterator;
        private final FnMapping<A, B>       mapping;

        public XIterator(final Iterator<? extends A> iterator, final FnMapping<A, B> mapping)
        {
            this.iterator = iterator;
            this.mapping = mapping;
        }

        @Override
        public boolean hasNext()
        {
            return this.iterator.hasNext();
        }

        @Override
        public B next()
        {
            return this.mapping.applyMapping(this.iterator.next());
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
