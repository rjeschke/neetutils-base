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
package com.github.rjeschke.neetutils.fn;

import java.util.Collections;
import java.util.Iterator;

import com.github.rjeschke.neetutils.Objects;

public abstract class Maybe<A> implements Iterable<A>
{
    public abstract boolean isNothing();
    public abstract Maybe<A> or(Maybe<A> a);
    
    public final static <A> Maybe<A> nothing()
    {
        return new Nothing<A>();
    }

    public final static <A> Maybe<A> just(A value)
    {
        return new Just<A>(value);
    }
    
    private final static class Nothing<A> extends Maybe<A>
    {
        public Nothing()
        {
            //
        }
        
        @Override
        public Iterator<A> iterator()
        {
            return Collections.<A>emptyList().iterator();
        }

        @Override
        public boolean isNothing()
        {
            return true;
        }

        @Override
        public Maybe<A> or(Maybe<A> a)
        {
            return a;
        }
        
        @Override
        public int hashCode()
        {
            return 0;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            return false;
        }
        
        @Override
        public String toString()
        {
            return "nothing";
        }
    }
    
    
    private final static class Just<A> extends Maybe<A>
    {
        private final A value;
        
        public Just(A value)
        {
            this.value = value;
        }
        
        @Override
        public Iterator<A> iterator()
        {
            return Collections.singleton(this.value).iterator();
        }

        @Override
        public boolean isNothing()
        {
            return false;
        }

        @Override
        public Maybe<A> or(Maybe<A> a)
        {
            return this;
        }
        
        @Override
        public int hashCode()
        {
            return this.value != null ? this.value.hashCode() : 0;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if(!(obj instanceof Just))
                return false;
            final Just<?> just = (Just<?>)obj;
            
            return Objects.equals(this.value, just.value);
        }
        
        @Override
        public String toString()
        {
            return this.value != null ? this.value.toString() : "null";
        }
    }
}
