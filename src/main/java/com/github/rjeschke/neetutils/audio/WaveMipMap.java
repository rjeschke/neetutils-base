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

import java.util.Arrays;

import com.github.rjeschke.neetutils.math.CatmullRomSpline;
import com.github.rjeschke.neetutils.math.NMath;

public class WaveMipMap
{
    private final MipWave[] mipWaves;
    final int               maxHarmonics;
    final int               minHarmonics;
    private final int[]     mipHarmonics;
    final MipWave           first;
    final MipWave           last;

    public WaveMipMap(final double[] wave, final int minSize, final boolean normalize)
    {
        final int bits = (int)NMath.log2(wave.length);
        final int minBits = (int)NMath.log2(minSize);
        if ((1 << bits) != wave.length)
        {
            throw new IllegalArgumentException("Wave length must be a power of two");
        }
        if ((1 << minBits) != minSize)
        {
            throw new IllegalArgumentException("Minimum size must be a power of two");
        }
        if ((minBits > bits))
        {
            throw new IllegalArgumentException("Minimum size must be less than wave length");
        }

        final double[] fir = FIRUtils.windowKaiserFromAttenuation(FIRUtils.createLowpass(510, 9500, 40000), 90, 40000);

        this.mipWaves = new MipWave[bits - minBits + 1];
        this.mipWaves[0] = new MipWave(wave);

        if (normalize)
        {
            double max = 0;
            final MipWave w = this.mipWaves[0];
            for (int i = 0; i < w.size; i++)
            {
                max = Math.max(Math.abs(w.wave[i]), max);
            }
            if (max != 0)
            {
                for (int i = 0; i < w.size; i++)
                {
                    w.wave[i] /= max;
                }
            }
        }

        for (int i = 1; i < this.mipWaves.length; i++)
        {
            this.mipWaves[i] = this.mipWaves[i - 1].half(fir);
        }

        this.first = this.mipWaves[0];
        this.last = this.mipWaves[this.mipWaves.length - 1];

        this.maxHarmonics = this.first.harmonics;
        this.minHarmonics = this.last.harmonics;

        this.mipHarmonics = new int[this.maxHarmonics - this.minHarmonics];

        for (int i = this.minHarmonics; i < this.maxHarmonics; i++)
        {
            int m = 0;
            while (i <= this.mipWaves[m].harmonics && m < this.size() - 1)
            {
                m++;
            }
            this.mipHarmonics[i - this.minHarmonics] = m;
        }
    }

    public MipWave getMipMap(final int index)
    {
        return this.mipWaves[index];
    }

    public int size()
    {
        return this.mipWaves.length;
    }

    public int getMipMapIndex(final int harmonics)
    {
        if (harmonics >= this.maxHarmonics)
        {
            return 0;
        }
        if (harmonics <= this.minHarmonics)
        {
            return this.mipWaves.length - 1;
        }
        return this.mipHarmonics[harmonics - this.minHarmonics];
    }

    protected final static class MipWave
    {
        public final int      harmonics;
        public final int      size;
        public final int      mask;
        public final int      shift;
        public final int      shiftMask;
        public final double   frac;
        public final double[] wave;

        public MipWave(final double[] wave)
        {
            this(wave, wave.length / 2);
        }

        public MipWave(final double[] wave, final int harmonics)
        {
            this.size = wave.length;
            this.mask = this.size - 1;
            this.harmonics = harmonics;
            this.wave = Arrays.copyOf(wave, this.size);
            this.shift = 28 - (int)NMath.log2(this.size);
            this.shiftMask = (1 << this.shift) - 1;
            this.frac = 1.0 / (1 << this.shift);
        }

        public double get(int phase)
        {
            final int p = phase >> this.shift;
            final double f = (phase & this.shiftMask) * this.frac;

            return CatmullRomSpline.get(this.wave[(p - 1) & this.mask], this.wave[p & this.mask], this.wave[(p + 1) & this.mask],
                    this.wave[(p + 2) & this.mask], f);
        }

        public MipWave half(final double[] fir)
        {
            final int nl = this.size >> 1;
            final double[] out = new double[nl];

            for (int n = 0; n < nl; n++)
            {
                final int p = n * 2 - (fir.length / 2) + 1;
                double sum = 0;
                for (int m = 0; m < fir.length; m++)
                {
                    sum += fir[m] * this.wave[(p + m) & this.mask];
                }
                out[n] = sum;
            }

            return new MipWave(out, this.harmonics / 2);
        }
    }

}
