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
import java.io.OutputStream;

/**
 * Little endian stream IO methods.
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public final class LEIO
{
    private LEIO()
    { /* forbidden */
    }

    public final static void write8(final OutputStream out, final int value) throws IOException
    {
        out.write(value);
    }

    public final static void write16(final OutputStream out, final int value) throws IOException
    {
        out.write(value);
        out.write(value >> 8);
    }

    public final static void write24(final OutputStream out, final int value) throws IOException
    {
        out.write(value);
        out.write(value >> 8);
        out.write(value >> 16);
    }

    public final static void write32(final OutputStream out, final int value) throws IOException
    {
        out.write(value);
        out.write(value >> 8);
        out.write(value >> 16);
        out.write(value >> 24);
    }

    public final static void write64(final OutputStream out, final long value) throws IOException
    {
        out.write((int)(value));
        out.write((int)(value >> 8));
        out.write((int)(value >> 16));
        out.write((int)(value >> 24));
        out.write((int)(value >> 32));
        out.write((int)(value >> 40));
        out.write((int)(value >> 48));
        out.write((int)(value >> 56));
    }

    public final static void writeFloat(final OutputStream out, final float value) throws IOException
    {
        write32(out, Float.floatToIntBits(value));
    }

    public final static void writeDouble(final OutputStream out, final double value) throws IOException
    {
        write64(out, Double.doubleToLongBits(value));
    }

    public final static void writeString(final OutputStream out, final String value, final int length,
            final int padding, final String charsetName) throws IOException
    {
        BEIO.writeString(out, value, length, padding, charsetName);
    }

    public final static void writeString(final OutputStream out, final String value, final int length, final int padding)
            throws IOException
    {
        BEIO.writeString(out, value, length, padding, "UTF-8");
    }

    public final static void writeString8(final OutputStream out, final String value) throws IOException
    {
        BEIO.writeString8(out, value);
    }

    public final static void writeString16(final OutputStream out, final String value) throws IOException
    {
        final byte[] bytes = value.getBytes("UTF-8");
        if(bytes.length > 65535)
            throw new IOException("String too long (" + bytes.length + ")");
        write16(out, bytes.length);
        out.write(bytes);
    }

    public final static void writeString32(final OutputStream out, final String value) throws IOException
    {
        final byte[] bytes = value.getBytes("UTF-8");
        write32(out, bytes.length);
        out.write(bytes);
    }

    public final static byte readI8(final InputStream in) throws IOException
    {
        return (byte)in.read();
    }

    public final static int readU8(final InputStream in) throws IOException
    {
        return in.read();
    }

    public final static short readI16(final InputStream in) throws IOException
    {
        return (short)readU16(in);
    }

    public final static int readU16(final InputStream in) throws IOException
    {
        int v = in.read();
        v |= in.read() << 8;
        return v;
    }

    public final static int readI24(final InputStream in) throws IOException
    {
        int v = readU24(in);
        if(v >= 0x800000)
            return v - 0x1000000;
        return v;
    }

    public final static int readU24(final InputStream in) throws IOException
    {
        int v = in.read();
        v |= in.read() << 8;
        v |= in.read() << 16;
        return v;
    }

    public final static int readI32(final InputStream in) throws IOException
    {
        int v = in.read();
        v |= in.read() << 8;
        v |= in.read() << 16;
        v |= in.read() << 24;
        return v;
    }

    public final static long readU32(final InputStream in) throws IOException
    {
        return readI32(in) & 0xffffffffL;
    }

    public final static long readI64(final InputStream in) throws IOException
    {
        long v = in.read();
        v |= in.read() << 8;
        v |= in.read() << 16;
        v |= (long)in.read() << 24;
        v |= (long)in.read() << 32;
        v |= (long)in.read() << 40;
        v |= (long)in.read() << 48;
        v |= (long)in.read() << 56;
        return v;
    }

    public final static float readFloat(final InputStream in) throws IOException
    {
        return Float.intBitsToFloat(readI32(in));
    }

    public final static double readDouble(final InputStream in) throws IOException
    {
        return Double.longBitsToDouble(readI64(in));
    }

    public final static String readString8(final InputStream in) throws IOException
    {
        return BEIO.readStringN(in, readU8(in));
    }

    public final static String readString16(final InputStream in) throws IOException
    {
        return BEIO.readStringN(in, readU16(in));
    }

    public final static String readString32(final InputStream in) throws IOException
    {
        return BEIO.readStringN(in, readI32(in));
    }

    public final static String readString(final InputStream in, final int length, final int padding,
            final String charsetName) throws IOException
    {
        return BEIO.readString(in, length, padding, charsetName);
    }

    public final static String readString(final InputStream in, final int length, final int padding) throws IOException
    {
        return BEIO.readString(in, length, padding, "UTF-8");
    }

    public final static int readBytes(final InputStream in, final byte[] b) throws IOException
    {
        return BEIO.readBytes(in, b, 0, b.length);
    }

    public final static int readBytes(final InputStream in, final byte[] b, final int offs, final int len)
            throws IOException
    {
        return BEIO.readBytes(in, b, offs, len);
    }

    public final static long skipBytes(final InputStream in, final long bytes) throws IOException
    {
        return BEIO.skipBytes(in, bytes);
    }
}
