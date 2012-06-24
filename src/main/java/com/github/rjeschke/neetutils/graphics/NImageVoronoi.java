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

import com.github.rjeschke.neetutils.concurrent.Worker;
import com.github.rjeschke.neetutils.math.NMath;
import com.github.rjeschke.neetutils.rng.RNG;
import com.github.rjeschke.neetutils.rng.RNGFactory;
import com.github.rjeschke.neetutils.rng.RNGType;

public class NImageVoronoi implements Worker<NImagePBlock>
{
    final NImage image;
    final float[][] points;
    final int pointCount;
    final float fallOff;
    final boolean invert;
    final boolean colorCells;
    final NColor color0;
    final NColor color1;

    NImageVoronoi(NImage image, final int seed, final int max, final float minDist, final float fallOff,
            final boolean invert, final boolean colorCells, final NColor color0, final NColor color1)
    {
        this.image = image;
        this.fallOff = fallOff;
        this.invert = invert;
        this.color0 = color0;
        this.color1 = color1;
        this.colorCells = colorCells;

        final RNG rnd = RNGFactory.create(RNGType.LCG, seed);
        final int todo = Math.min(max, (int)(1.0 / (minDist * minDist)));
        this.points = new float[todo][3];
        int count = 0;

        while(count < todo)
        {
            int i;
            float[] point = new float[3];
            for(i = 0; i < 500; i++)
            {
                point[0] = rnd.nextFloatUnipolar();
                point[1] = rnd.nextFloatUnipolar();
                boolean ok = true;
                for(int n = 0; n < count; n++)
                {
                    if(NImage.distOnTorus(point, this.points[n]) < minDist)
                    {
                        ok = false;
                        break;
                    }
                }
                if(ok)
                {
                    point[2] = rnd.nextFloatUnipolar();
                    this.points[count++] = point;
                    break;
                }
            }
            if(i >= 500)
                break;
        }

        this.pointCount = count;
    }

    @Override
    public void run(NImagePBlock p)
    {
        for(int y = 0; y < p.h; y++)
        {
            final float[] point = new float[2];
            point[1] = (float)(p.y + y) / (float)this.image.height;
            for(int x = 0; x < p.w; x++)
            {
                point[0] = (float)(p.x + x) / (float)this.image.width;
                float d0 = Float.MAX_VALUE, d1 = Float.MAX_VALUE;
                float c = 0;
                for(int i = 0; i < this.pointCount; i++)
                {
                    final float d = NImage.distOnTorus(point, this.points[i]);
                    if(d < d0)
                    {
                        d1 = d0;
                        d0 = d;
                        c = this.points[i][2];
                    }
                    else if(d < d1)
                    {
                        d1 = d;
                    }
                }
                float z = 1.f;
                if(this.fallOff > 0)
                {
                    z = NMath.saturate(d0 / d1);
                    z = (float)Math.pow(this.invert ? 1.0 - z : z, this.fallOff);
                }
                if(this.colorCells)
                    z *= c;
                p.pixels[x + y * p.w] = this.color0.lerp(this.color1, z);
            }
        }
    }
}
