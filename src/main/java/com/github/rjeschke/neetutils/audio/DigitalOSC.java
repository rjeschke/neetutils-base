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
    private Wave wave = Wave.NONE;
    private final double freqMul;
    private int phase;
    private int step;
    private float fstep;
    private float invFstep;
    private float rampFstep;
    private float fpw;
    private int pw;
    private int nextPw;
    private final static float SIN3 = -1.0f / 6.0f;
    private final static float SIN5 = 1.0f / 120.0f;
    private final static float SIN7 = -1.0f / 5040.0f;
    private final static float SIN9 = 1.0f / 362880.0f;
    private final static float F0PI5 = 1.570796327f;
    private final static float P1 = 1.f / 16777216.f;

    public DigitalOSC(final double fs)
    {
        this.freqMul = 440.0 * 16777216.0 / fs;

        this.pw = 0x800000;
        this.fpw = 0.5f;
        this.nextPw = 0xffffff;
    }

    public void setPw(final float v)
    {
        this.nextPw = (int)(NMath.clamp(v, 0.05f, 0.95f) * 16777216.f);
    }

    public void setPitch(final int cents)
    {
        final int c = NMath.clamp(cents, 0, 12799);
        this.step = (int)(this.freqMul * Math.pow(2.0, (c - 6900) / 1200.0));
        this.fstep = this.step / 16777216.f;
        this.invFstep = 1.f / this.fstep;
        this.rampFstep = (this.fstep * (this.fstep + P1) * 0.5f) * this.invFstep;
    }

    public void setWave(final Wave w)
    {
        this.wave = w;
    }

    public static float fastSin(final int p)
    {
        float x = (p & 0x3fffff) / 4194304.f;
        if((p & 0x400000) != 0)
            x = 1.0f - x;
        x *= F0PI5;
        final float x2 = x * x;
        final float asin = ((((SIN9 * x2 + SIN7) * x2 + SIN5) * x2 + SIN3) * x2 + 1.0f) * x;
        return p > 0x7fffff ? -asin : asin;
    }

    public float tick()
    {
        final int np = (this.phase + this.step) & 0xffffff;

        float out = 0;
        switch(this.wave)
        {
        case NONE:
            out = 0;
            break;
        case SINE:
            out = fastSin(this.phase);
            break;
        case SAWTOOTH:
        {
            float fp = this.phase / 16777216.f;
            if(this.phase > np)
            {
                float a = 1 - fp;
                out = a * (fp + (a + P1) * 0.5f);
                a = this.fstep - a;
                out = (out + a * (a + P1) * 0.5f) * this.invFstep;
            }
            else
            {
                out = fp + this.rampFstep;
            }
            out = 2.f * out - 1.f;
        }
            break;
        case PULSE:
        {
            float fp = this.phase / 16777216.f;
            if(this.phase > np)
            {
                if(this.phase < this.pw && np < this.pw)
                    out = (1 - this.fpw) * this.invFstep;
                else if(this.phase >= this.pw && np >= this.pw)
                    out = (this.fstep - this.fpw) * this.invFstep;
                else
                    out = (1 - fp) * this.invFstep;
            }
            else if(this.phase < this.pw && np >= this.pw)
            {
                out = (fp + this.fstep - this.fpw) * this.invFstep;
            }
            else if(fp >= this.fpw)
            {
                out = 1;
            }
            if(np > this.nextPw)
            {
                this.pw = this.nextPw;
                this.fpw = this.pw / 16777216.f;
                this.nextPw = 0xffffff;
            }
            out = 2.f * out - 1.f;
        }
            break;
        case TRIANGLE:
        {
            float fp = this.phase / 16777216.f;
            if(this.phase > np)
            {
                float a = 1 - fp;
                out = a * (a - (a + P1) * 0.5f);
                a = this.fstep - a;
                out = (out + (a * (a + P1) * 0.5f)) * this.invFstep;

            }
            else if(this.phase < 0x800000 && np >= 0x800000)
            {
                float a = 0.5f - fp;
                out = a * (fp + (a + P1) * 0.5f);
                a = 1 - this.fstep + a;
                out = (out + a * (0.5f - (a + P1) * 0.5f)) * this.invFstep;
            }
            else if(this.phase < 0x800000)
            {
                out = fp + this.rampFstep;
            }
            else
            {
                out = 1 - fp - this.rampFstep;
            }
            out = 4.f * out - 1.f;
        }
            break;
        }

        this.phase = np;

        return out;
    }

    public enum Wave
    {
        NONE, SINE, SAWTOOTH, PULSE, TRIANGLE
    }
}
