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

import java.util.Arrays;
import java.util.Iterator;

public class ArrayIterator<A> extends AbstractXIterable<A>
{
    private final A[] values;
    
    public ArrayIterator(final boolean defensiveCopy, final A ... values)
    {
        this.values = defensiveCopy ? Arrays.copyOf(values, values.length) : values;
    }
    
    public ArrayIterator(final A ... values)
    {
        this(true, values);
    }
    
    public final static <A> ArrayIterator<A> of(final A ... values)
    {
        return new ArrayIterator<A>(values);
    }
    
    public final static <A> ArrayIterator<A> unsafeOf(final A ... values)
    {
        return new ArrayIterator<A>(false, values);
    }
    
    @Override
    public Iterator<A> iterator()
    {
        return new XIterator<A>(this.values);
    }

    private final static class XIterator<A> implements Iterator<A>
    {
        private final A[] values;
        private int position = 0;
        
        public XIterator(final A ... values)
        {
            this.values = values;
        }
        
        @Override
        public boolean hasNext()
        {
            return this.position < this.values.length;
        }

        @Override
        public A next()
        {
            return this.values[this.position++];
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("ArrayIterator is read-only.");
        }
    }
}
