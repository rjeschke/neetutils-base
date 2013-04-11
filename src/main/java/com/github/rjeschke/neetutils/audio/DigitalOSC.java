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

import com.github.rjeschke.neetutils.math.NMath;

public class DigitalOSC
{
    private Wave                wave    = Wave.NONE;
    private final double        freqMul;
    private int                 phase;
    private int                 step;
    private double              fstep;
    private double              invFstep;
    private double              rampFstep;
    private double              fpw;
    private int                 pw;
    private int                 nextPw;
    private final static double SIN3    = -1.0 / 6.0;
    private final static double SIN5    = 1.0 / 120.0;
    private final static double SIN7    = -1.0 / 5040.0;
    private final static double SIN9    = 1.0 / 362880.0;
    private final static double F0PI5   = Math.PI / 2;
    private final static double F0PI5_2 = F0PI5 / 4194304.0;
    private final static double P1      = 1. / 16777216.0;

    public DigitalOSC(final double fs)
    {
        this.freqMul = 440.0 * 16777216.0 / fs;

        this.pw = 0x800000;
        this.fpw = 0.5;
        this.nextPw = 0xffffff;
    }

    public void setPw(final double v)
    {
        this.nextPw = (int)(NMath.clamp(v, 0.05, 0.95) * 16777216.0);
    }

    public void setPitch(final int cents)
    {
        final int c = NMath.clamp(cents, 0, 12799);
        this.step = (int)(this.freqMul * Math.pow(2.0, (c - 6900) / 1200.0));
        this.fstep = this.step / 16777216.0;
        this.invFstep = 1.0 / this.fstep;
        this.rampFstep = (this.fstep * (this.fstep + P1) * 0.50) * this.invFstep;
    }

    public void setWave(final Wave w)
    {
        this.wave = w;
    }

    public static int pitchFromFreq(double freq)
    {
        return (int)Math.floor(6900.5 + 1200.0 * Math.log(freq / 440) / Math.log(2));
    }

    public static double fastSin(final int p)
    {
        final double x = (1 - (((p & 0x800000) >> 22) & 2)) * ((p ^ (0x400000 - ((p >> 22) & 1))) & 0x3fffff) * F0PI5_2;
        final double x2 = x * x;
        return ((((SIN9 * x2 + SIN7) * x2 + SIN5) * x2 + SIN3) * x2 + 1.0) * x;
    }

    public double tick()
    {
        final int np = (this.phase + this.step) & 0xffffff;

        double out = 0;
        switch (this.wave)
        {
        case NONE:
            out = 0;
            break;
        case SINE:
            out = fastSin(this.phase);
            break;
        case SAWTOOTH:
        {
            double fp = this.phase / 16777216.0;
            if (this.phase > np)
            {
                double a = 1 - fp;
                out = a * (fp + (a + P1) * 0.5);
                a = this.fstep - a;
                out = (out + a * (a + P1) * 0.5) * this.invFstep;
            }
            else
            {
                out = fp + this.rampFstep;
            }
            out = 2.0 * out - 1.0;
            break;
        }
        case PULSE:
        {
            double fp = this.phase / 16777216.0;
            if (this.phase > np)
            {
                if (this.phase < this.pw && np < this.pw)
                    out = (1 - this.fpw) * this.invFstep;
                else if (this.phase >= this.pw && np >= this.pw)
                    out = (this.fstep - this.fpw) * this.invFstep;
                else out = (1 - fp) * this.invFstep;
            }
            else if (this.phase < this.pw && np >= this.pw)
            {
                out = (fp + this.fstep - this.fpw) * this.invFstep;
            }
            else if (fp >= this.fpw)
            {
                out = 1;
            }
            if (np > this.nextPw)
            {
                this.pw = this.nextPw;
                this.fpw = this.pw / 16777216.0;
                this.nextPw = 0xffffff;
            }
            out = 2.0 * out - 1.0;
            break;
        }
        case TRIANGLE:
        {
            double fp = this.phase / 16777216.0;
            if (this.phase > np)
            {
                double a = 1 - fp;
                out = a * (a - (a + P1) * 0.5);
                a = this.fstep - a;
                out = (out + (a * (a + P1) * 0.5)) * this.invFstep;

            }
            else if (this.phase < 0x800000 && np >= 0x800000)
            {
                double a = 0.5 - fp;
                out = a * (fp + (a + P1) * 0.5);
                a = 1 - this.fstep + a;
                out = (out + a * (0.5 - (a + P1) * 0.5)) * this.invFstep;
            }
            else if (this.phase < 0x800000)
            {
                out = fp + this.rampFstep;
            }
            else
            {
                out = 1 - fp - this.rampFstep;
            }
            out = 4.0 * out - 1.0;
            break;
        }
        }

        this.phase = np;

        return out;
    }

    public enum Wave
    {
        NONE, SINE, SAWTOOTH, PULSE, TRIANGLE
    }
}
