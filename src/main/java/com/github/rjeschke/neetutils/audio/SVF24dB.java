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

public class SVF24dB
{
    private final double fs;
    private double       b0, b1, b2, b3;
    private double       f, fB0, fB1, fC0, fC1, fD0, fD1, fE0;
    private double       a, b, c, d, r, dr;
    private double       A, B, C, D, E;
    private Clipper      clipper = new DefaultClipper();

    public SVF24dB(double fs)
    {
        this.fs = fs;
        this.setButterworthResponse();
    }

    public SVF24dB setCoefficients(double a, double b, double c, double d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.recalc();
        return this;
    }

    public SVF24dB setButterworthResponse()
    {
        final double a = NMath.getButterworthFactor(4, 1);
        final double b = NMath.getButterworthFactor(4, 2);
        return this.setCoefficients(a + b, 2 * a + b, a + b, 1);
    }

    public SVF24dB setLadderResponse()
    {
        return this.setCoefficients(4, 6, 4, 1);
    }

    public SVF24dB setTBResponse()
    {
        return this.setCoefficients(6.727171322029717, 14.142135623730951, 9.513656920021768, 1);
    }

    public SVF24dB setEMSResponse()
    {
        return this.setCoefficients(8.335135415365786, 20.207259421636902, 13.677042341728665, 1);
    }

    public SVF24dB setDResponse()
    {
        return this.setCoefficients(8.115667411135693, 19.595917942265423, 13.554030054147672, 1);
    }

    public void setCutoff(double freq)
    {
        this.f = Math.tan(Math.PI * freq / this.fs);
        this.recalc();
    }

    public void setQ(double q)
    {
        this.r = q;
        this.recalc();
    }

    public void reset()
    {
        this.b0 = this.b1 = this.b2 = this.b3 = 0;
    }

    public SVF24dB setClipper(Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    private void recalc()
    {
        this.dr = this.d + this.r;
        this.fB0 = 1 / (1 + this.f * this.a);
        this.fB1 = this.f * this.fB0;

        this.fC0 = 1 / (1 + this.fB1 * this.f * this.b);
        this.fC1 = this.f * this.fC0;

        this.fD0 = 1 / (1 + this.fC1 * this.fB1 * this.f * this.c);
        this.fD1 = this.f * this.fD0;

        this.fE0 = 1 / (1 + this.fD1 * this.fC1 * this.fB1 * this.f * this.dr);
    }

    public double low()
    {
        return this.E;
    }

    public double lowBand()
    {
        return this.D;
    }

    public double band()
    {
        return this.C;
    }

    public double highBand()
    {
        return this.B;
    }

    public double high()
    {
        return this.A;
    }

    public SVF24dB process(double input)
    {
        this.E = (this.b3 + this.fD1 * (this.b2 + this.fC1 * (this.b1 + this.fB1 * (this.b0 + this.f * input)))) * this.fE0;
        double temp = input - this.dr * this.E;
        this.D = (this.b2 + this.fC1 * (this.b1 + this.fB1 * (this.b0 + this.f * temp))) * this.fD0;
        temp -= this.c * this.D;
        this.C = (this.b1 + this.fB1 * (this.b0 + this.f * temp)) * this.fC0;
        temp -= this.b * this.C;
        this.B = (this.b0 + this.f * temp) * this.fB0;
        this.A = temp - this.a * this.B;

        this.b3 = this.clipper.clip(this.E + this.f * this.D);
        this.b2 = this.clipper.clip(this.D + this.f * this.C);
        this.b1 = this.clipper.clip(this.C + this.f * this.B);
        this.b0 = this.clipper.clip(this.B + this.f * this.A);

        return this;
    }
}
