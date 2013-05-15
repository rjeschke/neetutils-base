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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<E extends Comparable<? super E>> implements List<E>, Cloneable
{
    private final ArrayList<E>          list;
    private final Comparator<? super E> comparator;

    public SortedList()
    {
        this.list = new ArrayList<>();
        this.comparator = null;
    }

    public SortedList(final int initialSize)
    {
        this.list = new ArrayList<>(initialSize);
        this.comparator = null;
    }

    public SortedList(final Comparator<? super E> comparator)
    {
        this.list = new ArrayList<>();
        this.comparator = comparator;
    }

    public SortedList(final Collection<? extends E> c)
    {
        this.list = new ArrayList<>();
        this.comparator = null;
        this.addAll(c);
    }

    public SortedList(final Collection<? extends E> c, final Comparator<? super E> comparator)
    {
        this.list = new ArrayList<>();
        this.comparator = comparator;
        this.addAll(c);
    }

    @Override
    public boolean add(final E e)
    {
        final int index = this.comparator == null ? Collections.binarySearch(this.list, e) : Collections.binarySearch(this.list, e, this.comparator);
        if (index < 0)
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
    public void add(final int index, final E element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> c)
    {
        for (final E e : c)
        {
            this.add(e);
        }
        return c.size() > 0;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        this.list.clear();
    }

    @Override
    public boolean contains(final Object o)
    {
        return this.list.contains(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.list.containsAll(c);
    }

    @Override
    public E get(final int index)
    {
        return this.list.get(index);
    }

    @Override
    public int indexOf(final Object o)
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
    public int lastIndexOf(final Object o)
    {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator()
    {
        return this.list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index)
    {
        return this.list.listIterator(index);
    }

    @Override
    public boolean remove(final Object o)
    {
        return this.list.remove(o);
    }

    @Override
    public E remove(final int index)
    {
        return this.list.remove(index);
    }

    @Override
    public boolean removeAll(final Collection<?> c)
    {
        return this.list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c)
    {
        return this.list.retainAll(c);
    }

    @Override
    public E set(final int index, final E element)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        return this.list.size();
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex)
    {
        return this.list.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray()
    {
        return this.list.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a)
    {
        return this.list.toArray(a);
    }

    @Override
    public SortedList<E> clone()
    {
        return this.comparator == null ? new SortedList<>(this.list) : new SortedList<>(this.list, this.comparator);
    }

    @Override
    public int hashCode()
    {
        return this.list.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof SortedList)) return false;
        return this.list.equals(((SortedList<E>)obj).list);
    }

    @Override
    public String toString()
    {
        return this.list.toString();
    }

    public void trimToSize()
    {
        this.list.trimToSize();
    }
}
