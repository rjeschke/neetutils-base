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

import com.github.rjeschke.neetutils.fn.FnFoldStep;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 * @param <B>
 */
class XIterableReductions<A, B> extends AbstractXIterable<B>
{
    private final Iterable<A>                      iterable;
    private final FnFoldStep<? super A, ? super B> foldStep;
    private final B                                initialValue;

    public XIterableReductions(final Iterable<A> iterable, final FnFoldStep<? super A, ? super B> foldStep, final B initialValue)
    {
        this.iterable = iterable;
        this.foldStep = foldStep;
        this.initialValue = initialValue;
    }

    @Override
    public Iterator<B> iterator()
    {
        return new XIterableReductions.XIterator<>(this.iterable.iterator(), this.foldStep, this.initialValue);
    }

    private final static class XIterator<A, B> implements Iterator<B>
    {
        private final Iterator<A>                      iterator;
        private final FnFoldStep<? super A, ? super B> foldStep;
        private B                                      initialValue;
        private boolean                                isInitialValue = true;

        public XIterator(final Iterator<A> iterator, final FnFoldStep<? super A, ? super B> foldStep, final B initialValue)
        {
            this.iterator = iterator;
            this.foldStep = foldStep;
            this.initialValue = initialValue;
        }

        @Override
        public boolean hasNext()
        {
            return this.isInitialValue || this.iterator.hasNext();
        }

        @Override
        public B next()
        {
            if (this.isInitialValue)
            {
                this.isInitialValue = false;
                return this.initialValue;
            }
            this.initialValue = (B)this.foldStep.applyFoldStep(this.iterator.next(), this.initialValue);
            return this.initialValue;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
