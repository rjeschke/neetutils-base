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

public class NOutputStreamLE extends NOutputStream
{
    public NOutputStreamLE(OutputStream out)
    {
        super(out);
    }

    @Override
    public void write8(int value) throws IOException
    {
        LEIO.write8(this.out, value);
    }

    @Override
    public void write16(int value) throws IOException
    {
        LEIO.write16(this.out, value);
    }

    @Override
    public void write24(int value) throws IOException
    {
        LEIO.write24(this.out, value);
    }

    @Override
    public void write32(int value) throws IOException
    {
        LEIO.write32(this.out, value);
    }

    @Override
    public void write64(long value) throws IOException
    {
        LEIO.write64(this.out, value);
    }

    @Override
    public void writeFloat(float value) throws IOException
    {
        LEIO.writeFloat(this.out, value);
    }

    @Override
    public void writeDouble(double value) throws IOException
    {
        LEIO.writeDouble(this.out, value);
    }

    @Override
    public void writeString(String value, int length, int padding, String charsetName) throws IOException
    {
        LEIO.writeString(this.out, value, length, padding, charsetName);
    }

    @Override
    public void writeString(String value, int length, int padding) throws IOException
    {
        LEIO.writeString(this.out, value, length, padding);
    }

    @Override
    public void writeString8(String value) throws IOException
    {
        LEIO.writeString8(this.out, value);
    }

    @Override
    public void writeString16(String value) throws IOException
    {
        LEIO.writeString16(this.out, value);
    }

    @Override
    public void writeString32(String value) throws IOException
    {
        LEIO.writeString32(this.out, value);
    }

    @Override
    public void writeString(String value, int length) throws IOException
    {
        LEIO.writeString(this.out, value, length, 0);
    }

    @Override
    public ByteOrder getByteOrder()
    {
        return ByteOrder.LITTLE_ENDIAN;
    }

}
