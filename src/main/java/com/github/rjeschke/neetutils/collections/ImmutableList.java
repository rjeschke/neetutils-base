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
package com.github.rjeschke.neetutils.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class ImmutableList<A> implements List<A>, RandomAccess, Cloneable, Serializable 
{
    private static final long serialVersionUID = 634340469636541150L;

    private final ArrayList<A> list;
    
    public ImmutableList(Collection<? extends A> coll)
    {
        this.list = new ArrayList<A>(coll.size());
        for(final A a : coll)
            this.list.add(a);
        this.list.trimToSize();
    }
    
    public ImmutableList(A ... coll)
    {
        this.list = new ArrayList<A>(coll.length);
        for(final A a : coll)
            this.list.add(a);
        this.list.trimToSize();
    }
    
    public ImmutableList(Iterable<? extends A> coll)
    {
        this.list = new ArrayList<A>();
        for(final A a : coll)
            this.list.add(a);
        this.list.trimToSize();
    }
    
    private ImmutableList(ArrayList<A> list)
    {
        this.list = list;
    }
    
    @Override
    public int size()
    {
        return this.list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return this.list.contains(o);
    }

    @Override
    public Iterator<A> iterator()
    {
        return new ImmutableListIterator<A>(this.list, 0);
    }

    @Override
    public Object[] toArray()
    {
        return this.list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return this.list.toArray(a);
    }

    @Override
    public boolean add(A e)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return this.list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends A> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends A> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public A get(int index)
    {
        return this.list.get(index);
    }

    @Override
    public A set(int index, A element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, A element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public A remove(int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o)
    {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<A> listIterator()
    {
        return new ImmutableListIterator<A>(this.list, 0);
    }

    @Override
    public ListIterator<A> listIterator(int index)
    {
        return new ImmutableListIterator<A>(this.list, index);
    }

    @Override
    public List<A> subList(int fromIndex, int toIndex)
    {
        return new ImmutableList<A>((ArrayList<A>)this.list.subList(fromIndex, toIndex));
    }
    
    @Override
    public int hashCode()
    {
        return this.list.hashCode();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this)
            return true;
        
        if(!(obj instanceof ImmutableList))
            return false;
        
        return this.list.equals(((ImmutableList<?>)obj).list);
    }
    
    @Override
    public String toString()
    {
        return this.list.toString();
    }
    
    @Override
    public ImmutableList<A> clone()
    {
        // FIXME ... we are immutable, so we could just return 'this', isn't it?
        return new ImmutableList<A>(this.list);
    }
    
    private final static class ImmutableListIterator<A> implements ListIterator<A>
    {
        private final List<A> list;
        private int index = 0;
        private final int size;
        
        public ImmutableListIterator(List<A> list, int index)
        {
            this.list = list;
            this.index = index;
            this.size = this.list.size();
        }
        
        @Override
        public boolean hasNext()
        {
            return this.index < this.size;
        }

        @Override
        public A next()
        {
            if(this.index >= this.size)
                throw new NoSuchElementException();
            return this.list.get(this.index++);
        }

        @Override
        public boolean hasPrevious()
        {
            return this.index > 0;
        }

        @Override
        public A previous()
        {
            if(this.index == 0)
                throw new NoSuchElementException();
            return this.list.get(this.index - 1);
        }

        @Override
        public int nextIndex()
        {
            return this.index;
        }

        @Override
        public int previousIndex()
        {
            return this.index - 1;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(A e)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(A e)
        {
            throw new UnsupportedOperationException();
        }
    }
}
