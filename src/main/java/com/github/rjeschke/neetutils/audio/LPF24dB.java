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
public class LPF24dB
{
    private final double  fs;
    private double q;
    private double qfu0;
    private double  f, bl0, bb0, r0, t0, tf0, u0, f0u1;
    private double  bl1, bb1, r1, t1, tf1, u1;
    private Clipper clipper = new DefaultClipper();

    public LPF24dB(final double fs)
    {
        this.fs = fs;
        this.setButterworthResponse();
        this.setCutoff(fs * 0.1);
    }

    public void setCutoff(final double cutoff)
    {
        this.f = Math.tan(Math.PI * cutoff / this.fs);
        this.recalc();
    }

    private void recalc()
    {
        this.t0 = 1.0 / (1.0 + this.r0 * this.f);
        this.tf0 = this.t0 * this.f;
        this.u0 = 1.0 / (1.0 + this.t0 * this.f * this.f);

        this.t1 = 1.0 / (1.0 + this.r1 * this.f);
        this.tf1 = this.t1 * this.f;
        this.u1 = 1.0 / (1.0 + this.t1 * this.f * this.f);

        this.f0u1 = this.f * this.u1;
        this.qfu0 = this.u0 / (1.0 + this.u0 * this.tf0 * this.f * this.tf1 * this.u1 * this.f * this.q);
    }

    public void setQ(final double q)
    {
        this.q = q;
        this.recalc();
    }

    public LPF24dB setButterworthResponse()
    {
        this.setRawQs(NMath.getButterworthFactor(4, 1), NMath.getButterworthFactor(4, 2));
        return this;
    }

    public LPF24dB setMoogLadderResponse()
    {
        this.setRawQs(2, 2);
        return this;
    }

    public void reset()
    {
        this.bl0 = this.bl1 = 0;
        this.bb0 = this.bb1 = 0;
    }

    public LPF24dB setRawQs(final double r0, final double r1)
    {
        this.r0 = r0;
        this.r1 = r1;
        this.recalc();
        return this;
    }

    public LPF24dB setClipper(final Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    public double process(final double input)
    {
        // Precalculate output
        final double out = this.qfu0
                * (this.bl0 + this.tf0 * (this.bb0 + this.f0u1 * (this.bl1 + this.tf1 * (this.bb1 + this.f * input))));
        final double qin = input - this.q * out;

        // Tick chained SVFs
        final double low1 = (this.bl1 + this.tf1 * (this.bb1 + this.f * qin)) * this.u1;
        final double band1 = (this.bb1 + this.f * (qin - low1)) * this.t1;
        final double high1 = qin - low1 - this.r1 * band1;

        final double low0 = (this.bl0 + this.tf0 * (this.bb0 + this.f * low1)) * this.u0;
        final double band0 = (this.bb0 + this.f * (low1 - low0)) * this.t0;
        final double high0 = low1 - low0 - this.r0 * band0;

        this.bb1 = this.clipper.clip(band1 + this.f * high1);
        this.bl1 = this.clipper.clip(low1 + this.f * band1);

        this.bb0 = this.clipper.clip(band0 + this.f * high0);
        this.bl0 = this.clipper.clip(low0 + this.f * band0);

        return out;
    }
}
