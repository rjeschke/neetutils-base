package com.github.rjeschke.neetutils.graphics;

import com.github.rjeschke.neetutils.concurrent.Worker;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
class NImageFilter implements Worker<NImagePBlock>
{
    final NImage       image;
    final FilterKernel filter;
    final int          mode;

    NImageFilter(final NImage image, final FilterKernel filter, final int mode)
    {
        this.image = image;
        this.filter = filter;
        this.mode = mode;
    }

    @Override
    public void run(final NImagePBlock p)
    {
        if (this.mode == 0)
        {
            for (int y = 0; y < p.h; y++)
            {
                for (int x = 0; x < p.w; x++)
                {
                    float a = 0, r = 0, g = 0, b = 0;

                    for (int fy = 0; fy < this.filter.height; fy++)
                    {
                        for (int fx = 0; fx < this.filter.width; fx++)
                        {
                            final float f = this.filter.xyf[fx + fy * this.filter.width];
                            final int rx = p.x + x + fx + this.filter.offsx;
                            final int ry = p.y + y + fy + this.filter.offsy;

                            final NColor c = this.image.getPixel(rx, ry);

                            a += c.a * f;
                            r += c.r * f;
                            g += c.g * f;
                            b += c.b * f;
                        }
                    }

                    p.pixels[x + y * p.w] = new NColor(a, r, g, b);
                }
            }
        }
        else if (this.mode == 1)
        {
            for (int y = 0; y < p.h; y++)
            {
                for (int x = 0; x < p.w; x++)
                {
                    float a = 0, r = 0, g = 0, b = 0;

                    for (int fx = 0; fx < this.filter.width; fx++)
                    {
                        final float f = this.filter.xf[fx];

                        final int rx = p.x + x + fx + this.filter.offsx;
                        final int ry = p.y + y;

                        final NColor c = this.image.getPixel(rx, ry);

                        a += c.a * f;
                        r += c.r * f;
                        g += c.g * f;
                        b += c.b * f;
                    }

                    p.pixels[x + y * p.w] = new NColor(a, r, g, b);
                }
            }
        }
        else
        {
            for (int y = 0; y < p.h; y++)
            {
                for (int x = 0; x < p.w; x++)
                {
                    float a = 0, r = 0, g = 0, b = 0;

                    for (int fy = 0; fy < this.filter.height; fy++)
                    {
                        final float f = this.filter.yf[fy];
                        final int rx = p.x + x;
                        final int ry = p.y + y + fy + this.filter.offsy;

                        final NColor c = this.image.getPixel(rx, ry);

                        a += c.a * f;
                        r += c.r * f;
                        g += c.g * f;
                        b += c.b * f;
                    }

                    p.pixels[x + y * p.w] = new NColor(a, r, g, b);
                }
            }
        }
    }
}
