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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class SVF12dB2
{
    private final double fs;
    private double       b0, b1;
    private double       f, fB0, fB1, fC0;
    private double       a, b, r, ar;
    private double       A, B, C;
    private Clipper      clipper = new DefaultClipper();

    public SVF12dB2(final double fs)
    {
        this.fs = fs;
        this.setButterworthResponse();
    }

    public SVF12dB2 setCoefficients(final double a, final double b)
    {
        this.a = a;
        this.b = b;
        this.recalc();
        return this;
    }

    public SVF12dB2 setButterworthResponse()
    {
        return this.setCoefficients(Math.sqrt(2), 1);
    }

    public SVF12dB2 setLadderResponse()
    {
        return this.setCoefficients(2, 1);
    }

    public void setCutoff(final double freq)
    {
        this.f = Math.tan(Math.PI * freq / this.fs);
        this.recalc();
    }

    public void setQ(final double q)
    {
        this.r = q;
        this.recalc();
    }

    public void reset()
    {
        this.b0 = this.b1 = 0;
    }

    public SVF12dB2 setClipper(final Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    private void recalc()
    {
        final double rq = 1 + this.r;
        this.ar = this.a / rq;
        this.fB0 = 1 / (1 + this.f * this.ar);
        this.fB1 = this.f * this.fB0;

        this.fC0 = 1 / (1 + this.fB1 * this.f * this.b);
    }

    public double low()
    {
        return this.C;
    }

    public double band()
    {
        return this.B;
    }

    public double high()
    {
        return this.A;
    }

    public SVF12dB2 process(final double input)
    {
        this.C = (this.b1 + this.fB1 * (this.b0 + this.f * input)) * this.fC0;
        final double temp = input - this.b * this.C;
        this.B = (this.b0 + this.f * temp) * this.fB0;
        this.A = temp - this.ar * this.B;

        this.b1 = this.clipper.clip(this.C + this.f * this.B);
        this.b0 = this.clipper.clip(this.B + this.f * this.A);

        return this;
    }
}
