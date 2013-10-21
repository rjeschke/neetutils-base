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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public abstract class NOutputStream extends FilterOutputStream
{
    protected NOutputStream(final OutputStream out)
    {
        super(out);
    }

    public static NOutputStream fromStream(final OutputStream in, final ByteOrder byteOrder)
    {
        if (byteOrder == ByteOrder.BIG_ENDIAN) return new NOutputStreamBE(in);
        return new NOutputStreamLE(in);
    }

    public abstract void write8(final int value) throws IOException;

    public abstract void write16(final int value) throws IOException;

    public abstract void write24(final int value) throws IOException;

    public abstract void write32(final int value) throws IOException;

    public abstract void write64(final long value) throws IOException;

    public abstract void writeFloat(final float value) throws IOException;

    public abstract void writeDouble(final double value) throws IOException;

    public abstract void writeString(final String value, final int length, final int padding, final String charsetName) throws IOException;

    public abstract void writeString(final String value, final int length, final int padding) throws IOException;

    public abstract void writeString(final String value, final int length) throws IOException;

    public abstract void writeString8(final String value) throws IOException;

    public abstract void writeString16(final String value) throws IOException;

    public abstract void writeString32(final String value) throws IOException;

    public abstract ByteOrder getByteOrder();
}
