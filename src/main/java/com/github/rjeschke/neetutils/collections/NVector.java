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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Vector;

public class NVector<E> implements NList<E>, RandomAccess, Cloneable
{
    private final Vector<E> list;

    public NVector(int initialCapacity)
    {
        this.list = new Vector<E>(initialCapacity);
    }

    public NVector()
    {
        this.list = new Vector<E>();
    }

    public NVector(Collection<? extends E> c)
    {
        this.list = new Vector<E>(c);
    }

    public NVector(E... args)
    {
        this(args.length);
        for(int i = 0; i < args.length; i++)
        {
            this.list.add(args[i]);
        }
    }

    @Override
    public NList<E> filter(ListFilter<E> filter)
    {
        NList<E> ret = new NVector<E>();
        for(E e : this.list)
        {
            if(!filter.filter(e))
            {
                ret.add(e);
            }
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public NList<E> sort()
    {
        Object[] arr = this.list.toArray();
        Arrays.sort(arr);
        for(int n = 0; n < arr.length; n++)
        {
            this.list.set(n, (E)arr[n]);
        }
        return this;
    }

    @Override
    @SuppressWarnings(
        { "unchecked", "rawtypes" })
    public NList<E> sort(Comparator<? super E> c)
    {
        Object[] arr = this.list.toArray();
        Arrays.sort(arr, (Comparator)c);
        for(int n = 0; n < arr.length; n++)
        {
            this.list.set(n, (E)arr[n]);
        }
        return this;
    }

    @Override
    public NList<NList<E>> split(ListFilter<E> filter)
    {
        NList<E> rt = new NVector<E>();
        NList<E> rf = new NVector<E>();
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

        NList<NList<E>> ret = new NVector<NList<E>>();
        ret.add(rt);
        ret.add(rf);
        return ret;
    }

    @Override
    public NList<E> modify(ListModifier<E> modifier)
    {
        for(int i = 0; i < this.list.size(); i++)
        {
            this.list.set(i, modifier.modify(this.list.get(i)));
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
        return this.list.add(e);
    }

    @Override
    public void add(int index, E element)
    {
        this.list.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        return this.list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c)
    {
        return this.list.addAll(index, c);
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
        return this.list.set(index, element);
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
        return this.list.get(0);
    }

    @Override
    public E getLast()
    {
        return this.list.get(this.list.size() - 1);
    }

    @SuppressWarnings("unchecked")
    private NVector(Vector<E> list, boolean clone)
    {
        this.list = clone ? (Vector<E>)list.clone() : list;
    }

    @Override
    public Object clone()
    {
        return new NVector<E>(this.list, true);
    }

    @Override
    public E removeFirst()
    {
        return this.list.remove(0);
    }

    @Override
    public E removeLast()
    {
        return this.list.remove(this.list.size() - 1);
    }
}
