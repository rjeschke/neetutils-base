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
package com.github.rjeschke.neetutils.graphics;

import java.util.Arrays;

import com.github.rjeschke.neetutils.concurrent.Worker;
import com.github.rjeschke.neetutils.math.NMath;
import com.github.rjeschke.neetutils.rng.RNG;
import com.github.rjeschke.neetutils.rng.RNGFactory;
import com.github.rjeschke.neetutils.rng.RNGType;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
class NImagePerlin implements Worker<NImagePBlock>
{
    final NImage image;
    final int[]  perm = new int[256];
    final float[] gradx = new float[256], grady = new float[256];
    final float   scalex;
    final float   scaley;
    final int     octaves;
    final float   fallOff;
    final float   amp;
    final NColor  color0;
    final NColor  color1;

    NImagePerlin(final NImage image, final int seed, final float scalex, final float scaley, final int octaves, final float fallOff, final float amp,
            final NColor color0, final NColor color1)
    {
        this.image = image;
        this.scalex = scalex;
        this.scaley = scaley;
        this.octaves = octaves;
        this.fallOff = fallOff;
        this.amp = amp;
        this.color0 = color0;
        this.color1 = color1;
        final RNG rnd = RNGFactory.create(RNGType.LCG, seed);

        Arrays.fill(this.perm, -1);
        for (int i = 0; i < 256; i++)
        {
            int p = rnd.nextInt() >>> 24;
            while (this.perm[p] != -1)
                p = rnd.nextInt() >>> 24;
            this.perm[p] = i;
            final float x = rnd.nextFloatUnipolar() * 2.f - 1.f;
            final float y = rnd.nextFloatUnipolar() * 2.f - 1.f;
            final float len = (float)Math.sqrt(x * x + y * y);
            this.gradx[i] = len > 0 ? x / len : x;
            this.grady[i] = len > 0 ? y / len : y;
        }
    }

    private static float s_curve(final float t)
    {
        return t * t * (3.f - 2.f * t);
    }

    @Override
    public void run(final NImagePBlock p)
    {
        for (int y = 0; y < p.h; y++)
        {
            final float fy = (float)(p.y + y) / (float)this.image.height;
            for (int x = 0; x < p.w; x++)
            {
                final float fx = (float)(p.x + x) / (float)this.image.width;

                float tx = fx * this.scalex;
                float ty = fy * this.scaley;
                float noise = 0, am = this.amp;

                for (int oct = 0; oct < this.octaves; oct++)
                {
                    float px0 = tx * 256.f;
                    final int ix = (int)px0;
                    px0 -= ix;
                    final float px1 = px0 - 1.f;

                    float py0 = ty * 256.f;
                    final int iy = (int)py0;
                    py0 -= iy;
                    final float py1 = py0 - 1.f;

                    final int p00 = this.perm[(ix + this.perm[iy & 255]) & 255];
                    final int p10 = this.perm[(ix + 1 + this.perm[iy & 255]) & 255];
                    final int p01 = this.perm[(ix + this.perm[(iy + 1) & 255]) & 255];
                    final int p11 = this.perm[(ix + 1 + this.perm[(iy + 1) & 255]) & 255];

                    final float fsx = s_curve(px0);
                    final float fsy = s_curve(py0);
                    float a, b, u, v;

                    u = px0 * this.gradx[p00] + py0 * this.grady[p00];
                    v = px1 * this.gradx[p10] + py0 * this.grady[p10];
                    a = NMath.lerp(u, v, fsx);

                    u = px0 * this.gradx[p01] + py1 * this.grady[p01];
                    v = px1 * this.gradx[p11] + py1 * this.grady[p11];
                    b = NMath.lerp(u, v, fsx);

                    noise += NMath.lerp(a, b, fsy) * am;
                    am *= this.fallOff;
                    tx *= 2.0;
                    ty *= 2.0;
                }
                p.pixels[x + y * p.w] = this.color0.lerp(this.color1, NMath.saturate(noise * 0.5f + 0.5f));
            }
        }
    }
}
