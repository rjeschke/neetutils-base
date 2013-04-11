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

import java.util.Collections;
import java.util.Iterator;

class XIterableConcat2<A> extends AbstractXIterable<A>
{
    private final Iterable<? extends Iterable<? extends A>> iterable;

    public XIterableConcat2(final Iterable<? extends Iterable<? extends A>> iterable)
    {
        this.iterable = iterable;
    }

    @Override
    public Iterator<A> iterator()
    {
        return new XIterableConcat2.XIterator<>(this.iterable.iterator());
    }

    private final static class XIterator<A> implements Iterator<A>
    {
        private final Iterator<? extends Iterable<? extends A>> iterator;
        private Iterator<? extends A>                           innerIterator = Collections.<A> emptyList().iterator();

        public XIterator(final Iterator<? extends Iterable<? extends A>> iterator)
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            while (!this.innerIterator.hasNext() && this.iterator.hasNext())
            {
                this.innerIterator = this.iterator.next().iterator();
            }

            return this.innerIterator.hasNext();
        }

        @Override
        public A next()
        {
            return this.innerIterator.next();
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
