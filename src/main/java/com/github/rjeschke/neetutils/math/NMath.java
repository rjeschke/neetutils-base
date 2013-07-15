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
package com.github.rjeschke.neetutils.math;

/**
 * Some handy arithmetic methods.
 *
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public final class NMath
{
    private NMath()
    { /* forbidden */
    }

    public final static double INV_LOG_2  = 1.0 / Math.log(2);
    public final static double LOG_2      = Math.log(2);
    public final static float  INV_LOG_2f = (float)(1.0 / Math.log(2));
    public final static float  LOG_2f     = (float)Math.log(2);

    /** yotta */
    public final static double U_Y        = 1e24;
    /** zetta */
    public final static double U_Z        = 1e21;
    /** exa */
    public final static double U_E        = 1e18;
    /** peta */
    public final static double U_P        = 1e15;
    /** tera */
    public final static double U_T        = 1e12;
    /** giga */
    public final static double U_G        = 1e9;
    /** mega */
    public final static double U_M        = 1e6;
    /** kilo */
    public final static double U_k        = 1e3;
    /** hecto */
    public final static double U_h        = 1e2;
    /** deca */
    public final static double U_da       = 1e1;
    /** deci */
    public final static double U_d        = 1e-1;
    /** centi */
    public final static double U_c        = 1e-2;
    /** milli */
    public final static double U_m        = 1e-3;
    /** micro */
    public final static double U_u        = 1e-6;
    /** nano */
    public final static double U_n        = 1e-9;
    /** pico */
    public final static double U_p        = 1e-12;
    /** femto */
    public final static double U_f        = 1e-15;
    /** atto */
    public final static double U_a        = 1e-18;
    /** zepto */
    public final static double U_z        = 1e-21;
    /** yocto */
    public final static double U_y        = 1e-24;

    /**
     * Returns <code>min</code> if <code>x</code> is less than <code>min</code>,
     * <code>max</code> if <code>x</code> is greater than <code>max</code> and
     * </code>x</code> otherwise.
     *
     * @param x
     *            Value
     * @param min
     *            Minimum
     * @param max
     *            Maximum
     * @return Clamped value
     */
    public final static int clamp(final int x, final int min, final int max)
    {
        return Math.max(min, Math.min(max, x));
    }

    /**
     * Returns <code>min</code> if <code>x</code> is less than <code>min</code>,
     * <code>max</code> if <code>x</code> is greater than <code>max</code> and
     * </code>x</code> otherwise.
     *
     * @param x
     *            Value
     * @param min
     *            Minimum
     * @param max
     *            Maximum
     * @return Clamped value
     */
    public final static float clamp(final float x, final float min, final float max)
    {
        return Math.max(min, Math.min(max, x));
    }

    /**
     * Returns <code>min</code> if <code>x</code> is less than <code>min</code>,
     * <code>max</code> if <code>x</code> is greater than <code>max</code> and
     * </code>x</code> otherwise.
     *
     * @param x
     *            Value
     * @param min
     *            Minimum
     * @param max
     *            Maximum
     * @return Clamped value
     */
    public final static double clamp(final double x, final double min, final double max)
    {
        return Math.max(min, Math.min(max, x));
    }

    /**
     * Linearly interpolates between <code>a</code> and </code>b</code>.
     *
     * @param a
     *            A
     * @param b
     *            B
     * @param f
     *            Factor
     * @return Linearly interpolated value
     */
    public final static float lerp(final float a, final float b, final float f)
    {
        return a + (b - a) * f;
    }

    /**
     * Linearly interpolates between <code>a</code> and </code>b</code>.
     *
     * @param a
     *            A
     * @param b
     *            B
     * @param f
     *            Factor
     * @return Linearly interpolated value
     */
    public final static double lerp(final double a, final double b, final double f)
    {
        return a + (b - a) * f;
    }

    /**
     * Normalizes a denormalized float number.
     *
     * @param v
     *            Number to normalize
     * @return <code>0</code> if <code>abs(v)</code> is smaller than
     *         <code>Float.MIN_NORMAL</code>
     */
    public final static float normalize(final float v)
    {
        return Math.abs(v) < Float.MIN_NORMAL ? 0 : v;
    }

    /**
     * Normalizes a denormalized double number.
     *
     * @param v
     *            Number to normalize
     * @return <code>0</code> if <code>abs(v)</code> is smaller than
     *         <code>Double.MIN_NORMAL</code>
     */
    public final static double normalize(final double v)
    {
        return Math.abs(v) < Double.MIN_NORMAL ? 0 : v;
    }

    public final static int nextPow2(final int value)
    {
        return 1 << (int)Math.ceil(Math.log(value) * INV_LOG_2);
    }

    public final static float step(final float edge, final float x)
    {
        return x < edge ? 0.0f : 1.0f;
    }

    public final static double step(final double edge, final double x)
    {
        return x < edge ? 0.0 : 1.0;
    }

    public final static float smoothstep(final float edge0, final float edge1, final float x)
    {
        final float y = saturate((x - edge0) / (edge1 - edge0));
        return y * y * (3.0f - 2.0f * y);
    }

    public final static double smoothstep(final double edge0, final double edge1, final double x)
    {
        final double y = saturate((x - edge0) / (edge1 - edge0));
        return y * y * (3.0 - 2.0 * y);
    }

    public final static float saturate(final float x)
    {
        return Math.max(0, Math.min(1, x));
    }

    public final static double saturate(final double x)
    {
        return Math.max(0, Math.min(1, x));
    }

    public final static float fract(final float x)
    {
        return Math.min(x - (float)Math.floor(x), 0x1.fffffep-1f);
    }

    public final static double fract(final double x)
    {
        return Math.min(x - Math.floor(x), 0x1.ffffffffffff7ep-1);
    }

    /**
     * The sinc function.
     *
     * @param x
     *            x.
     * @return sinc of x.
     */
    public final static float sinc(final float x)
    {
        if (x != 0)
        {
            final float xpi = (float)Math.PI * x;
            return (float)Math.sin(xpi) / xpi;
        }
        return 1.f;
    }

    /**
     * Zeroth order modified Bessel function.
     *
     * @param x
     *            Value.
     * @return Return value.
     */
    public final static double i0(final double x)
    {
        double f = 1;
        final double x2 = x * x * 0.25;
        double xc = x2;
        double v = 1 + x2;
        for (int i = 2; i < 100; i++)
        {
            f *= i;
            xc *= x2;
            final double a = xc / (f * f);
            v += a;
            if (a < 1e-20) break;
        }
        return v;
    }

    /**
     * The sinc function.
     *
     * @param x
     *            x.
     * @return sinc of x.
     */
    public final static double sinc(final double x)
    {
        if (x != 0)
        {
            final double xpi = Math.PI * x;
            return Math.sin(xpi) / xpi;
        }
        return 1.0;
    }

    public final static long rol64(final long value, final int bits)
    {
        final int b = bits & 63;
        if (b == 0) return value;

        return (value << b) | (value >>> (64 - b));
    }

    public final static long ror64(final long value, final int bits)
    {
        final int b = bits & 63;
        if (b == 0) return value;

        return (value >>> b) | (value << (64 - b));
    }

    public final static int rol32(final int value, final int bits)
    {
        final int b = bits & 31;
        if (b == 0) return value;

        return (value << b) | (value >>> (32 - b));
    }

    public final static int ror32(final int value, final int bits)
    {
        final int b = bits & 31;
        if (b == 0) return value;

        return (value >>> b) | (value << (32 - b));
    }

    public final static int rol24(final int value, final int bits)
    {
        final int b = Math.abs(bits % 24);
        if (b == 0) return value;

        final int v = value & 0xffffff;
        return ((v >> b) | (v << (24 - b))) & 0xffffff;
    }

    public final static int ror24(final int value, final int bits)
    {
        final int b = Math.abs(bits % 24);
        if (b == 0) return value;

        final int v = value & 0xffffff;
        return ((v >> b) | (v << (24 - b))) & 0xffffff;
    }

    public final static int rol16(final int value, final int bits)
    {
        final int b = bits & 15;
        if (b == 0) return value;

        final int v = value & 65535;
        return ((v << b) | (v >> (16 - b))) & 65535;
    }

    public final static int ror16(final int value, final int bits)
    {
        final int b = bits & 15;
        if (b == 0) return value;

        final int v = value & 65535;
        return ((v >> b) | (v << (16 - b))) & 65535;
    }

    public final static int rol8(final int value, final int bits)
    {
        final int b = bits & 7;
        if (b == 0) return value;

        final int v = value & 255;
        return ((v << b) | (v >> (8 - b))) & 255;
    }

    public final static int ror8(final int value, final int bits)
    {
        final int b = bits & 7;
        if (b == 0) return value;

        final int v = value & 255;
        return ((v >> b) | (v << (8 - b))) & 255;
    }

    public final static double getButterworthFactor(final int n, final int k)
    {
        return -2.0 * Math.cos((2.0 * k + n - 1) / (2 * n) * Math.PI);
    }

    public final static double log2(final double x)
    {
        return Math.log(x) * INV_LOG_2;
    }

    public final static double exp2(final double x)
    {
        return Math.pow(2, x);
    }

    public final static float log2(final float x)
    {
        return (float)(Math.log(x) * INV_LOG_2);
    }

    public final static float exp2(final float x)
    {
        return (float)Math.pow(2, x);
    }

    public final static boolean doubleEquals(final double a, final double b, final double epsilon)
    {
        return Math.abs(a - b) <= epsilon;
    }

    public final static boolean floatEquals(final float a, final float b, final float epsilon)
    {
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static byte min(final byte[] xs)
    {
        byte v = Byte.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = (byte)Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static byte min(final byte[] xs, final int offset, final int length)
    {
        byte v = Byte.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = (byte)Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static byte max(final byte[] xs)
    {
        byte v = Byte.MIN_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = (byte)Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static byte max(final byte[] xs, final int offset, final int length)
    {
        byte v = Byte.MIN_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = (byte)Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static short min(final short[] xs)
    {
        short v = Short.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = (short)Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static short min(final short[] xs, final int offset, final int length)
    {
        short v = Short.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = (short)Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static short max(final short[] xs)
    {
        short v = Short.MIN_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = (short)Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static short max(final short[] xs, final int offset, final int length)
    {
        short v = Short.MIN_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = (short)Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static int min(final int[] xs)
    {
        int v = Integer.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static int min(final int[] xs, final int offset, final int length)
    {
        int v = Integer.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static int max(final int[] xs)
    {
        int v = Integer.MIN_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static int max(final int[] xs, final int offset, final int length)
    {
        int v = Integer.MIN_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static long min(final long[] xs)
    {
        long v = Long.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static long min(final long[] xs, final int offset, final int length)
    {
        long v = Long.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static long max(final long[] xs)
    {
        long v = Long.MIN_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static long max(final long[] xs, final int offset, final int length)
    {
        long v = Long.MIN_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static float min(final float[] xs)
    {
        float v = Float.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static float min(final float[] xs, final int offset, final int length)
    {
        float v = Float.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static float max(final float[] xs)
    {
        float v = -Float.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static float max(final float[] xs, final int offset, final int length)
    {
        float v = -Float.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of the absolute values of {@code xs}
     */
    public final static float amin(final float[] xs)
    {
        float v = Float.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The minimum value of the absolute values of {@code xs}
     */
    public final static float amin(final float[] xs, final int offset, final int length)
    {
        float v = Float.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The maximum value of the absolute values of {@code xs}
     */
    public final static float amax(final float[] xs)
    {
        float v = 0;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The maximum value of the absolute values of {@code xs}
     */
    public final static float amax(final float[] xs, final int offset, final int length)
    {
        float v = 0;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(Math.abs(xs[i]), v);
        }
        return v;
    }

    public final static float[] add(final float[] xs, final float y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] += y;
        }
        return xs;
    }

    public final static float[] add(final float[] xs, final int offset, final int length, final float y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] += y;
        }
        return xs;
    }

    public final static float[] sub(final float[] xs, final float y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] -= y;
        }
        return xs;
    }

    public final static float[] sub(final float[] xs, final int offset, final int length, final float y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] -= y;
        }
        return xs;
    }

    public final static float[] mul(final float[] xs, final float y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] *= y;
        }
        return xs;
    }

    public final static float[] mul(final float[] xs, final int offset, final int length, final float y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] *= y;
        }
        return xs;
    }

    public final static float[] div(final float[] xs, final float y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] /= y;
        }
        return xs;
    }

    public final static float[] div(final float[] xs, final int offset, final int length, final float y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] /= y;
        }
        return xs;
    }

    public final static float[] pow(final float[] xs, final float y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] = (float)Math.pow(xs[i], y);
        }
        return xs;
    }

    public final static float[] pow(final float[] xs, final int offset, final int length, final float y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] = (float)Math.pow(xs[i], y);
        }
        return xs;
    }

    public final static float[] square(final float[] xs)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] *= xs[i];
        }
        return xs;
    }

    public final static float[] square(final float[] xs, final int offset, final int length)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] *= xs[i];
        }
        return xs;
    }

    public final static float[] sqrt(final float[] xs)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] = (float)Math.sqrt(xs[i]);
        }
        return xs;
    }

    public final static float[] sqrt(final float[] xs, final int offset, final int length)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] = (float)Math.sqrt(xs[i]);
        }
        return xs;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static double min(final double[] xs)
    {
        double v = Double.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of {@code xs}
     */
    public final static double min(final double[] xs, final int offset, final int length)
    {
        double v = Double.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static double max(final double[] xs)
    {
        double v = -Double.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The maximum value of {@code xs}
     */
    public final static double max(final double[] xs, final int offset, final int length)
    {
        double v = -Double.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(xs[i], v);
        }
        return v;
    }

    /**
     * @return The minimum value of the absolute values of {@code xs}
     */
    public final static double amin(final double[] xs)
    {
        double v = Double.MAX_VALUE;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.min(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The minimum value of the absolute values of {@code xs}
     */
    public final static double amin(final double[] xs, final int offset, final int length)
    {
        double v = Double.MAX_VALUE;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.min(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The maximum value of the absolute values of {@code xs}
     */
    public final static double amax(final double[] xs)
    {
        double v = 0;
        for (int i = 0; i < xs.length; i++)
        {
            v = Math.max(Math.abs(xs[i]), v);
        }
        return v;
    }

    /**
     * @return The maximum value of the absolute values of {@code xs}
     */
    public final static double amax(final double[] xs, final int offset, final int length)
    {
        double v = 0;
        for (int i = offset; i < offset + length; i++)
        {
            v = Math.max(Math.abs(xs[i]), v);
        }
        return v;
    }

    public final static double[] add(final double[] xs, final double y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] += y;
        }
        return xs;
    }

    public final static double[] add(final double[] xs, final int offset, final int length, final double y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] += y;
        }
        return xs;
    }

    public final static double[] sub(final double[] xs, final double y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] -= y;
        }
        return xs;
    }

    public final static double[] sub(final double[] xs, final int offset, final int length, final double y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] -= y;
        }
        return xs;
    }

    public final static double[] mul(final double[] xs, final double y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] *= y;
        }
        return xs;
    }

    public final static double[] mul(final double[] xs, final int offset, final int length, final double y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] *= y;
        }
        return xs;
    }

    public final static double[] div(final double[] xs, final double y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] /= y;
        }
        return xs;
    }

    public final static double[] div(final double[] xs, final int offset, final int length, final double y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] /= y;
        }
        return xs;
    }

    public final static double[] pow(final double[] xs, final double y)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] = Math.pow(xs[i], y);
        }
        return xs;
    }

    public final static double[] pow(final double[] xs, final int offset, final int length, final double y)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] = Math.pow(xs[i], y);
        }
        return xs;
    }

    public final static double[] square(final double[] xs)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] *= xs[i];
        }
        return xs;
    }

    public final static double[] square(final double[] xs, final int offset, final int length)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] *= xs[i];
        }
        return xs;
    }

    public final static double[] sqrt(final double[] xs)
    {
        for (int i = 0; i < xs.length; i++)
        {
            xs[i] = Math.sqrt(xs[i]);
        }
        return xs;
    }

    public final static double[] sqrt(final double[] xs, final int offset, final int length)
    {
        for (int i = offset; i < offset + length; i++)
        {
            xs[i] = Math.sqrt(xs[i]);
        }
        return xs;
    }
}
