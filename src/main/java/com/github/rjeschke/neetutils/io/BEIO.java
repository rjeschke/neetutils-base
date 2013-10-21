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
 * Big endian stream IO methods.
 *
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public final class BEIO
{
    private BEIO()
    {
        // meh!
    }

    /**
     * Writes a byte.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void write8(final OutputStream out, final int value) throws IOException
    {
        out.write(value);
    }

    /**
     * Writes a short.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void write16(final OutputStream out, final int value) throws IOException
    {
        out.write(value >> 8);
        out.write(value);
    }

    /**
     * Writes a 24 bit integer.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void write24(final OutputStream out, final int value) throws IOException
    {
        out.write(value >> 16);
        out.write(value >> 8);
        out.write(value);
    }

    /**
     * Writes an integer.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void write32(final OutputStream out, final int value) throws IOException
    {
        out.write(value >> 24);
        out.write(value >> 16);
        out.write(value >> 8);
        out.write(value);
    }

    /**
     * Writes a long.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void write64(final OutputStream out, final long value) throws IOException
    {
        out.write((int)(value >> 56L));
        out.write((int)(value >> 48L));
        out.write((int)(value >> 40L));
        out.write((int)(value >> 32L));
        out.write((int)(value >> 24));
        out.write((int)(value >> 16));
        out.write((int)(value >> 8));
        out.write((int)(value));
    }

    /**
     * Writes a float.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void writeFloat(final OutputStream out, final float value) throws IOException
    {
        write32(out, Float.floatToIntBits(value));
    }

    /**
     * Writes a double.
     *
     * @param out
     *            Stream to write to
     * @param value
     *            Value to write
     * @throws IOException
     *             if an IO error occurred
     */
    public final static void writeDouble(final OutputStream out, final double value) throws IOException
    {
        write64(out, Double.doubleToLongBits(value));
    }

    public final static void writeString(final OutputStream out, final String value, final int length, final int padding, final String charsetName)
            throws IOException
    {
        final byte[] bytes = value.getBytes(charsetName);
        if (bytes.length > length) throw new IOException("String too long, expected max. " + length + ", got " + bytes.length);
        for (int i = 0; i < length; i++)
            out.write(i < bytes.length ? bytes[i] : padding);
    }

    public final static void writeString(final OutputStream out, final String value, final int length, final int padding) throws IOException
    {
        writeString(out, value, length, padding, "UTF-8");
    }

    public final static void writeString8(final OutputStream out, final String value) throws IOException
    {
        final byte[] bytes = value.getBytes("UTF-8");
        if (bytes.length > 255) throw new IOException("String too long (" + bytes.length + ")");
        write8(out, bytes.length);
        out.write(bytes);
    }

    public final static void writeString16(final OutputStream out, final String value) throws IOException
    {
        final byte[] bytes = value.getBytes("UTF-8");
        if (bytes.length > 65535) throw new IOException("String too long (" + bytes.length + ")");
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
        int v = in.read() << 8;
        v |= in.read();
        return v;
    }

    public final static int readI24(final InputStream in) throws IOException
    {
        final int v = readU24(in);
        if (v >= 0x800000) return v - 0x1000000;
        return v;
    }

    public final static int readU24(final InputStream in) throws IOException
    {
        int v = in.read() << 16;
        v |= in.read() << 8;
        v |= in.read();
        return v;
    }

    public final static int readI32(final InputStream in) throws IOException
    {
        int v = in.read() << 24;
        v |= in.read() << 16;
        v |= in.read() << 8;
        v |= in.read();
        return v;
    }

    public final static long readU32(final InputStream in) throws IOException
    {
        return readI32(in) & 0xffffffffL;
    }

    public final static long readI64(final InputStream in) throws IOException
    {
        long v = (long)in.read() << 56L;
        v |= (long)in.read() << 48;
        v |= (long)in.read() << 40;
        v |= (long)in.read() << 32;
        v |= (long)in.read() << 24;
        v |= in.read() << 16;
        v |= in.read() << 8;
        v |= in.read();
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

    protected final static String readStringN(final InputStream in, final int length) throws IOException
    {
        final byte[] bytes = new byte[length];
        final int r = readBytes(in, bytes);
        if (r != bytes.length) throw new IOException("Unexpected end of stream while reading string, expected " + bytes.length + ", got " + r + " bytes");
        return new String(bytes, "UTF-8");
    }

    public final static String readString8(final InputStream in) throws IOException
    {
        return readStringN(in, readU8(in));
    }

    public final static String readString16(final InputStream in) throws IOException
    {
        return readStringN(in, readU16(in));
    }

    public final static String readString32(final InputStream in) throws IOException
    {
        return readStringN(in, readI32(in));
    }

    public final static String readString(final InputStream in, final int length, final int padding, final String charsetName) throws IOException
    {
        final byte[] bytes = new byte[length];
        final int r = readBytes(in, bytes);
        if (r != length) throw new IOException("Unexpected end of stream while reading string, expected " + length + ", got " + r + " bytes");
        int n = length - 1;
        while (n >= 0 && bytes[n] == padding)
            n--;
        return new String(bytes, 0, n + 1, charsetName);
    }

    public final static String readString(final InputStream in, final int length, final int padding) throws IOException
    {
        return readString(in, length, padding, "UTF-8");
    }

    public final static int readBytes(final InputStream in, final byte[] b) throws IOException
    {
        return readBytes(in, b, 0, b.length);
    }

    public final static int readBytes(final InputStream in, final byte[] b, final int offs, final int len) throws IOException
    {
        int todo = len, got = 0;
        while (todo > 0)
        {
            final int r = in.read(b, offs + got, todo);
            if (r == -1) break;
            todo -= r;
            got += r;
        }
        return got;
    }

    public final static long skipBytes(final InputStream in, final long bytes) throws IOException
    {
        long todo = bytes, done = 0;
        while (todo > 0)
        {
            final long r = in.skip(todo);
            if (r == -1) break;
            todo -= r;
            done += r;
        }
        return done;
    }
}
