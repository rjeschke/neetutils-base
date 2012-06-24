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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class NSortedList<E extends Comparable<? super E>> implements NList<E>, Cloneable
{
    private final LinkedList<E> list;

    public NSortedList()
    {
        this.list = new LinkedList<E>();
    }

    public NSortedList(Collection<? extends E> c)
    {
        this.list = new LinkedList<E>();
        for(E e : c)
        {
            this.add(e);
        }
    }

    public NSortedList(E... args)
    {
        this.list = new LinkedList<E>();
        for(int i = 0; i < args.length; i++)
        {
            this.add(args[i]);
        }
    }

    @Override
    public NList<E> filter(ListFilter<E> filter)
    {
        NList<E> ret = new NSortedList<E>();
        for(E e : this.list)
        {
            if(!filter.filter(e))
            {
                ret.add(e);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public NList<E> sort()
    {
        Object[] arr = this.list.toArray();
        Arrays.sort(arr);
        ListIterator<E> i = this.list.listIterator();
        for(int n = 0; n < arr.length; n++)
        {
            i.next();
            i.set((E)arr[n]);
        }
        return this;
    }

    @SuppressWarnings(
        { "unchecked", "rawtypes" })
    @Override
    public NList<E> sort(Comparator<? super E> c)
    {
        Object[] arr = this.list.toArray();
        Arrays.sort(arr, (Comparator)c);
        ListIterator<E> i = this.list.listIterator();
        for(int n = 0; n < arr.length; n++)
        {
            i.next();
            i.set((E)arr[n]);
        }
        return this;
    }

    @Override
    public NList<NList<E>> split(ListFilter<E> filter)
    {
        NList<E> rt = new NSortedList<E>();
        NList<E> rf = new NSortedList<E>();
        for(E e : this.list)
        {
            if(filter.filter(e))
            {
                rt.add(e);
            }
            else
            {
                rf.add(e);
            }
        }

        NList<NList<E>> ret = new NArrayList<NList<E>>();
        ret.add(rt);
        ret.add(rf);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public NList<E> modify(ListModifier<E> modifier)
    {
        Object[] arr = this.list.toArray();
        this.list.clear();
        for(Object e : arr)
        {
            this.add(modifier.modify((E)e));
        }
        return this;
    }

    @Override
    public NList<E> operate(ListOperator<E> operator)
    {
        for(E e : this.list)
        {
            operator.operate(e);
        }
        return this;
    }

    @Override
    public boolean add(E e)
    {
        final int index = Collections.binarySearch(this.list, e);
        if(index < 0)
        {
            this.list.add(-index - 1, e);
        }
        else
        {
            this.list.add(index + 1, e);
        }
        return true;
    }

    @Override
    public void add(int index, E element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        for(E e : c)
        {
            this.add(e);
        }
        return c.size() > 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        this.list.clear();
    }

    @Override
    public boolean contains(Object o)
    {
        return this.list.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return this.list.containsAll(c);
    }

    @Override
    public E get(int index)
    {
        return this.list.get(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return this.list.indexOf(o);
    }

    @Override
    public boolean isEmpty()
    {
        return this.list.isEmpty();
    }

    @Override
    public Iterator<E> iterator()
    {
        return this.list.iterator();
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator()
    {
        return this.list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index)
    {
        return this.list.listIterator(index);
    }

    @Override
    public boolean remove(Object o)
    {
        return this.list.remove(o);
    }

    @Override
    public E remove(int index)
    {
        return this.list.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return this.list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return this.list.retainAll(c);
    }

    @Override
    public E set(int index, E element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        return this.list.size();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex)
    {
        return this.list.subList(fromIndex, toIndex);
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
    public E getFirst()
    {
        return this.list.getFirst();
    }

    @Override
    public E getLast()
    {
        return this.list.getLast();
    }

    @SuppressWarnings("unchecked")
    private NSortedList(LinkedList<E> list, boolean clone)
    {
        this.list = clone ? (LinkedList<E>)list.clone() : list;
    }

    @Override
    public Object clone()
    {
        return new NSortedList<E>(this.list, true);
    }

    @Override
    public E removeFirst()
    {
        return this.list.removeFirst();
    }

    @Override
    public E removeLast()
    {
        return this.list.removeLast();
    }
}
