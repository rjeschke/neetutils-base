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

import com.github.rjeschke.neetutils.iterables.AbstractXIterable;

class XIterableCollect<A, B> extends AbstractXIterable<B>
{
    private final Iterable<? extends A> iterable;
    private final Collector<? super A, B> collector;

    public XIterableCollect(final Iterable<? extends A> iterable, final Collector<? super A, B> collector)
    {
        this.iterable = iterable;
        this.collector = collector;
    }

    @Override
    public Iterator<B> iterator()
    {
        return new XIterableCollect.XIterator<A, B>(this.iterable.iterator(), this.collector);
    }

    final static class XIterator<A, B> implements Iterator<B>
    {
        private final Iterator<? extends A> iterator;
        private final Collector<? super A, B> collector;
        
        public XIterator(final Iterator<? extends A> iterator, final Collector<? super A, B> collector)
        {
            this.iterator = iterator;
            this.collector = collector;
            this.collector.init();
        }

        @Override
        public boolean hasNext()
        {
            if(!this.collector.hasElement)
            {
                this.collector.collect(this.iterator);
            }
            return this.collector.hasElement;
        }

        @Override
        public B next()
        {
            if(!this.collector.hasElement)
            {
                throw new NoSuchElementException();
            }
            
            this.collector.hasElement = false;
            
            return this.collector.value;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
