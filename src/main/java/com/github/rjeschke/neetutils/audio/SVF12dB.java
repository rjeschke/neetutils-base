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

public class SVF12dB
{
    private double  fs, f, bl, bb;
    private double  r       = Math.sqrt(2), t, tf, u;
    private double  low, high, band;
    private Clipper clipper = new DefaultClipper();

    public SVF12dB(final double fs)
    {
        this.fs = fs;
        this.setCutoff(fs * 0.1);
    }

    public double low()
    {
        return this.low;
    }

    public double band()
    {
        return this.band;
    }

    public double high()
    {
        return this.high;
    }

    public double getF()
    {
        return this.f;
    }

    public void setCutoff(final double cutoff)
    {
        this.f = Math.tan(Math.PI * cutoff / this.fs);
        this.recalc();
    }

    private void recalc()
    {
        this.t = 1.0 / (1.0 + this.r * this.f);
        this.tf = this.t * this.f;
        this.u = 1.0 / (1.0 + this.t * this.f * this.f);
    }

    public void setRawQ(double r)
    {
        this.r = r;
        this.recalc();
    }

    public SVF12dB setClipper(Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    public SVF12dB process(double input)
    {
        this.low = (this.bl + this.tf * (this.bb + this.f * input)) * this.u;
        this.band = (this.bb + this.f * (input - this.low)) * this.t;
        this.high = input - this.low - this.r * this.band;

        this.bb = this.clipper.clip(this.band + this.f * this.high);
        this.bl = this.clipper.clip(this.low + this.f * this.band);

        return this;
    }

    public void reset()
    {
        this.bl = this.bb = 0;
    }
}
