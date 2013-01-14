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

import com.github.rjeschke.neetutils.math.CatmullRomSpline;

public class TubeCompress
{
    private final CatmullRomSpline spline;
    private final double           bp, m1, tp0, tp1, rtw;

    public TubeCompress(double bp, double tw, double m1)
    {
        this.bp = bp;
        this.m1 = m1;
        final double tw2 = tw * 0.5;
        this.tp0 = bp - tw2;
        this.tp1 = bp + tw2;
        this.rtw = 1.0 / tw;
        final double v2 = 1 + m1 * (tw2);
        final double v3 = 1 + m1 * (tw + tw2);
        this.spline = new CatmullRomSpline(1, 1, v2, v3);
    }

    public double get(double in)
    {
        final double i = Math.abs(in);
        if (i < this.tp0) return 1;
        if (i < this.tp1) return this.spline.get((i - this.tp0) * this.rtw);
        return (i - this.bp) * this.m1 + 1;
    }

    public double process(double in)
    {
        return in / this.get(in);
    }
}
