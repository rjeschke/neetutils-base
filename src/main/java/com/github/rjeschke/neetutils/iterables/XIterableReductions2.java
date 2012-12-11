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

class XIterableReductions2<A> extends AbstractXIterable<A>
{
    private final Iterable<? extends A> iterable;
    private final FnFoldStep<? super A, ? super A> foldStep;

    public XIterableReductions2(final Iterable<? extends A> iterable, final FnFoldStep<? super A, ? super A> foldStep)
    {
        this.iterable = iterable;
        this.foldStep = foldStep;
    }

    @Override
    public Iterator<A> iterator()
    {
        return new XIterableReductions2.XIterator<A>(this.iterable.iterator(), this.foldStep);
    }

    private final static class XIterator<A> implements Iterator<A>
    {
        private final Iterator<? extends A> iterator;
        private final FnFoldStep<? super A, ? super A> foldStep;
        private A initialValue;
        private boolean isInitialValue = true;

        public XIterator(final Iterator<? extends A> iterator, final FnFoldStep<? super A, ? super A> foldStep)
        {
            this.iterator = iterator;
            this.foldStep = foldStep;
        }

        @Override
        public boolean hasNext()
        {
            return this.iterator.hasNext();
        }

        @SuppressWarnings("unchecked")
        @Override
        public A next()
        {
            if(this.isInitialValue)
            {
                this.isInitialValue = false;
                this.initialValue = this.iterator.next();
            }
            else
            {
                this.initialValue = (A)this.foldStep.applyFoldStep(this.iterator.next(), this.initialValue);
            }
            return this.initialValue;
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("XIterators are read-only.");
        }
    }
}
