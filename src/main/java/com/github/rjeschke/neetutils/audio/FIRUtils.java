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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
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
    public final static double[] createLowpass(final int order, final double fc, double fs)
    {
        final double cutoff = fc / fs;
        final double[] fir = new double[order + 1];
        final double factor = 2.0 * cutoff;
        final int half = order >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] = factor * NMath.sinc(factor * (i - half));
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
    public final static double[] createHighpass(final int order, final double fc, double fs)
    {
        final double cutoff = fc / fs;
        final double[] fir = new double[order + 1];
        final double factor = 2.0 * cutoff;
        final int half = order >> 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] = (i == half ? 1.0 : 0.0) - factor * NMath.sinc(factor * (i - half));
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
    public final static double[] createBandstop(final int order, final double fcl, final double fch, final double fs)
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
    public final static double[] createBandpass(final int order, final double fcl, final double fch, final double fs)
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
    public final static double[] normalize(final double[] fir)
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
     * Creates a Kaiser window.
     * 
     * @param transitionWidth
     *            The transition width in Hz
     * @param attenuation
     *            Attenuation in dB
     * @param fs
     *            Sampling frequency
     * @return The window.
     */
    public final static double[] windowKaiser(double transitionWidth, double attenuation, double fs)
    {
        final double tw = 2.0 * Math.PI * transitionWidth / fs;
        int m;
        if(attenuation <= 21)
            m = (int)Math.ceil(5.79 / tw);
        else
            m = (int)Math.ceil((attenuation - 7.95) / (2.285 * tw));
        if((m & 1) == 0)
            m++;
        final double[] win = new double[m];

        final double beta;

        if(attenuation <= 21)
            beta = 0;
        else if(attenuation <= 50)
            beta = 0.5842 * Math.pow(attenuation - 21, 0.4) + 0.07886 * (attenuation - 21);
        else
            beta = 0.1102 * (attenuation - 8.7);

        final double i0b = NMath.i0(beta);

        for(int n = 0; n < m; n++)
        {
            final double v = beta * Math.sqrt(1.0 - Math.pow(2.0 * n / (m - 1) - 1.0, 2));
            win[n] = NMath.i0(v) / i0b;
        }

        return win;
    }

    /**
     * Applies a Bartlett window to the given FIR.
     * 
     * @param fir
     *            The FIR filter.
     * @return The windowed FIR filter.
     */
    public final static double[] windowBartlett(final double[] fir)
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
    public final static double[] windowSinc(final double[] fir)
    {
        final int m = fir.length - 1;
        for(int i = 0; i < fir.length; i++)
        {
            fir[i] *= NMath.sinc(2.0 * i / m - 1.0);
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
    public final static double[] windowHanning(final double[] fir)
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
    public final static double[] windowHamming(final double[] fir)
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
    public final static double[] windowBlackman(final double[] fir)
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
    public final static float[] toFloats(final double[] array)
    {
        final float[] ret = new float[array.length];
        for(int i = 0; i < ret.length; i++)
        {
            ret[i] = (float)array[i];
        }
        return ret;
    }

    private final static String[] PRES =
        { "1", "10", "100" };
    private final static String[] POSTS =
        { "", "k", "M", "G", "T", "P" };

    private final static String engVal(int val)
    {
        int v = 1;
        int z = 0;
        while(v < val)
        {
            z++;
            v *= 10;
        }

        final int pre = z % 3;
        final int post = z / 3;

        return PRES[pre] + POSTS[post];
    }

    /**
     * Renders the frequency response of the given FIR filter.
     * 
     * @param fir
     *            The FIR filter.
     * @return The response as an BufferedImage.
     */
    public final static BufferedImage freqResponse(double[] fir, final double fs)
    {
        final int[] dbs = new int[]
            { 12, 6, 0, -6, -12, -24, -48, -72, -96, -120 };
        final BufferedImage ret = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = ret.createGraphics();
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        FontRenderContext frc = g.getFontRenderContext();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1024, 512);
        g.setColor(Color.GRAY);

        for(int i = 0, n = 0; i < 500; i += 6)
        {
            final int db = 12 - i;
            final int dy = 511 - (db + 144) * 512 / 156;
            if(dy > 511)
                break;
            g.drawLine(0, dy, 1024, dy);

            if(n < dbs.length && dbs[n] == db)
            {
                g.setColor(Color.WHITE);
                final String value = Integer.toString(db);
                final Rectangle2D r = g.getFont().getStringBounds(value, frc);
                g.drawString(value, 2, dy - (float)r.getMinY());
                g.setColor(Color.GRAY);
                n++;
            }

        }

        final double logFs = Math.log10(fs * 0.5);
        {
            int v = 1;
            int st = v;
            int next = v * 10;
            boolean wasNext = false;
            for(;;)
            {
                final int x = (int)(1024.0 * Math.log10(v) / logFs);
                if(x >= 1024)
                    break;
                g.drawLine(x, 0, x, 512);

                if(wasNext)
                {
                    g.setColor(Color.WHITE);
                    final String value = engVal(next / 10);
                    final Rectangle2D r = g.getFont().getStringBounds(value, frc);
                    g.drawString(value, x - (float)r.getWidth() * 0.5f, 518 + (float)r.getMinY());
                    g.setColor(Color.GRAY);
                    wasNext = false;
                }

                v += st;
                if(v == next)
                {
                    next *= 10;
                    st *= 10;
                    wasNext = true;
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
            final double w = (Math.pow(10, logFs * x / 1024.0) / (fs * 0.5)) * Math.PI;
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
     * Multiplies two FIR filters.
     * 
     * @param inout
     *            In and output FIR filter.
     * @param in
     *            Second FIR filter.
     * @return inout.
     */
    public final static double[] multiply(final double[] inout, final double[] in)
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
    public final static double[] add(final double[] inout, final double[] in)
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
    public final static double[] scale(final double[] fir, final double f)
    {
        for(int i = 0; i < fir.length; i++)
            fir[i] *= f;
        return fir;
    }
}
