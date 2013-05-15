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
package com.github.rjeschke.neetutils.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class NInputStreamLE extends NInputStream
{
    public NInputStreamLE(final InputStream in)
    {
        super(in);
    }

    @Override
    public int readI8() throws IOException
    {
        return LEIO.readI8(this.in);
    }

    @Override
    public int readU8() throws IOException
    {
        return LEIO.readU8(this.in);
    }

    @Override
    public int readI16() throws IOException
    {
        return LEIO.readI16(this.in);
    }

    @Override
    public int readU16() throws IOException
    {
        return LEIO.readU16(this.in);
    }

    @Override
    public int readI24() throws IOException
    {
        return LEIO.readI24(this.in);
    }

    @Override
    public int readU24() throws IOException
    {
        return LEIO.readU24(this.in);
    }

    @Override
    public int readI32() throws IOException
    {
        return LEIO.readI32(this.in);
    }

    @Override
    public long readU32() throws IOException
    {
        return LEIO.readU32(this.in);
    }

    @Override
    public long readI64() throws IOException
    {
        return LEIO.readI64(this.in);
    }

    @Override
    public float readFloat() throws IOException
    {
        return LEIO.readFloat(this.in);
    }

    @Override
    public double readDouble() throws IOException
    {
        return LEIO.readDouble(this.in);
    }

    @Override
    public String readString8() throws IOException
    {
        return LEIO.readString8(this.in);
    }

    @Override
    public String readString16() throws IOException
    {
        return LEIO.readString16(this.in);
    }

    @Override
    public String readString32() throws IOException
    {
        return LEIO.readString32(this.in);
    }

    @Override
    public String readString(final int length, final int padding, final String charsetName) throws IOException
    {
        return LEIO.readString(this.in, length, padding, charsetName);
    }

    @Override
    public String readString(final int length, final int padding) throws IOException
    {
        return LEIO.readString(this.in, length, padding);
    }

    @Override
    public String readString(final int length) throws IOException
    {
        return LEIO.readString(this.in, length, 0);
    }

    @Override
    public ByteOrder getByteOrder()
    {
        return ByteOrder.LITTLE_ENDIAN;
    }

}
