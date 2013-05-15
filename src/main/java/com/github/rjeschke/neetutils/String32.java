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
package com.github.rjeschke.neetutils;

import java.util.Arrays;

/**
 * WIP mutable UTF-32 String implementation.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class String32
{
    private int[] chars;
    private int   length;

    public String32(final int initialSize)
    {
        this.chars = new int[Math.max(0, initialSize)];
    }

    public String32()
    {
        this(16);
    }

    public String32(final String string)
    {
        this(string.length());
        this.append(string);
    }

    public int length()
    {
        return this.length;
    }

    public int charAt(final int index)
    {
        if (index >= this.length)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return this.chars[index];
    }

    private void growIfNeeded()
    {
        if (this.length >= this.chars.length)
        {
            final int newLength = (int)Math.ceil(this.chars.length * 1.3);
            this.chars = Arrays.copyOf(this.chars, Math.max(this.chars.length + 1, newLength));
        }
    }

    public String32 shrinkToFit()
    {
        if (this.length != this.chars.length) this.chars = Arrays.copyOf(this.chars, this.length);

        return this;
    }

    public void append(final String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            final char c = str.charAt(i);
            if (c >= 0xd800 && c < 0xe000)
            {
                final char d = str.charAt(++i);
                if (c < 0xdc00 && d >= 0xdc00 && d < 0xe000)
                {
                    this.appendCodepoint((((c & 0x3ff) << 10) | (d & 0x3ff)) + 0x10000);
                }
                else
                {
                    if (d < 0xd800 || d > 0xdfff)
                    {
                        i--;
                    }
                    this.appendCodepoint(0xfffd);
                }
            }
            else
            {
                this.appendCodepoint(c);
            }
        }
    }

    public void append(final int number)
    {
        this.append(Integer.toString(number));
    }

    public void append(final float number)
    {
        this.append(Float.toString(number));
    }

    public void append(final double number)
    {
        this.append(Double.toString(number));
    }

    public void append(final boolean bool)
    {
        this.append(bool ? "true" : "false");
    }

    public void appendCodepoint(final int ch)
    {
        this.growIfNeeded();
        this.chars[this.length++] = ch;
    }

    @Override
    public int hashCode()
    {
        int h = 0;
        for (int i = 0; i < this.length; i++)
        {
            h = (h * 31) + this.chars[i];
        }
        return h;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof String32)) return false;

        final String32 s = (String32)obj;
        if (this.length != s.length) return false;

        for (int i = 0; i < this.length; i++)
        {
            if (this.chars[i] != s.chars[i]) return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder(this.length);
        for (int i = 0; i < this.length; i++)
        {
            sb.appendCodePoint(this.chars[i]);
        }
        return sb.toString();
    }
}
