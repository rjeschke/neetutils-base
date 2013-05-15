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
import java.io.OutputStream;
import java.nio.ByteOrder;

public class NOutputStreamBE extends NOutputStream
{
    public NOutputStreamBE(final OutputStream out)
    {
        super(out);
    }

    @Override
    public void write8(final int value) throws IOException
    {
        BEIO.write8(this.out, value);
    }

    @Override
    public void write16(final int value) throws IOException
    {
        BEIO.write16(this.out, value);
    }

    @Override
    public void write24(final int value) throws IOException
    {
        BEIO.write24(this.out, value);
    }

    @Override
    public void write32(final int value) throws IOException
    {
        BEIO.write32(this.out, value);
    }

    @Override
    public void write64(final long value) throws IOException
    {
        BEIO.write64(this.out, value);
    }

    @Override
    public void writeFloat(final float value) throws IOException
    {
        BEIO.writeFloat(this.out, value);
    }

    @Override
    public void writeDouble(final double value) throws IOException
    {
        BEIO.writeDouble(this.out, value);
    }

    @Override
    public void writeString(final String value, final int length, final int padding, final String charsetName) throws IOException
    {
        BEIO.writeString(this.out, value, length, padding, charsetName);
    }

    @Override
    public void writeString(final String value, final int length, final int padding) throws IOException
    {
        BEIO.writeString(this.out, value, length, padding);
    }

    @Override
    public void writeString8(final String value) throws IOException
    {
        BEIO.writeString8(this.out, value);
    }

    @Override
    public void writeString16(final String value) throws IOException
    {
        BEIO.writeString16(this.out, value);
    }

    @Override
    public void writeString32(final String value) throws IOException
    {
        BEIO.writeString32(this.out, value);
    }

    @Override
    public void writeString(final String value, final int length) throws IOException
    {
        BEIO.writeString(this.out, value, length, 0);
    }

    @Override
    public ByteOrder getByteOrder()
    {
        return ByteOrder.BIG_ENDIAN;
    }

}
