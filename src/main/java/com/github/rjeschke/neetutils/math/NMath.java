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

    public final static double INV_LOG_2 = 1.0 / Math.log(2);
    
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
        int t = Math.max((int)Math.floor(Math.log(value) * INV_LOG_2), 1);
        while(t < value)
            t <<= 1;
        return t;
    }

    public final static float step(float edge, float x)
    {
        return x < edge ? 0.0f : 1.0f;
    }

    public final static double step(double edge, double x)
    {
        return x < edge ? 0.0 : 1.0;
    }

    public final static float smoothstep(float edge0, float edge1, float x)
    {
        final float y = saturate((x - edge0) / (edge1 - edge0));
        return y * y * (3.0f - 2.0f * y);
    }

    public final static double smoothstep(double edge0, double edge1, double x)
    {
        final double y = saturate((x - edge0) / (edge1 - edge0));
        return y * y * (3.0 - 2.0 * y);
    }

    public final static float saturate(float x)
    {
        return Math.max(0, Math.min(1, x));
    }

    public final static double saturate(double x)
    {
        return Math.max(0, Math.min(1, x));
    }

    // TODO verify
    public final static float fract(float x)
    {
        return  x < 0 ? x - (float)Math.ceil(x) : x - (float)Math.floor(x);
    }

    // TODO verify
    public final static double fract(double x)
    {
        return  x < 0 ? x - Math.ceil(x) : x - Math.floor(x);
    }

    public final static float sinc(final float x)
    {
        if(x != 0)
        {
            final float xpi = (float)Math.PI * x;
            return (float)Math.sin(xpi) / xpi;
        }
        return 1.f;
    }

    public final static double sinc(final double x)
    {
        if(x != 0)
        {
            final double xpi = Math.PI * x;
            return Math.sin(xpi) / xpi;
        }
        return 1.0;
    }
}
