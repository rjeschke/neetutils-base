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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public abstract class NInputStream extends FilterInputStream implements AutoCloseable
{
    protected NInputStream(final InputStream in)
    {
        super(in);
    }

    public static NInputStream fromStream(final InputStream in, final ByteOrder byteOrder)
    {
        if (byteOrder == ByteOrder.BIG_ENDIAN) return new NInputStreamBE(in);
        return new NInputStreamLE(in);
    }

    public abstract int readI8() throws IOException;

    public abstract int readU8() throws IOException;

    public abstract int readI16() throws IOException;

    public abstract int readU16() throws IOException;

    public abstract int readI24() throws IOException;

    public abstract int readU24() throws IOException;

    public abstract int readI32() throws IOException;

    public abstract long readU32() throws IOException;

    public abstract long readI64() throws IOException;

    public abstract float readFloat() throws IOException;

    public abstract double readDouble() throws IOException;

    public abstract String readString8() throws IOException;

    public abstract String readString16() throws IOException;

    public abstract String readString32() throws IOException;

    public abstract String readString(final int length, final int padding, final String charsetName) throws IOException;

    public abstract String readString(final int length, final int padding) throws IOException;

    public abstract String readString(final int length) throws IOException;

    public abstract ByteOrder getByteOrder();

    @Override
    public int read(final byte[] b) throws IOException
    {
        return BEIO.readBytes(this.in, b);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException
    {
        return BEIO.readBytes(this.in, b, off, len);
    }

    @Override
    public long skip(final long n) throws IOException
    {
        return BEIO.skipBytes(this.in, n);
    }
}
