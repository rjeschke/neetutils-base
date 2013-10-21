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
import java.util.NoSuchElementException;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<T>>
{
    public final static double DEFAULT_GROWTH_FACTOR = 0.5;
    public final static int    DEFAULT_INITIAL_SIZE  = 16;

    private Object[]           data;
    private int                size;
    private final int          compMod;
    private final double       growthFactor;

    public BinaryHeap(final Type type)
    {
        this(type, DEFAULT_INITIAL_SIZE, DEFAULT_GROWTH_FACTOR);
    }

    public BinaryHeap(final Type type, final int initialSize)
    {
        this(type, initialSize, DEFAULT_GROWTH_FACTOR);
    }

    public BinaryHeap(final Type type, final int initialSize, final double growthFactor)
    {
        this.data = new Object[Math.max(initialSize, 1)];
        this.growthFactor = growthFactor;
        this.compMod = type == Type.MAX ? 1 : -1;
    }

    public int size()
    {
        return this.size;
    }

    public boolean isEmpty()
    {
        return this.size == 0;
    }

    public void clear()
    {
        Arrays.fill(this.data, null);
        this.size = 0;
    }

    public void shrinkToFit()
    {
        if (this.size != this.data.length)
        {
            this.data = Arrays.copyOf(this.data, this.size);
        }
    }

    void ensureCapacity()
    {
        if (this.size >= this.data.length)
        {
            final int newLen = this.data.length + Math.max(1, (int)(this.data.length * this.growthFactor));
            this.data = Arrays.copyOf(this.data, newLen);
        }
    }

    @SuppressWarnings("unchecked")
    public T get()
    {
        return this.size != 0 ? (T)this.data[0] : null;
    }

    @SuppressWarnings("unchecked")
    public T remove()
    {
        if (this.size == 0) throw new NoSuchElementException("Heap is empty");

        final T removed = (T)this.data[0];

        this.data[0] = this.data[--this.size];
        this.data[this.size] = null;

        int child, pos = 0;
        while ((child = 2 * pos + 1) < this.size)
        {
            if (child + 1 < this.size && ((T)this.data[child]).compareTo((T)this.data[child + 1]) * this.compMod < 0) child++;

            final T a = (T)this.data[pos];
            final T b = (T)this.data[child];
            if (a.compareTo(b) * this.compMod >= 0) break;

            this.data[pos] = b;
            this.data[child] = a;
            pos = child;
        }

        return removed;
    }

    @SuppressWarnings("unchecked")
    public void put(final T e)
    {
        this.ensureCapacity();
        this.data[this.size] = e;
        int parent, pos = this.size++;
        while ((parent = (pos - 1) >> 1) >= 0)
        {
            final T a = (T)this.data[pos];
            final T b = (T)this.data[parent];
            if (a.compareTo(b) * this.compMod <= 0) break;
            this.data[pos] = b;
            this.data[parent] = a;
            pos = parent;
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < this.size; i++)
        {
            if (i > 0) sb.append(", ");
            sb.append(this.data[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public enum Type
    {
        MIN, MAX
    }
}
