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
public class SVF18dB
{
    private final double fs;
    private double       b0, b1, b2;
    private double       f, fB0, fB1, fC0, fC1, fD0;
    private double       a, b, c, r, ar, br;
    private double       A, B, C, D;
    private Clipper      clipper = new DefaultClipper();

    public SVF18dB(final double fs)
    {
        this.fs = fs;
        this.setButterworthResponse();
    }

    public SVF18dB setCoefficients(final double a, final double b, final double c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.recalc();
        return this;
    }

    public SVF18dB setButterworthResponse()
    {
        return this.setCoefficients(2, 2, 1);
    }

    public SVF18dB setLadderResponse()
    {
        return this.setCoefficients(3, 3, 1);
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
        this.b0 = this.b1 = this.b2 = 0;
    }

    public SVF18dB setClipper(final Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    private void recalc()
    {
        final double rq = 1 + this.r;
        this.ar = this.a / rq;
        this.br = this.b / rq;
        this.fB0 = 1 / (1 + this.f * this.ar);
        this.fB1 = this.f * this.fB0;

        this.fC0 = 1 / (1 + this.fB1 * this.f * this.br);
        this.fC1 = this.f * this.fC0;

        this.fD0 = 1 / (1 + this.fC1 * this.fB1 * this.f * this.c);
    }

    /**
     * @return 1
     */
    public double low()
    {
        return this.D;
    }

    /**
     * @return s
     */
    public double lowBand()
    {
        return this.C;
    }

    /**
     * @return s^2
     */
    public double highBand()
    {
        return this.B;
    }

    /**
     * @return s^3
     */
    public double high()
    {
        return this.A;
    }

    public SVF18dB process(final double input)
    {
        this.D = (this.b2 + this.fC1 * (this.b1 + this.fB1 * (this.b0 + this.f * input))) * this.fD0;
        double temp = input - this.c * this.D;
        this.C = (this.b1 + this.fB1 * (this.b0 + this.f * temp)) * this.fC0;
        temp -= this.br * this.C;
        this.B = (this.b0 + this.f * temp) * this.fB0;
        this.A = temp - this.ar * this.B;

        this.b2 = this.clipper.clip(this.D + this.f * this.C);
        this.b1 = this.clipper.clip(this.C + this.f * this.B);
        this.b0 = this.clipper.clip(this.B + this.f * this.A);

        return this;
    }
}
