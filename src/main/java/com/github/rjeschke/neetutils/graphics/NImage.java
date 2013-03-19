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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.List;

import com.github.rjeschke.neetutils.collections.Colls;
import com.github.rjeschke.neetutils.concurrent.Worker;
import com.github.rjeschke.neetutils.concurrent.WorkerCallback;
import com.github.rjeschke.neetutils.concurrent.WorkerPool;
import com.github.rjeschke.neetutils.concurrent.WorkerStatus;
import com.github.rjeschke.neetutils.math.NMath;
import com.github.rjeschke.neetutils.rng.RNG;
import com.github.rjeschke.neetutils.rng.RNGFactory;
import com.github.rjeschke.neetutils.rng.RNGType;

public class NImage implements WorkerCallback<NImagePBlock>
{
    public final int            width;
    public final int            height;
    final NColor[]              pixels;
    private ClampMode           clampX            = ClampMode.ClAMP_TO_EDGE;
    private ClampMode           clampY            = ClampMode.ClAMP_TO_EDGE;
    private ColorOp             cop               = ColorOp.SET;
    private int                 processingThreads = 1;
    final static float[]        SOBEL_X           =
                                                  { 1, 0, -1, 2, 0, -2, 1, 0, -1 };
    final static float[]        SOBEL_Y           =
                                                  { 1, 2, 1, 0, 0, 0, -1, -2, -1 };

    private final static double TO_sRGB           = 1.0 / 2.2;
    private final static double FROM_sRGB         = 2.2;

    public NImage(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.pixels = new NColor[width * height];
        Arrays.fill(this.pixels, NColor.BLACK_TRANS);
    }

    public NImage(NImage image)
    {
        this.width = image.width;
        this.height = image.height;
        this.pixels = new NColor[this.width * this.height];
        System.arraycopy(image.pixels, 0, this.pixels, 0, this.pixels.length);
    }

    public NImage(BufferedImage image)
    {
        this(image, FROM_sRGB);
    }

    public NImage(BufferedImage image, double gamma)
    {
        final BufferedImage img = forceARGB(image);
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new NColor[this.width * this.height];

        final int[] pix = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        if (gamma != 1)
        {
            for (int i = 0; i < pix.length; i++)
                this.pixels[i] = new NColor(pix[i], gamma);
        }
        else
        {
            for (int i = 0; i < pix.length; i++)
                this.pixels[i] = new NColor(pix[i]);
        }
    }

    private static BufferedImage forceARGB(BufferedImage in)
    {
        if (in.getType() == BufferedImage.TYPE_INT_ARGB) return in;
        final BufferedImage img = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        return img;
    }

    private final static int clampXY(ClampMode c, int v, int max)
    {
        int temp;

        switch (c)
        {
        default:
        case ClAMP_TO_EDGE:
            return NMath.clamp(v, 0, max - 1);
        case WRAP:
            temp = v % max;
            return temp < 0 ? temp + max : temp;
        }
    }

    private int clampX(int x)
    {
        return clampXY(this.clampX, x, this.width);
    }

    private int clampY(int y)
    {
        return clampXY(this.clampY, y, this.height);
    }

    private int clampedPos(int x, int y)
    {
        return this.clampX(x) + this.clampY(y) * this.width;
    }

    public NImage setThreadCount(int threads)
    {
        this.processingThreads = Math.max(1, threads);
        return this;
    }

    public void setPixel(int x, int y, NColor c)
    {
        final int p = this.clampedPos(x, y);

        switch (this.cop)
        {
        default:
        case SET:
            this.pixels[p] = c;
            break;
        case ADD:
            this.pixels[p] = this.pixels[p].add(c);
            break;
        case SUB:
            this.pixels[p] = this.pixels[p].sub(c);
            break;
        case MUL:
            this.pixels[p] = this.pixels[p].mul(c);
            break;
        case ADD_RGB:
            this.pixels[p] = this.pixels[p].addRGB(c);
            break;
        case SUB_RGB:
            this.pixels[p] = this.pixels[p].subRGB(c);
            break;
        case MUL_RGB:
            this.pixels[p] = this.pixels[p].mulRGB(c);
            break;
        case BLEND:
            this.pixels[p] = this.pixels[p].blendOver(c);
            break;
        }
    }

