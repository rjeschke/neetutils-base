package com.github.rjeschke.neetutils.graphics;

import com.github.rjeschke.neetutils.concurrent.Worker;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
class NImageBoxDownsampler implements Worker<NImagePBlock>
{
    final NImage image;
    final int    fx;
    final int    fy;

    NImageBoxDownsampler(final NImage image, final int fx, final int fy)
    {
        this.image = image;
        this.fx = fx;
        this.fy = fy;
    }

    @Override
    public void run(final NImagePBlock p)
    {
        final float div = this.fx * this.fy;
        for (int y = 0; y < p.h; y++)
        {
            for (int x = 0; x < p.w; x++)
            {
                final int rx = (x + p.x) * this.fx;
                final int ry = (y + p.y) * this.fy;

                float a = 0, r = 0, g = 0, b = 0;

                for (int y1 = 0; y1 < this.fy; y1++)
                {
                    for (int x1 = 0; x1 < this.fx; x1++)
                    {
                        final NColor c = this.image.getPixel(rx + x1, ry + y1);

                        a += c.a;
                        r += c.r;
                        g += c.g;
                        b += c.b;
                    }
                }

                p.pixels[x + y * p.w] = new NColor(a / div, r / div, g / div, b / div);
            }
        }
    }
}
