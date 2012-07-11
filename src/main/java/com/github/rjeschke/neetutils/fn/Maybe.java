package com.github.rjeschke.neetutils.fn;

import java.util.Collections;
import java.util.Iterator;

import com.github.rjeschke.neetutils.SysUtils;

public abstract class Maybe<A> implements Iterable<A>
{
    public abstract boolean isNothing();
    public abstract Maybe<A> or(Maybe<A> a);
    
    public final static <A> Maybe<A> nothing()
    {
        return new Maybe<A>()
        {
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
        };
    }

    public final static <A> Maybe<A> just(A value)
    {
        return new Just<A>(value);
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
            
            return SysUtils.equals(this.value, just.value);
        }
        
        @Override
        public String toString()
        {
            return this.value != null ? this.value.toString() : "null";
        }
    }
}
