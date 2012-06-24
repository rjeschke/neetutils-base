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

import java.util.Arrays;

import com.github.rjeschke.neetutils.math.NMath;

/**
 * Integer primitive list with focus on fast adding and low memory usage (for
 * large list sizes).
 * 
 * Roughly 16 times faster on 'add' and 2 times slower on 'get' compared to an
 * ArrayList (benchmarked using 16M adds/gets).
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public class IntList2
{
    /**
     * Default block size.
     */
    public final static int DEFAULT_BLOCK_SIZE = 32768;

    private Block[] blkmap = new Block[1];
    private int bpos = 0, spos = 0, size = 0;
    private final int blkSize, blkMask, shift;

    /**
     * Uses DEFAULT_BLOCK_SIZE.
     */
    public IntList2()
    {
        this(DEFAULT_BLOCK_SIZE);
    }

    /**
     * @param blkSize
     *            The block size (gets rounded up to next pow2)
     */
    public IntList2(final int blkSize)
    {
        this.blkSize = NMath.nextPow2(blkSize);
        this.blkMask = this.blkSize - 1;
        this.shift = (int)(Math.log(this.blkSize) / Math.log(2));
        this.blkmap[0] = new Block(this.blkSize);
    }

    /**
     * Gets this list's size.
     * 
     * @return The size.
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Adds an element to the end of this list.
     * 
     * @param value
     *            Value to add.
     */
    public void add(final int value)
    {
        if(this.spos == this.blkSize)
        {
            this.spos = 0;
            this.bpos++;
            if(this.bpos == this.blkmap.length)
                this.blkmap = Arrays.copyOf(this.blkmap, this.blkmap.length << 1);
            this.blkmap[this.bpos] = new Block(this.blkSize);
        }
        this.size++;
        this.blkmap[this.bpos].d[this.spos++] = value;
    }

    /**
     * Sets a new value at index.
     * 
     * @param index
     *            The index.
     * @param value
     *            The value.
     * @throws ArrayIndexOutOfBoundsException
     *             if index is out of range
     */
    public void set(final int index, final int value)
    {
        if(index < 0 || index >= this.size)
            throw new ArrayIndexOutOfBoundsException("Got " + index + ", range is [0, " + this.size + "[");
        this.blkmap[index >>> this.shift].d[index & this.blkMask] = value;
    }

    /**
     * Sets a new value at index, returns the old value at that index.
     * 
     * @param index
     *            The index.
     * @param value
     *            The value.
     * @return Old value at index.
     * @throws ArrayIndexOutOfBoundsException
     *             if index is out of range
     */
    public int getAndSet(final int index, final int value)
    {
        if(index < 0 || index >= this.size)
            throw new ArrayIndexOutOfBoundsException("Got " + index + ", range is [0, " + this.size + "[");
        final int ret = this.blkmap[index >>> this.shift].d[index & this.blkMask];
        this.blkmap[index >>> this.shift].d[index & this.blkMask] = value;
        return ret;
    }

    /**
     * Gets the value at the given index.
     * 
     * @param index
     *            The index.
     * @return The value.
     * @throws ArrayIndexOutOfBoundsException
     *             if index is out of range
     */
    public int get(final int index)
    {
        if(index < 0 || index >= this.size)
            throw new ArrayIndexOutOfBoundsException("Got " + index + ", range is [0, " + this.size + "[");
        return this.blkmap[index >>> this.shift].d[index & this.blkMask];
    }

    /**
     * Copies this list's contents into a newly created array.
     * 
     * @return the array.
     */
    public int[] toArray()
    {
        final int[] arr = new int[this.size];
        for(int left = this.size, n = 0; left > 0; n++)
        {
            final int todo = Math.min(left, this.blkSize);
            System.arraycopy(this.blkmap[n].d, 0, arr, this.size - left, todo);
            left -= todo;
        }
        return arr;
    }

    private final static class Block
    {
        public final int[] d;

        public Block(final int size)
        {
            this.d = new int[size];
        }
    }
}
