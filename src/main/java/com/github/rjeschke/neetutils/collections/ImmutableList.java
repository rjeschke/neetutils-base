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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.github.rjeschke.neetutils.Objects;

public class ImmutableList<A> implements List<A>, RandomAccess, Cloneable, Serializable 
{
    private static final long serialVersionUID = 634340469636541150L;
    
    private final Object[] data;
     
    public ImmutableList(Collection<? extends A> coll)
    {
        this.data = new Object[coll.size()];
        int i = 0;
        for(final A a : coll)
            this.data[i++] = a;
    }
    
    public ImmutableList(A ... coll)
    {
        this.data = Arrays.copyOf(coll, coll.length);
    }
    
    public ImmutableList(Iterable<? extends A> coll)
    {
        Object[] d = new Object[8];
        int i = 0;
        for(final A a : coll)
        {
            if(i == d.length)
                d = Arrays.copyOf(d, (d.length * 3) >> 1);
            d[i++] = a;
        }
        this.data = Arrays.copyOf(d, i);
    }
    
    @Override
    public int size()
    {
        return this.data.length;
    }

    @Override
    public boolean isEmpty()
    {
        return this.data.length == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        if(o == null)
        {
            for(int i = 0; i < this.data.length; i++)
            {
                if(this.data[i] == null)
                    return true;
            }
        }
        else
        {
            for(int i = 0; i < this.data.length; i++)
            {
                if(o.equals(this.data[i]))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<A> iterator()
    {
        return new ImmutableListIterator(0, this.data.length);
    }

    @Override
    public Object[] toArray()
    {
        return Arrays.copyOf(this.data, this.data.length);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a)
    {
        if(a.length >= this.data.length)
        {
            System.arraycopy(this.data, 0, a, 0, this.data.length);
            return a;
        }
        
        return (T[])Arrays.copyOf(this.data, this.data.length, a.getClass());
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
        for(final Object o : c)
        {
            if(!this.contains(o))
                return false;
        }
        return true;
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

    @SuppressWarnings("unchecked")
    @Override
    public A get(int index)
    {
        return (A)this.data[index];
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
        for(int i = 0; i < this.data.length; i++)
        {
            if(Objects.equals(this.data[i], o))
                return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        for(int i = this.data.length - 1; i >= 0; i--)
        {
            if(Objects.equals(this.data[i], o))
                return i;
        }
        return -1;
    }

    @Override
    public ListIterator<A> listIterator()
    {
        return new ImmutableListIterator(0, this.data.length);
    }

    @Override
    public ListIterator<A> listIterator(int index)
    {
        return new ImmutableListIterator(index, this.data.length - index);
    }

    @Override
    public List<A> subList(int fromIndex, int toIndex)
    {
        return null; //new ImmutableList<A>((ArrayList<A>)this.list.subList(fromIndex, toIndex));
    }

    /*
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
    */
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(int i = 0; i < this.data.length; i++)
        {
            if(i != 0)
                sb.append(", ");
            sb.append(this.data[i].toString());
        }
        sb.append(']');
        return sb.toString();
    }
    
    @Override
    public ImmutableList<A> clone()
    {
        return this;
    }
    
    private final class ImmutableListIterator implements ListIterator<A>
    {
        private final int end;
        private final int start;
        private int index;
        
        public ImmutableListIterator(int index, int size)
        {
            this.index = this.start = index;
            this.end = this.start + size;
        }
        
        @Override
        public boolean hasNext()
        {
            return this.index != this.end;
        }

        @Override
        public A next()
        {
            return get(this.index++);
        }

        @Override
        public boolean hasPrevious()
        {
            return this.index > this.start;
        }

        @Override
        public A previous()
        {
            return get(this.index - 1);
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
