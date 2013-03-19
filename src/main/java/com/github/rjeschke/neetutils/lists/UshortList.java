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
package com.github.rjeschke.neetutils.lists;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.RandomAccess;

/**
 * Unsigned short list.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class UshortList implements RandomAccess, Cloneable, Serializable
{
    /** serialVersionUID */
    private static final long          serialVersionUID = -3837395301685776241L;
    /** Initial size. */
    private transient final static int INIT_SIZE        = 16;
    /** Our backing array. */
    private transient short[]          data;
    /** Number of data elements. */
    private int                        size;
    /** Maximum size. */
    private transient int              maxSize;

    /**
     * Creates a list with default initial capacity.
     */
    public UshortList()
    {
        this(INIT_SIZE);
    }

    /**
     * Creates a list with a given initial capacity.
     * 
     * @param initialCapacity
     *            Initial capacity.
     */
    public UshortList(final int initialCapacity)
    {
        if (initialCapacity < 0) throw new IllegalArgumentException("Initial capacity must not be less than 0");

        this.maxSize = initialCapacity;
        this.size = 0;
        this.data = new short[initialCapacity];
    }

    /**
     * Returns a list containing the given values.
     * 
     * @param values
     *            Values to create the list from.
     * @return The list.
     */
    public static UshortList fromArray(final int... values)
    {
        final UshortList list = new UshortList(values.length);
        for (int i = 0; i < values.length; i++)
        {
            list.data[i] = (short)values[i];
        }
        list.size = values.length;
        return list;
    }

    /**
     * Returns a list containing the given values.
     * 
     * @param values
     *            Values to create the list from.
     * @return The list.
     */
    public static UshortList fromArray(final short... values)
    {
        final UshortList list = new UshortList(values.length);
        System.arraycopy(values, 0, list.data, 0, values.length);
        list.size = values.length;
        return list;
    }

    /**
     * Gets the size of this list.
     * 
     * @return The size.
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Checks if this list is empty.
     * 
     * @return <code>true</code> if this list is empty.
     */
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * Clears this list without releasing unnecessary storage.
     */
    public void clear()
    {
        this.size = 0;
    }

    /**
     * Shrinks the backing array of this list to the used size.
     */
    public void trimToSize()
    {
        if (this.maxSize > this.size)
        {
            this.maxSize = this.size;
            this.data = Arrays.copyOf(this.data, this.size);
        }
    }

    /**
     * Adds an element to the end of this list.
     * 
     * @param v
     *            The element to add.
     * @return The added element.
     */
    public int add(final int v)
    {
        this.grow(this.size + 1);
        this.data[this.size++] = (short)v;
        return v;
    }

    /**
     * Adds (inserts) an element at the given index.
     * 
     * @param index
     *            The index.
     * @param v
     *            The element to add.
     * @return The added element.
     */
    public int add(final int index, final int v)
    {
        if (index == this.size) return this.add(v);

        if (index < 0 || index > this.size) throw new ArrayIndexOutOfBoundsException(index);

        this.grow(this.size + 1);

        System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
        this.data[index] = (short)v;
        this.size++;

        return v;
    }

    /**
     * Removes the element at the specified position.
     * 
     * @param index
     *            Index of element to remove.
     * @return The removed element.
     */
    public int remove(final int index)
    {
        if (index >= this.size) throw new ArrayIndexOutOfBoundsException(index);
        final int old = this.data[index];
        this.size--;
        if (index != this.size)
        {
            System.arraycopy(this.data, index + 1, this.data, index, this.size - index);
        }
        return old & 65535;
    }

    /**
     * Sets the element at the given position.
     * 
     * @param index
     *            Index of element to set.
     * @param v
     *            The value to set.
     * @return The new value.
     */
    public int set(final int index, final int v)
    {
        if (index >= this.size) throw new ArrayIndexOutOfBoundsException(index);
        this.data[index] = (short)v;
        return v;
    }

    /**
     * Gets the element at the given position.
     * 
     * @param index
     *            The elements index.
     * @return The elements value.
     */
    public int get(final int index)
    {
        if (index >= this.size) throw new ArrayIndexOutOfBoundsException(index);
        return this.data[index] & 65535;
    }

    /**
     * Returns a copy of this list's content as an array.
     * 
     * @return The array.
     */
    public short[] toArray()
    {
        return Arrays.copyOf(this.data, this.size);
    }

    /**
     * Checks if this list contains the given value.
     * 
     * @param v
     *            The value.
     * @return <code>true</code> if the list contains the value
     */
    public boolean contains(final int v)
    {
        return this.indexOf(v) != -1;
    }

    /**
     * Returns the first occurrence of the given value in this list.
     * 
     * @param v
     *            The value.
     * @return The index or <code>-1</code> if the value was not found.
     */
    public int indexOf(final int v)
    {
        final short c = (short)v;
        for (int i = 0; i < this.size; i++)
        {
            if (c == this.data[i])
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the last occurrence of the given value in this list.
     * 
     * @param v
     *            The value.
     * @return The index or <code>-1</code> if the value was not found.
     */
    public int lastIndexOf(final int v)
    {
        final short c = (short)v;
        for (int i = this.size - 1; i >= 0; i--)
        {
            if (c == this.data[i])
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (this.size > 0)
        {
            sb.append(this.data[0] & 65535);
            for (int i = 1; i < this.size; i++)
            {
                sb.append(',');
                sb.append(this.data[i] & 65535);
            }
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 1;
        for (int i = 0; i < this.size; i++)
        {
            hash = hash * 31 + this.data[i];
        }
        return hash;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object other)
    {
        if (other == this)
        {
            return true;
        }

        if (!(other instanceof UshortList))
        {
            return false;
        }

        final UshortList list = (UshortList)other;

        if (list.size != this.size)
        {
            return false;
        }

        for (int i = 0; i < this.size; i++)
        {
            if (this.data[i] != list.data[i])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a clone of this list.
     */
    @Override
    public UshortList clone()
    {
        final UshortList list = new UshortList(this.size);
        list.size = this.size;
        System.arraycopy(this.data, 0, list.data, 0, this.size);
        return list;
    }

    /**
     * Serialize object.
     * 
     * @param out
     *            Output stream.
     * @throws IOException
     *             on IO error.
     */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        for (int i = 0; i < this.size; i++)
        {
            out.writeShort(this.data[i]);
        }
    }

    /**
     * Deserialize object.
     * 
     * @param in
     *            Input stream.
     * @throws IOException
     *             on IO error.
     * @throws ClassNotFoundException
     *             on class not found.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.data = new short[this.maxSize = this.size];
        for (int i = 0; i < this.size; i++)
        {
            this.data[i] = in.readShort();
        }
    }

    /**
     * Grows this list if required.
     * 
     * @param required
     *            Minimum size required.
     */
    private void grow(final int required)
    {
        if (required > this.maxSize)
        {
            this.maxSize = ((this.maxSize * 3) >>> 1) + 1;
            this.data = Arrays.copyOf(this.data, this.maxSize);
        }
    }
}
