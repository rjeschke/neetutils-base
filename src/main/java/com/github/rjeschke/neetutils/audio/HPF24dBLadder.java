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

public class HPF24dBLadder
{
    private double       b0, b1, b2, b3;
    private double       q, f, f2, f3, fr;
    private final double fs;
    private Clipper      clipper = new DefaultClipper();

    public HPF24dBLadder(double fs)
    {
        this.fs = fs;
        this.setCutoff(fs * 0.1);
    }

    public void setCutoff(final double freq)
    {
        this.f = Math.tan(Math.PI * freq / this.fs);
        this.recalc();
    }

    private void recalc()
    {
        this.f2 = 1.0 / (1 + this.f);
        this.f3 = 1.0 - this.f2 * this.f;
        final double t = this.f3 * this.f3;
        this.fr = 1.0 / (1.0 + this.q * t * t);
    }

    public void setQ(double q)
    {
        this.q = q;
        this.recalc();
    }

    public void reset()
    {
        this.b0 = this.b1 = this.b2 = this.b3 = 0;
    }

    public HPF24dBLadder setClipper(Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    public double process(double input)
    {
        final double out = (this.f3 * (this.f3 * (this.f3 * (this.f3 * input - this.f2 * this.b0) - this.f2 * this.b1) - this.f2 * this.b2) - this.f2
                * this.b3)
                * this.fr;
        final double in = input - this.q * out;

        final double o0 = (this.b0 + this.f * in) * this.f2;
        this.b0 = this.clipper.clip(o0 + this.f * (in - o0));

        final double i1 = in - o0;
        final double o1 = (this.b1 + this.f * i1) * this.f2;
        this.b1 = o1 + this.f * (i1 - o1);

        final double i2 = i1 - o1;
        final double o2 = (this.b2 + this.f * i2) * this.f2;
        this.b2 = o2 + this.f * (i2 - o2);

        final double i3 = i2 - o2;
        final double o3 = (this.b3 + this.f * i3) * this.f2;
        this.b3 = o3 + this.f * (i3 - o3);

        return out;
    }
}
