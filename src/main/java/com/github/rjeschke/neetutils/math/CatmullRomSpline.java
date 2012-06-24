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
package com.github.rjeschke.neetutils.math;

public class CatmullRomSpline
{
    private double a, b, c, d;

    public CatmullRomSpline(final double v0, final double v1, final double v2, final double v3)
    {
        this.a = (2.0 * v1) * 0.5;
        this.b = (v2 - v0) * 0.5;
        this.c = (2.0 * v0 - 5.0 * v1 + 4.0 * v2 - v3) * 0.5;
        this.d = (3.0 * v1 - 3.0 * v2 + v3 - v0) * 0.5;
    }

    public static double get(final double v0, final double v1, final double v2, final double v3, final double f)
    {
        return ((((v3 - 3 * v2 + 3 * v1 - v0) * f + (2 * v0 - 5 * v1 + 4 * v2 - v3)) * f + (v2 - v0)) * f + (2 * v1)) * 0.5;
    }

    public static float get(final float v0, final float v1, final float v2, final float v3, final float f)
    {
        return ((((v3 - 3.f * v2 + 3.f * v1 - v0) * f + (2.f * v0 - 5.f * v1 + 4.f * v2 - v3)) * f + (v2 - v0)) * f + (2.f * v1)) * 0.5f;
    }

    public double get(final double f)
    {
        return ((this.d * f + this.c) * f + this.b) * f + this.a;
    }

    public void normalize()
    {
        double min = 1e9, max = -1e9;
        for(int i = 0; i <= 65536; i++)
        {
            final double v = this.get(i / 65536.0);
            min = Math.min(v, min);
            max = Math.max(v, max);
        }
        final double w = max - min;
        if(w > 0)
        {
            final double scale = 2.0 / w;

            this.a *= scale;
            this.b *= scale;
            this.c *= scale;
            this.d *= scale;
            this.a -= (max + min) * scale * 0.5;
        }
    }
}
