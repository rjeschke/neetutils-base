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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class MipMapOSC
{
    private final double       fs2;
    private int                maxPitch;
    private WaveMipMap         mipMap;
    private final double       pitchFactor;
    private int                phase;
    private int                step;
    private int                lastPitch = 3600;
    private WaveMipMap.MipWave wave0;
    private WaveMipMap.MipWave wave1;
    private double             waveCrossfade;
    private boolean            isCrossfade;
    private final int          noteMaximum;

    public MipMapOSC(final WaveMipMap mipMap, final double fs)
    {
        this(mipMap, fs, 128);
    }

    public MipMapOSC(final WaveMipMap mipMap, final double fs, final int noteMaximum)
    {
        this.pitchFactor = (1 << 28) / fs;
        this.fs2 = fs * 0.5;
        this.noteMaximum = NMath.clamp(noteMaximum, 64, 128);
        this.setMipMap(mipMap);
    }

    public void setMipMap(final WaveMipMap mipMap)
    {
        this.maxPitch = Math.min(this.noteMaximum, DigitalOSC.pitchFromFreq((this.fs2 / mipMap.minHarmonics)) / 100) * 100 - 1;
        System.out.println(this.maxPitch);
        this.mipMap = mipMap;
        this.setPitch(this.lastPitch);
    }

    public void setPitch(final int cents)
    {
        final double freq = 440.0 * NMath.exp2(((this.lastPitch = NMath.clamp(cents, 0, this.maxPitch)) - 6900) / 1200.0);
        final int harmonics = (int)Math.ceil(this.fs2 / freq);
        final int mi = this.mipMap.getMipMapIndex(harmonics);
        this.wave0 = this.mipMap.getMipMap(mi);
        if (mi + 1 < this.mipMap.size() && harmonics < this.mipMap.first.harmonics)
        {
            this.isCrossfade = true;
            this.wave1 = this.mipMap.getMipMap(mi + 1);
            this.waveCrossfade = 1.0 - Math.pow(((double)harmonics / (double)this.wave0.harmonics - 1.0), 1.0 / 8.0);
        }
        else
        {
            this.isCrossfade = false;
        }
        this.step = (int)(this.pitchFactor * freq);
    }

    public void setPhase(final int phase)
    {
        this.phase = phase & 0xfffffff;
    }

    public double tick()
    {
        final double out;
        if (this.isCrossfade)
        {
            final double s0 = this.wave0.get(this.phase);
            out = this.waveCrossfade * (this.wave1.get(this.phase) - s0) + s0;
        }
        else
        {
            out = this.wave0.get(this.phase);
        }
        this.phase = (this.phase + this.step) & 0xfffffff;
        return out;
    }
}
