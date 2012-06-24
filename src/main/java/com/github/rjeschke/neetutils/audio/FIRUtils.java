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
package com.github.rjeschke.neetutils.audio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.github.rjeschke.neetutils.math.NMath;

/**
 * FIR filter utility class.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class FIRUtils
{
    /**
     * Ctor.
     */
    private FIRUtils()
    {
        // Prevent instantiation
    }

    /**
     * The sinc function.
     * 
     * @param x
     *            x.
     * @return sinc of x.
     */
    public static double sinc(final double x)
    {
        if(x != 0)
        {
            final double xpi = Math.PI * x;
            return Math.sin(xpi) / xpi;
        }
        return 1.0;
    }

    /**
     * Creates a lowpass filter.
     * 
     * @param order
     *            Filter order.
     * @param fc
     *            Filter cutoff.
     * @param fs
     *            Filter sampling rate.
     * @return The FIR filter.
     */
    public static double[] createLowpass(final int order, final double fc, double fs)
    {
        final double cutoff = fc / fs;
        final double[] fir = new double[order + 1];
        final double factor = 2.0 * cutoff;
        final int half = order >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] = factor * sinc(factor * (i - half));
        }
        return fir;
    }

    /**
     * Creates a highpass filter.
     * 
     * @param order
     *            Filter order.
     * @param fc
     *            Filter cutoff.
     * @param fs
     *            Filter sampling rate.
     * @return The FIR filter.
     */
    public static double[] createHighpass(final int order, final double fc, double fs)
    {
        final double cutoff = fc / fs;
        final double[] fir = new double[order + 1];
        final double factor = 2.0 * cutoff;
        final int half = order >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] = (i == half ? 1.0 : 0.0) - factor * sinc(factor * (i - half));
        }
        return fir;
    }

    /**
     * Creates a bandstop filter.
     * 
     * @param order
     *            Filter order.
     * @param fcl
     *            Lower filter cutoff.
     * @param fch
     *            Higher filter cutoff.
     * @param fs
     *            Filter sampling rate.
     * @return The FIR filter.
     */
    public static double[] createBandstop(final int order, final double fcl, final double fch, final double fs)
    {
        final double[] low = createLowpass(order, fcl, fs);
        final double[] high = createHighpass(order, fch, fs);
        for(int i = 0; i < low.length; i++)
        {
            low[i] += high[i];
        }
        return low;
    }

    /**
     * Creates a bandpass filter.
     * 
     * @param order
     *            Filter order.
     * @param fcl
     *            Lower filter cutoff.
     * @param fch
     *            Higher filter cutoff.
     * @param fs
     *            Filter sampling rate.
     * @return The FIR filter.
     */
    public static double[] createBandpass(final int order, final double fcl, final double fch, final double fs)
    {
        final double[] fir = createBandstop(order, fcl, fch, fs);
        final int half = order >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] = (i == half ? 1.0 : 0.0) - fir[i];
        }
        return fir;
    }

    /**
     * Normalizes the sum of the given FIR filter to 1.
     * 
     * @param fir
     *            The FIR filter.
     * @return Normalized FIR filter.
     */
    public static double[] normalize(final double[] fir)
    {
        double sum = 0;
        for(int i = 0; i < fir.length; i++)
        {
            sum += fir[i];
        }
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] /= sum;
        }
        return fir;
    }

    /**
     * Applies a Bartlett window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public static double[] windowBartlett(final double[] fir)
    {
        final int m = fir.length - 1;
        final int m2 = m >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= 1.0 - 2.0 * (i - m2) / m;
        }
        return fir;
    }

    /**
     * Applies a sinc window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public static double[] windowSinc(final double[] fir)
    {
        final int m = fir.length - 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= sinc(2.0 * i / m - 1.0);
        }
        return fir;
    }

    /**
     * Applies a Hanning window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public static double[] windowHanning(final double[] fir)
    {
        final int m = fir.length - 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= 0.5 - 0.5 * Math.cos(2.0 * Math.PI * i / m);
        }
        return fir;
    }

    /**
     * Applies a Hamming window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public static double[] windowHamming(final double[] fir)
    {
        final int m = fir.length - 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= 0.54 - 0.46 * Math.cos(2.0 * Math.PI * i / m);
        }
        return fir;
    }

    /**
     * Applies a Blackman window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public static double[] windowBlackman(final double[] fir)
    {
        final int m = fir.length - 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= 0.42 - 0.5 * Math.cos(2.0 * Math.PI * i / m) + 0.08 * Math.cos(4.0 * Math.PI * i / m);
        }
        return fir;
    }

    /**
     * Converts a double array to a float array.
     * 
     * @param array
     *            The double array.
     * @return The float array.
     */
    public static float[] toFloats(final double[] array)
    {
        final float[] ret = new float[array.length];
        for(int i = 0; i < ret.length; i++)
        {
            ret[i] = (float)array[i];
        }
        return ret;
    }

    /**
     * Renders the frequency response of the given FIR filter.
     * 
     * @param fir
     *            The FIR filter.
     * @return The response as an BufferedImage.
     */
    public static BufferedImage freqResponse(double[] fir, final double fs, boolean log)
    {
        final double[] dbs = new double[]
            { 12, 6, 0, -6, -12, -24, -48, -96 };
        final BufferedImage ret = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_RGB);
        final Graphics g = ret.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1024, 512);
        g.setColor(Color.GRAY);
        for(int i = 0; i < dbs.length; i++)
        {
            int dy = 511 - (int)((dbs[i] + 144) * 512 / 156);
            g.drawLine(0, dy, 1024, dy);
        }
        final double logFs = Math.log10(fs * 0.5);
        {
            int v = log ? 1 : 100;
            int st = v;
            int next = v * 10;
            for(;;)
            {
                int x = log ? (int)(1024.0 * Math.log10(v) / logFs) : (int)(v * 2048.0 / fs);
                if(x >= 1024)
                    break;
                g.drawLine(x, 0, x, 512);
                v += st;
                if(v == next)
                {
                    next *= 10;
                    st *= 10;
                }
            }
        }

        double max = 0;
        for(int i = 0; i < fir.length; i++)
            max = Math.max(Math.abs(fir[i]), max);
        max = 255 / max;

        int oldy2 = 0;
        int oldy = 0;
        for(int x = 0; x < 1024; x++)
        {
            final double w = (log ? Math.pow(10, logFs * x / 1024.0) / (fs * 0.5) : x / 1024.0) * Math.PI;
            double re = 0, im = 0;
            for(int i = 0; i < fir.length; i++)
            {
                re += fir[i] * Math.cos(i * w);
                im -= fir[i] * Math.sin(i * w);
            }
            double v = Math.sqrt(re * re + im * im);
            v = v != 0 ? Math.max(Math.log10(v) * 20.0, -144) : -144;
            final int y = 511 - (int)((v + 144) * 512 / 156);

            double pp = (double)fir.length * (double)x / 1024.0;
            final int pi = (int)pp;
            pp -= pi;

            final double fv = fir[NMath.clamp(pi, 0, fir.length - 1)] + pp
                    * (fir[NMath.clamp(pi + 1, 0, fir.length - 1)] - fir[NMath.clamp(pi, 0, fir.length - 1)]);
            final int y2 = 256 - (int)(fv * max);

            if(x > 0)
            {
                g.setColor(Color.BLUE);
                g.drawLine(x - 1, oldy2, x, y2);
                g.setColor(Color.GREEN);
                g.drawLine(x - 1, oldy, x, y);
            }
            oldy = y;
            oldy2 = y2;
        }
        return ret;
    }

    /**
     * Convolves two FIR filters (multiply).
     * 
     * @param inout
     *            In and output FIR filter.
     * @param in
     *            Second FIR filter.
     * @return inout.
     */
    public static double[] convolve(final double[] inout, final double[] in)
    {
        if(inout.length != in.length)
            throw new RuntimeException("Filter lengths do not match!");
        for(int i = 0; i < inout.length; i++)
            inout[i] *= in[i];
        return inout;
    }

    /**
     * Adds two FIR filters.
     * 
     * @param inout
     *            In and output FIR filter.
     * @param in
     *            Second FIR filter.
     * @return inout.
     */
    public static double[] add(final double[] inout, final double[] in)
    {
        if(inout.length != in.length)
            throw new RuntimeException("Filter lengths do not match!");
        for(int i = 0; i < inout.length; i++)
            inout[i] += in[i];
        return inout;
    }

    /**
     * Scales a FIR filter.
     * 
     * @param fir
     *            FIR filter.
     * @param f
     *            Scaling factor.
     * @return FIR filter.
     */
    public static double[] scale(final double[] fir, final double f)
    {
        for(int i = 0; i < fir.length; i++)
            fir[i] *= f;
        return fir;
    }
}