    public NImage forceFill(NColor c)
    {
        Arrays.fill(this.pixels, c);
        return this;
    }

    public NColor getPixel(int x, int y)
    {
        return this.pixels[this.clampedPos(x, y)];
    }

    public void setClampMode(ClampMode clampX, ClampMode clampY)
    {
        this.clampX = clampX;
        this.clampY = clampY;
    }

    public void setColorOp(ColorOp op)
    {
        this.cop = op;
    }

    public NImage normalizeColors()
    {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            min = Math.min(min, c.a);
            min = Math.min(min, c.r);
            min = Math.min(min, c.g);
            min = Math.min(min, c.b);
            max = Math.max(max, c.a);
            max = Math.max(max, c.r);
            max = Math.max(max, c.g);
            max = Math.max(max, c.b);
        }
        float d = max - min;
        if (d == 0)
            d = 1;
        else d = 1.f / d;
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            this.pixels[i] = new NColor((c.a - min) * d, (c.r - min) * d, (c.g - min) * d, (c.b - min) * d);
        }
        return this;
    }

    public NImage toGrayscale()
    {
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            final float l = c.luminance();
            this.pixels[i] = new NColor(c.a, l, l, l);
        }
        return this;
    }

    public BufferedImage toBufferedImageARGB()
    {
        return this.toBufferedImageARGB(TO_sRGB);
    }

    public BufferedImage toBufferedImageARGB(double gamma)
    {
        final BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        final int[] pix = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        if (gamma != 1)
        {
            for (int i = 0; i < pix.length; i++)
                pix[i] = this.pixels[i].toARGB(gamma);
        }
        else
        {
            for (int i = 0; i < pix.length; i++)
                pix[i] = this.pixels[i].toARGB();
        }
        return img;
    }

    public BufferedImage toBufferedImageARGBDithered()
    {
        return toBufferedImageARGBDithered(TO_sRGB);
    }

    public BufferedImage toBufferedImageARGBDithered(double gamma)
    {
        final BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        final int[] pix = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

        float[] err0 = new float[3 * this.width + 6];
        float[] err1 = new float[3 * this.width + 6];

        final float e0 = 7.f / 16.f;
        final float e1 = 3.f / 16.f;
        final float e2 = 5.f / 16.f;
        final float e3 = 1.f / 16.f;

        for (int y = 0; y < this.height; y++)
        {
            System.arraycopy(err1, 0, err0, 0, err0.length);
            Arrays.fill(err1, 0);
            for (int x = 0; x < this.width; x++)
            {
                final int p = x * 3 + 3;
                NColor co = this.pixels[x + y * this.width].saturate();
                if (gamma != 1) co = co.powRGB(gamma);

                final int a = NMath.clamp((int)(co.a * 255.f), 0, 255);
                final int r = NMath.clamp((int)((co.r + err0[p]) * 255.f), 0, 255);
                final int g = NMath.clamp((int)((co.g + err0[p + 1]) * 255.f), 0, 255);
                final int b = NMath.clamp((int)((co.b + err0[p + 2]) * 255.f), 0, 255);

                final float er = co.r - r / 255.f;
                final float eg = co.g - g / 255.f;
                final float eb = co.b - b / 255.f;

                err0[p + 3 + 0] += er * e0;
                err0[p + 3 + 1] += eg * e0;
                err0[p + 3 + 2] += eb * e0;

                err1[p - 3 + 0] += er * e1;
                err1[p - 3 + 1] += eg * e1;
                err1[p - 3 + 2] += eb * e1;

                err1[p + 0] += er * e2;
                err1[p + 1] += eg * e2;
                err1[p + 2] += eb * e2;

                err1[p + 3 + 0] += er * e3;
                err1[p + 3 + 1] += eg * e3;
                err1[p + 3 + 2] += eb * e3;

                pix[x + y * this.width] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }

        return img;
    }

    private static void vecNormalize(float[] v)
    {
        final float len = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        if (len > 0)
        {
            v[0] /= len;
            v[1] /= len;
            v[2] /= len;
        }
    }

    public NImage bump(final NImage normals, final float[] light, final NColor ambient, final NColor diffuse,
            final NColor specular, final float power, final float heightScale, final boolean directional)
    {
        float[] l = new float[3], n = new float[3], h = new float[3];
        NColor color, out, norm;

        if (directional)
        {
            l[0] = -light[0];
            l[1] = -light[1];
            l[2] = -light[2];
            vecNormalize(l);
        }
        for (int y = 0; y < this.height; y++)
        {
            final float fz = (float)y / (float)this.height;
            for (int x = 0; x < this.width; x++)
            {
                final float fx = (float)x / (float)this.width;
                color = this.getPixel(x, y);
                norm = normals.getPixel(x, y);
                n[0] = norm.r * 2.f - 1.f;
                n[1] = norm.b * 2.f - 1.f;
                n[2] = norm.g * 2.f - 1.f;
                vecNormalize(n);
                final float fy = norm.a * heightScale;
                if (!directional)
                {
                    l[0] = light[0] - fx;
                    l[1] = light[1] - fy;
                    l[2] = light[2] - fz;
                    vecNormalize(l);
                }

                out = color.mulRGB(ambient);

                float d = Math.max(0.f, n[0] * l[0] + n[1] * l[1] + n[2] * l[2]);
                if (d > 0)
                {
                    out = out.addRGB(diffuse.mulRGB(color), d);
                    if (power > 0)
                    {
                        h[0] = l[0];
                        h[1] = l[1] + 1.f;
                        h[2] = l[2];
                        vecNormalize(h);
                        d = (float)Math.pow(Math.max(0.f, n[0] * h[0] + n[1] * h[1] + n[2] * h[2]), power);
                        out = out.addRGB(specular, d);
                    }
                }
                this.pixels[x + y * this.width] = out;
            }
        }
        return this;
    }

    public NImage normals(float scale)
    {
        final NColor[] pix = new NColor[this.width * this.height];
        final float sx = scale * this.width / 512.0f;
        final float sy = scale * this.height / 512.0f;

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                float dx = 0, dy = 0;
                for (int n = 0; n < 3; n++)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        final float l = this.getPixel(x + i - 1, y + n - 1).luminance();
                        dx += l * SOBEL_X[i + n * 3];
                        dy += l * SOBEL_Y[i + n * 3];
                    }
                }
                dx *= sx;
                dy *= sy;
                final float len = (float)Math.sqrt(dx * dx + dy * dy + 1.f);
                final float dz = 1.f / len;
                dx /= len;
                dy /= len;
                final NColor c = this.getPixel(x, y);
                pix[x + y * this.width] = new NColor(NMath.saturate(dx * 0.5f + 0.5f), NMath.saturate(dy * 0.5f + 0.5f),
                        NMath.saturate(dz * 0.5f + 0.5f), c.luminance());
            }
        }

        System.arraycopy(pix, 0, this.pixels, 0, pix.length);
        return this;
    }

    public NImage perlin(final int seed, final float scalex, final float scaley, final int octaves, final float fallOff,
            final float amp, final NColor color0, final NColor color1)
    {
        return this.runThreaded(new NImagePerlin(this, seed, scalex, scaley, octaves, fallOff, amp, color0, color1), 8);
    }

    public final static float distOnTorus(final float[] a, final float[] b)
    {
        final float dx = Math.min(Math.abs(a[0] - b[0] + 1.f) % 1.f, Math.abs(a[0] - b[0] - 1.f) % 1.f);
        final float dy = Math.min(Math.abs(a[1] - b[1] + 1.f) % 1.f, Math.abs(a[1] - b[1] - 1.f) % 1.f);
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    public synchronized NImage runThreaded(final Worker<NImagePBlock> worker, final int blockSize)
    {
        if (this.processingThreads < 2)
        {
            final NImagePBlock block = new NImagePBlock(0, 0, this.width, this.height);
            worker.run(block);
            System.arraycopy(block.pixels, 0, this.pixels, 0, this.pixels.length);
        }
        else
        {
            final WorkerPool<NImagePBlock> pool = WorkerPool.start(this, this.processingThreads, 512, true);
            final int wx = (this.width + blockSize - 1) / blockSize;
            final int wy = (this.height + blockSize - 1) / blockSize;
            for (int y = 0; y < wy; y++)
            {
                final int ry = y * blockSize;
                final int rh = Math.min(this.height - ry, blockSize);
                for (int x = 0; x < wx; x++)
                {
                    final int rx = x * blockSize;
                    final int rw = Math.min(this.width - rx, blockSize);
                    pool.enqueue(worker, new NImagePBlock(rx, ry, rw, rh));
                }
            }
            pool.stop();
        }

        return this;
    }

    public NImage filter(FilterKernel kernel)
    {
        if (kernel.isSingle) return this.runThreaded(new NImageFilter(new NImage(this), kernel, 0), 8);

        this.runThreaded(new NImageFilter(this, kernel, 1), 8);
        return this.runThreaded(new NImageFilter(this, kernel, 2), 8);
    }

    public NImage voronoi(final int seed, final int max, final float minDist, final float fallOff, final boolean invert,
            final boolean colorCells, final NColor color0, final NColor color1)
    {
        return this.runThreaded(new NImageVoronoi(this, seed, max, minDist, fallOff, invert, colorCells, color0, color1), 8);
    }

    public NImage bricks(final int seed, final int bricksx, final int bricksy, final float jointsx, final float jointsy,
            final float singleProb, final float rowOffset, final float jointHardness, final NColor color0, final NColor color1,
            final NColor colorJoints)
    {
        final RNG rnd = RNGFactory.create(RNGType.LCG, seed);
        NColor col;
        float[][] points = new float[bricksx + 1][2];
        int lastRow = -1, count = 0;

        for (int y = 0; y < this.height; y++)
        {
            final float fy = (float)y / (float)this.height;
            final int currentRow = (int)(bricksy * fy);
            final float offset = currentRow * rowOffset;
            if (currentRow > lastRow)
            {
                lastRow = currentRow;
                count = 0;
                for (int x = 0; x < bricksx;)
                {
                    boolean single = (bricksx - x == 1) ? true : rnd.nextFloatUnipolar() < singleProb;
                    final float fx = (float)x / (float)bricksx;
                    points[count][0] = fx;
                    points[count][1] = rnd.nextFloatUnipolar();
                    count++;
                    x += single ? 1 : 2;
                }
                points[count][0] = 1.f;
            }

            final float y0 = (float)currentRow / (float)bricksy;
            final float y1 = (float)(currentRow + 1) / (float)bricksy;

            for (int x = 0; x < this.width; x++)
            {
                final float fx = ((float)x / (float)this.width + offset) % 1.f;
                int idx = count;
                while (fx < points[idx][0])
                    idx--;
                final float x0 = points[idx][0];
                final float x1 = points[idx + 1][0];

                final float dx0 = Math.min(Math.abs(fx - x0 + 1.f) % 1.f, Math.abs(fx - x0 - 1.f) % 1.f);
                final float dx1 = Math.min(Math.abs(fx - x1 + 1.f) % 1.f, Math.abs(fx - x1 - 1.f) % 1.f);
                final float dx = Math.min(dx0, dx1);
                final float dy = Math.min(Math.abs(fy - y0), Math.abs(fy - y1));

                float jx = (dx / jointsx);
                float jy = (dy / jointsy);
                col = color0.lerp(color1, points[idx][1]);

                if (jx < 1 | jy < 1)
                    col = col.lerp(colorJoints, (float)Math.pow(1.0 - NMath.saturate(jx) * NMath.saturate(jy), jointHardness));

                this.pixels[x + y * this.width] = col;
            }
        }

        return this;
    }

    @Override
    public void workerCallback(WorkerPool<NImagePBlock> pool, WorkerStatus status, Worker<NImagePBlock> worker, NImagePBlock p)
    {
        for (int y = 0; y < p.h; y++)
            System.arraycopy(p.pixels, y * p.w, this.pixels, p.x + (y + p.y) * this.width, p.w);
    }

    public static void drawLine(int x0, int y0, int x1, int y1, List<NPoint> points)
    {
        int x, y;
        int dx, dy, xs, ys, a;

        x = x0;
        y = y0;
        xs = ys = 1;

        dx = x1 - x0;

        if (dx == 0)
        {
            xs = 0;
        }
        else if (dx < 0)
        {
            dx = -dx;
            xs = -xs;
        }

        dy = y1 - y0;

        if (dy == 0)
        {
            ys = 0;
        }
        else if (dy < 0)
        {
            dy = -dy;
            ys = -ys;
        }

        a = dx - dy;

        while ((x != x1) || (y != y1))
        {
            points.add(new NPoint(x, y));

            if (a >= 0)
            {
                x += xs;
                a -= dy;
            }
            if (a < 0)
            {
                y += ys;
                a += dx;
            }
        }
        points.add(new NPoint(x, y));
    }

    public void drawLine(int x0, int y0, int x1, int y1, NColor color)
    {
        int x, y;
        int dx, dy, xs, ys, a;

        x = x0;
        y = y0;
        xs = ys = 1;

        dx = x1 - x0;

        if (dx == 0)
        {
            xs = 0;
        }
        else if (dx < 0)
        {
            dx = -dx;
            xs = -xs;
        }

        dy = y1 - y0;

        if (dy == 0)
        {
            ys = 0;
        }
        else if (dy < 0)
        {
            dy = -dy;
            ys = -ys;
        }

        a = dx - dy;

        while ((x != x1) || (y != y1))
        {
            this.setPixel(x, y, color);

            if (a >= 0)
            {
                x += xs;
                a -= dy;
            }
            if (a < 0)
            {
                y += ys;
                a += dx;
            }
        }

        this.setPixel(x, y, color);
    }

    public void drawThickLine(int x0, int y0, int x1, int y1, int w, NColor color)
    {
        if (w < 2)
        {
            this.drawLine(x0, y0, x1, y1, color);
            return;
        }
        final int dx = x1 - x0;
        final int dy = y1 - y0;

        if (dx == 0)
        {
            if (y1 > y0)
            {
                this.fillRect(x0 - w, y0, w * 2 + 1, y1 - y0 + 1, color);
            }
            else
            {
                this.fillRect(x0 - w, y1, w * 2 + 1, y0 - y1 + 1, color);
            }
            return;
        }

        if (dy == 0)
        {
            if (x1 > x0)
            {
                this.fillRect(x0, y0 - w, x1 - x0 + 1, w * 2 + 1, color);
            }
            else
            {
                this.fillRect(x1, y0 - w, x0 - x1 + 1, w * 2 + 1, color);
            }
            return;
        }

        final double len = Math.sqrt(dx * dx + dy * dy);
        final double vx = dx / len;
        final double vy = dy / len;

        final NPoint[] points = new NPoint[4];

        points[0] = new NPoint((int)(x0 - vy * w), (int)(y0 + vx * w));
        points[1] = new NPoint((int)(x0 + vy * w), (int)(y0 - vx * w));
        points[2] = new NPoint((int)(x1 + vy * w), (int)(y1 - vx * w));
        points[3] = new NPoint((int)(x1 - vy * w), (int)(y1 + vx * w));

        this.fillPolygon(points, color);
    }

    public void drawHLine(int x, int y, int w, NColor color)
    {
        if (w < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        for (int i = x; i < x + w; i++)
        {
            this.setPixel(i, y, color);
        }
    }

    public void drawVLine(int x, int y, int h, NColor color)
    {
        if (h < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        for (int i = y; i < y + h; i++)
        {
            this.setPixel(x, i, color);
        }
    }

    public void drawRect(int x, int y, int w, int h, NColor color)
    {
        this.drawHLine(x, y, w, color);
        this.drawVLine(x, y, h, color);
        this.drawHLine(x, y + h - 1, w, color);
        this.drawVLine(x + w - 1, y, h, color);
    }

    public void fillRect(int x, int y, int w, int h, NColor color)
    {
        for (int i = y; i < y + h; i++)
        {
            this.drawHLine(x, i, w, color);
        }
    }

    public void drawPolygon(NPoint[] points, NColor color)
    {
        for (int i = 0; i < points.length - 1; i++)
            this.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, color);

        if (!points[0].equals(points[points.length - 1]))
            this.drawLine(points[points.length - 1].x, points[points.length - 1].y, points[0].x, points[0].y, color);
    }

    public void drawPolygon(List<NPoint> points, NColor color)
    {
        for (int i = 0; i < points.size() - 1; i++)
            this.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, color);

        if (!Colls.head(points).equals(Colls.last(points)))
            this.drawLine(Colls.last(points).x, Colls.last(points).y, Colls.head(points).x, Colls.head(points).y, color);
    }

    private void _fillPolygon(List<NPoint> dpoints, NColor color)
    {
        Colls.sort(dpoints);

        int i = 0;
        int oy = dpoints.get(i).y;
        for (; i < dpoints.size();)
        {
            int sx = dpoints.get(i).x, ex = 0;
            while (i < dpoints.size() && dpoints.get(i).y == oy)
            {
                ex = dpoints.get(i++).x;
            }
            this.drawHLine(sx, oy, ex - sx + 1, color);
            if (i < dpoints.size())
            {
                oy = dpoints.get(i).y;
            }
        }
    }

    public void fillPolygon(NPoint[] points, NColor color)
    {
        final List<NPoint> dpoints = Colls.list();

        for (int i = 0; i < points.length - 1; i++)
            drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, dpoints);

        if (!points[0].equals(points[points.length - 1]))
            drawLine(points[points.length - 1].x, points[points.length - 1].y, points[0].x, points[0].y, dpoints);

        this._fillPolygon(dpoints, color);
    }

    public void fillPolygon(List<NPoint> points, NColor color)
    {
        final List<NPoint> dpoints = Colls.list();

        for (int i = 0; i < points.size() - 1; i++)
            drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, dpoints);

        if (!Colls.head(points).equals(Colls.last(points)))
            drawLine(Colls.last(points).x, Colls.last(points).y, Colls.head(points).x, Colls.head(points).y, dpoints);

        this._fillPolygon(dpoints, color);
    }

    public void drawCircle(int x, int y, int radius, NColor color)
    {
        if (radius < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        int cy = radius;
        int cx = 0;

        while (cy >= 0 || cx < radius)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.setPixel(x + cx, y + cy, color);
            this.setPixel(x - cx, y + cy, color);
            this.setPixel(x + cx, y - cy, color);
            this.setPixel(x - cx, y - cy, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillCircle(int x, int y, int radius, NColor color)
    {
        if (radius < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        int cy = radius;
        int cx = 0;

        while (cy >= 0 || cx < radius)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.drawHLine(x - cx, y - cy, cx * 2 + 1, color);
            this.drawHLine(x - cx, y + cy, cx * 2 + 1, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawEllipse(int x, int y, int a, int b, NColor color)
    {
        if (a < 2 && b < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        if (a < 2)
        {
            this.drawVLine(x, y - b, b * 2 + 1, color);
            return;
        }
        if (b < 2)
        {
            this.drawHLine(x - a, y, a * 2 + 1, color);
            return;
        }
        int cy = b;
        int cx = 0;
        final double a2 = 1.0 / (a * a);
        final double b2 = 1.0 / (b * b);

        while (cy >= 0 || cx < a)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.setPixel(x + cx, y + cy, color);
            this.setPixel(x - cx, y + cy, color);
            this.setPixel(x + cx, y - cy, color);
            this.setPixel(x - cx, y - cy, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillEllipse(int x, int y, int a, int b, NColor color)
    {
        if (a < 2 && b < 2)
        {
            this.setPixel(x, y, color);
            return;
        }
        if (a < 2)
        {
            this.drawVLine(x, y - b, b * 2 + 1, color);
            return;
        }
        if (b < 2)
        {
            this.drawHLine(x - a, y, a * 2 + 1, color);
            return;
        }
        int cy = b;
        int cx = 0;
        final double a2 = 1.0 / (a * a);
        final double b2 = 1.0 / (b * b);

        while (cy >= 0 || cx < a)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.drawHLine(x - cx, y - cy, cx * 2 + 1, color);
            this.drawHLine(x - cx, y + cy, cx * 2 + 1, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawArc(int x, int y, int radius, double start, double end, NColor color)
    {
        final double invRad = 1.0 / (Math.PI * 180.0);
        int cy = radius;
        int cx = 0;
        double rs = start, re = end;

        while (rs < 0)
            rs += 360.0;
        while (rs >= 360)
            rs -= 360.0;
        while (re < 0)
            re += 360.0;
        while (re >= 360)
            re -= 360.0;

        while (cy >= 0 || cx < radius)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            double wi0 = 90 - (Math.asin(cy / Math.sqrt(cx * cx + cy * cy)) * invRad), wi;
            if (rs > re)
            {
                if (wi0 >= rs || wi0 <= re)
                {
                    this.setPixel(x + cx, y - cy, color);
                }
                wi = 180.0 - wi0;
                if (wi >= rs || wi <= re)
                {
                    this.setPixel(x + cx, y + cy, color);
                }
                wi = 180.0 + wi0;
                if (wi >= rs || wi <= re)
                {
                    this.setPixel(x - cx, y + cy, color);
                }
                wi = 360.0 - wi0;
                if (wi >= rs || wi <= re)
                {
                    this.setPixel(x - cx, y - cy, color);
                }
            }
            else
            {
                if (wi0 >= rs && wi0 <= re)
                {
                    this.setPixel(x + cx, y - cy, color);
                }
                wi = 180.0f - wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x + cx, y + cy, color);
                }
                wi = 180.0f + wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x - cx, y + cy, color);
                }
                wi = 360.0f - wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x - cx, y - cy, color);
                }
            }
            tx = cx + 1;
            ty = cy;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    private void fillArcHLine(int sx, int sy, int cx, int cy, double rs, double re, NColor color)
    {
        final double invRad = 1.0 / (Math.PI * 180.0);
        double cy2 = Math.abs(cy);

        for (int x = -cx; x <= cx; x++)
        {
            double r = Math.sqrt(x * x + cy * cy);
            if (r == 0)
            {
                this.setPixel(sx, sy, color);
                continue;
            }
            double wi = 0;
            if (x == 0 && cy < 0)
                wi = 0;
            else if (x == 0 && cy > 0)
                wi = 180;
            else if (cy == 0 && x > 0)
                wi = 90;
            else if (cy == 0 && x < 0)
                wi = 270;
            else
            {
                wi = 90 - (Math.asin(cy2 / r) * invRad);
                if (cy > 0 && x > 0)
                    wi = 180.0 - wi;
                else if (cy < 0 && x < 0)
                    wi = 360.0 - wi;
                else if (cy > 0 && x < 0) wi = 180.0 + wi;
            }

            if (rs > re)
            {
                if (wi >= rs || wi <= re) this.setPixel(sx + x, sy + cy, color);
            }
            else
            {
                if (wi >= rs && wi <= re) this.setPixel(sx + x, sy + cy, color);
            }
        }
    }

    public void fillArc(int x, int y, int radius, double start, double end, NColor color)
    {
        int cy = radius;
        int cx = 0;
        double rs = start, re = end;

        while (rs < 0)
            rs += 360.0;
        while (rs >= 360)
            rs -= 360.0;
        while (re < 0)
            re += 360.0;
        while (re >= 360)
            re -= 360.0;

        while (cy >= 0 || cx < radius)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;
            this.fillArcHLine(x, y, cx, -cy, rs, re, color);
            this.fillArcHLine(x, y, cx, cy, rs, re, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawRoundRect(int x, int y, int w, int h, int arcsz, NColor color)
    {
        this.drawRoundRect(x, y, w, h, arcsz, arcsz, color);
    }

    public void fillRoundRect(int x, int y, int w, int h, int arcsz, NColor color)
    {
        this.fillRoundRect(x, y, w, h, arcsz, arcsz, color);
    }

    public void drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight, NColor color)
    {
        final int x0 = x + arcWidth;
        final int y0 = y + arcHeight;
        final int x1 = x + w - arcWidth - 1;
        final int y1 = y + h - arcHeight - 1;
        final int w2 = w - arcWidth * 2;
        final int h2 = h - arcHeight * 2;

        if (w2 < 0 || h2 < 0)
        {
            this.drawRect(x, y, w, h, color);
            return;
        }

        this.drawHLine(x0, y, w2, color);
        this.drawHLine(x0, y + h - 1, w2, color);
        this.drawVLine(x, y0, h2, color);
        this.drawVLine(x + w - 1, y0, h2, color);

        int cy = arcHeight;
        int cx = 0;
        final double a2 = 1.0 / (arcWidth * arcWidth);
        final double b2 = 1.0 / (arcHeight * arcHeight);

        while (cy >= 0 || cx < arcWidth)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.setPixel(x1 + cx, y1 + cy, color);
            this.setPixel(x0 - cx, y1 + cy, color);
            this.setPixel(x1 + cx, y0 - cy, color);
            this.setPixel(x0 - cx, y0 - cy, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight, NColor color)
    {
        final int x0 = x + arcWidth;
        final int y0 = y + arcHeight;
        final int y1 = y + h - arcHeight - 1;
        final int w2 = w - arcWidth * 2;
        final int h2 = h - arcHeight * 2;

        if (w2 < 0 || h2 < 0)
        {
            this.fillRect(x, y, w, h, color);
            return;
        }

        for (int i = 0; i < h2; i++)
            this.drawHLine(x, y0 + i, w, color);

        int cy = arcHeight;
        int cx = 0;
        final double a2 = 1.0 / (arcWidth * arcWidth);
        final double b2 = 1.0 / (arcHeight * arcHeight);

        while (cy >= 0 || cx < arcWidth)
        {
            double err = 10, d;
            int nx = cx, ny = cy, tx, ty;

            this.drawHLine(x0 - cx, y0 - cy, cx * 2 + w2, color);
            this.drawHLine(x0 - cx, y1 + cy, cx * 2 + w2, color);

            tx = cx + 1;
            ty = cy;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx + 1;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            tx = cx;
            ty = cy - 1;
            d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
            if (d <= err)
            {
                err = d;
                nx = tx;
                ny = ty;
            }

            cx = nx;
            cy = ny;
        }
    }

    public static class NPoint implements Comparable<NPoint>
    {
        public final int x, y;

        public NPoint(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode()
        {
            return this.x * 31 + this.y;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof NPoint)) return false;
            final NPoint point = (NPoint)obj;
            return this.x == point.x && this.y == point.y;
        }

        @Override
        public int compareTo(NPoint o)
        {
            if (this.y == o.y) return this.x - o.x;
            return this.y - o.y;
        }
    }
}
