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

public class LPF6dBVar
{
    private final double fs;
    private double f, f2, b = 0, fb = 1;
    private Clipper clipper = new DefaultClipper();

    public LPF6dBVar(final double fs)
    {
        this.fs = fs;
        this.setCutoff(fs * 0.1);
    }

    public void reset()
    {
        this.b = 0;
    }
    
    public void setCutoff(final double freq)
    {
        this.f = Math.tan(Math.PI * freq / this.fs);
        this.f2 = 1.0 / (1 + this.f * this.fb);
    }

    public void setFeedback(double fb)
    {
        this.fb = fb;
        this.f2 = 1.0 / (1 + this.f * this.fb);
    }
    
    public double coef(double previous)
    {
        return previous * this.f * this.f2;
    }

    public double output(final double input)
    {
        return (this.b + this.f * input) * this.f2;
    }

    public double tick(final double input)
    {
        final double o = (this.b + this.f * input) * this.f2;
        this.b = this.clipper.clip(o + this.f * (input - this.fb * o));
        return o;
    }

    public void setClipper(Clipper clipper)
    {
        this.clipper = clipper;
    }
}
