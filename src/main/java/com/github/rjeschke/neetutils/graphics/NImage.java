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
import com.github.rjeschke.neetutils.vectors.Vector3f;

public class NImage implements WorkerCallback<NImagePBlock>
{
    public final int            width;
    public final int            height;
    final NColor[]              pixels;
    private ClampMode           clampX            = ClampMode.CLAMP_TO_EDGE;
    private ClampMode           clampY            = ClampMode.CLAMP_TO_EDGE;
    private ColorOp             cop               = ColorOp.SET;
    private int                 processingThreads = 1;
    final static float[]        SOBEL_X           = Colls.array(1.f, 0, -1, 2, 0, -2, 1, 0, -1);
    final static float[]        SOBEL_Y           = Colls.array(1.f, 2, 1, 0, 0, 0, -1, -2, -1);
    final static int[]          PAINT_DX          = Colls.array(1, 1, 0);
    final static int[]          PAINT_DY          = Colls.array(0, -1, -1);
    final static int            BLOCK_SIZE        = 8;
    private final static double TO_sRGB           = 1.0 / 2.2;
    private final static double FROM_sRGB         = 2.2;

    public NImage(final int width, final int height)
    {
        this.width = width;
        this.height = height;
        this.pixels = new NColor[width * height];
        Arrays.fill(this.pixels, NColor.BLACK_TRANS);
    }

    public NImage(final NImage image)
    {
        this.width = image.width;
        this.height = image.height;
        this.pixels = new NColor[this.width * this.height];
        this.processingThreads = image.processingThreads;
        System.arraycopy(image.pixels, 0, this.pixels, 0, this.pixels.length);
    }

    public NImage(final BufferedImage image)
    {
        this(image, FROM_sRGB);
    }

    public NImage(final WrappedImage image)
    {
        this(image.getImage(), FROM_sRGB);
    }

    public NImage(final BufferedImage image, final double gamma)
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

    private static BufferedImage forceARGB(final BufferedImage in)
    {
        if (in.getType() == BufferedImage.TYPE_INT_ARGB) return in;
        final BufferedImage img = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        return img;
    }

    private final static int clampXY(final ClampMode c, final int v, final int max)
    {
        int temp;

        switch (c)
        {
        default:
        case CLAMP_TO_EDGE:
            return NMath.clamp(v, 0, max - 1);
        case WRAP:
            temp = v % max;
            return temp < 0 ? temp + max : temp;
        }
    }

    private int clampX(final int x)
    {
        return clampXY(this.clampX, x, this.width);
    }

    private int clampY(final int y)
    {
        return clampXY(this.clampY, y, this.height);
    }

    private int clampedPos(final int x, final int y)
    {
        return this.clampX(x) + this.clampY(y) * this.width;
    }

    public NImage setThreadCount(final int threads)
    {
        this.processingThreads = Math.max(1, threads);
        return this;
    }

    public void setPixel(final int x, final int y, final NColor c)
    {
        if (this.clampX == ClampMode.CLIP && x < 0 || x >= this.width) return;
        if (this.clampY == ClampMode.CLIP && y < 0 || y >= this.height) return;

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
        case BLEND1:
            this.pixels[p] = new NColor(this.pixels[p].a, NColor.lerpRGB(this.pixels[p], c, c.a));
            break;
        }
    }

    public NImage forceFill(final NColor c)
    {
        Arrays.fill(this.pixels, c);
        return this;
    }

    public NImage fill(final NColor c)
    {
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                this.setPixel(x, y, c);
            }
        }

        return this;
    }

    public NColor getPixel(final int x, final int y)
    {
        if (this.clampX == ClampMode.CLIP && x < 0 || x >= this.width) return NColor.BLACK_TRANS;
        if (this.clampY == ClampMode.CLIP && y < 0 || y >= this.height) return NColor.BLACK_TRANS;

        return this.pixels[this.clampedPos(x, y)];
    }

    public NImage clampColors()
    {
        for (int i = 0; i < this.pixels.length; i++)
        {
            this.pixels[i] = this.pixels[i].saturate();
        }
        return this;
    }

    public NImage invertColors()
    {
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            this.pixels[i] = new NColor(1.0 - c.a, 1.0 - c.r, 1.0 - c.g, 1.0 - c.b);
        }

        return this;
    }

    public NImage invertColorsRGB()
    {
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            this.pixels[i] = new NColor(c.a, 1.0 - c.r, 1.0 - c.g, 1.0 - c.b);
        }

        return this;
    }

    public void setClampMode(final ClampMode clampX, final ClampMode clampY)
    {
        this.clampX = clampX;
        this.clampY = clampY;
    }

    public void setColorOp(final ColorOp op)
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
        {
            d = 1;
        }
        else
        {
            d = 1.f / d;
        }
        for (int i = 0; i < this.pixels.length; i++)
        {
            final NColor c = this.pixels[i];
            this.pixels[i] = new NColor((c.a - min) * d, (c.r - min) * d, (c.g - min) * d, (c.b - min) * d);
        }
        return this;
    }

    public NImage setAlpha(final float alpha)
    {
        for (int i = 0; i < this.pixels.length; i++)
        {
            this.pixels[i] = new NColor(alpha, this.pixels[i]);
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

    public BufferedImage toBufferedImageARGB(final double gamma)
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
        return this.toBufferedImageARGBDithered(TO_sRGB);
    }

    public BufferedImage toBufferedImageARGBDithered(final double gamma)
    {
        final BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        final int[] pix = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

        final float[] err0 = new float[3 * this.width + 6];
        final float[] err1 = new float[3 * this.width + 6];

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

    public NImage bump(final NImage normals, final Vector3f light, final NColor ambient, final NColor diffuse, final NColor specular, final float power,
            final float heightScale, final boolean directional)
    {
        final Vector3f l = new Vector3f(), n = new Vector3f(), h = new Vector3f();
        NColor color, out, norm;

        l.set(light);
        if (directional)
        {
            l.negate().normalize();
        }

        for (int y = 0; y < this.height; y++)
        {
            final float fz = (float)y / (float)this.height;
            for (int x = 0; x < this.width; x++)
            {
                final float fx = (float)x / (float)this.width;
                color = this.getPixel(x, y);
                norm = normals.getPixel(x, y);
                n.set(norm.r * 2.f - 1.f, norm.b * 2.f - 1.f, norm.g * 2.f - 1.f).normalize();
                final float fy = norm.a * heightScale;

                if (!directional)
                {
                    l.set(light.x - fx, light.y - fy, light.z - fz).normalize();
                }

                out = color.mulRGB(ambient);

                float d = Math.max(0.f, n.dot(l));
                if (d > 0)
                {
                    out = out.addRGB(diffuse.mulRGB(color), d);
                    if (power > 0)
                    {
                        h.set(light.x, light.y + 1, light.z).normalize();
                        d = (float)Math.pow(Math.max(0.f, n.dot(h)), power);
                        out = out.addRGB(specular, d);
                    }
                }
                this.pixels[x + y * this.width] = out;
            }
        }
        return this;
    }

    public NImage normals(final float scale)
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
                pix[x + y * this.width] = new NColor(NMath.saturate(dx * 0.5f + 0.5f), NMath.saturate(dy * 0.5f + 0.5f), NMath.saturate(dz * 0.5f + 0.5f),
                        c.luminance());
            }
        }

        System.arraycopy(pix, 0, this.pixels, 0, pix.length);
        return this;
    }

    public NImage perlin(final int seed, final float scalex, final float scaley, final int octaves, final float fallOff, final float amp, final NColor color0,
            final NColor color1)
    {
        return this.runThreaded(new NImagePerlin(this, seed, scalex, scaley, octaves, fallOff, amp, color0, color1), BLOCK_SIZE);
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

    @Override
    public void workerCallback(final WorkerPool<NImagePBlock> pool, final WorkerStatus status, final Worker<NImagePBlock> worker, final NImagePBlock p)
    {
        for (int y = 0; y < p.h; y++)
            System.arraycopy(p.pixels, y * p.w, this.pixels, p.x + (y + p.y) * this.width, p.w);
    }

    public NImage boxDownsample(final int fx, final int fy)
    {
        final NImage ret = new NImage(this.width / fx, this.height / fy);
        ret.setThreadCount(this.processingThreads);
        return ret.runThreaded(new NImageBoxDownsampler(this, fx, fy), BLOCK_SIZE);
    }

    public NImage decimate(final int fx, final int fy)
    {
        final NImage ret = new NImage(this.width / fx, this.height / fy);
        ret.setThreadCount(this.processingThreads);
        final int fx2 = fx / 2;
        final int fy2 = fy / 2;
        for (int y = 0; y < ret.height; y++)
        {
            for (int x = 0; x < ret.width; x++)
            {
                ret.setPixel(x, y, this.getPixel(x * fx + fx2, y * fy + fy2));
            }
        }

        return ret;
    }

    public NImage filter(final FilterKernel kernel)
    {
        if (kernel.isSingle) return this.runThreaded(new NImageFilter(new NImage(this), kernel, 0), BLOCK_SIZE);

        this.runThreaded(new NImageFilter(this, kernel, 1), BLOCK_SIZE);
        return this.runThreaded(new NImageFilter(this, kernel, 2), BLOCK_SIZE);
    }

    public NImage combine(final NImage other, final ColorOp colorOp, final int dx, final int dy, final int sx, final int sy, final int w, final int h)
    {
        final int tw = Math.min(Math.min(w, other.width - sx), this.width - dx);
        final int th = Math.min(Math.min(h, other.height - sy), this.height - dy);
        final ColorOp old = this.cop;

        this.cop = colorOp;
        for (int y = 0; y < th; y++)
        {
            for (int x = 0; x < tw; x++)
            {
                this.setPixel(dx + x, dy + y, other.getPixel(sx + x, sy + y));
            }
        }
        this.cop = old;

        return this;
    }

    public NImage combine(final NImage other, final int dx, final int dy)
    {
        return this.combine(other, ColorOp.SET, dx, dy, 0, 0, other.width, other.height);
    }

    public NImage combine(final NImage other)
    {
        return this.combine(other, ColorOp.SET, 0, 0, 0, 0, other.width, other.height);
    }

    public NImage combine(final NImage other, final ColorOp colorOp, final int dx, final int dy)
    {
        return this.combine(other, colorOp, dx, dy, 0, 0, other.width, other.height);
    }

    public NImage combine(final NImage other, final ColorOp colorOp)
    {
        return this.combine(other, colorOp, 0, 0, 0, 0, other.width, other.height);
    }

    public NImage voronoi(final int seed, final int max, final float minDist, final float fallOff, final boolean invert, final boolean colorCells,
            final NColor color0, final NColor color1)
    {
        return this.runThreaded(new NImageVoronoi(this, seed, max, minDist, fallOff, invert, colorCells, color0, color1), BLOCK_SIZE);
    }

    public NImage bricks(final int seed, final int bricksx, final int bricksy, final float jointsx, final float jointsy, final float singleProb,
            final float rowOffset, final float jointHardness, final NColor color0, final NColor color1, final NColor colorJoints)
    {
        final RNG rnd = RNGFactory.create(RNGType.LCG, seed);
        NColor col;
        final float[][] points = new float[bricksx + 1][2];
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
                    final boolean single = (bricksx - x == 1) ? true : rnd.nextFloatUnipolar() < singleProb;
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

                final float jx = (dx / jointsx);
                final float jy = (dy / jointsy);
                col = color0.lerp(color1, points[idx][1]);

                if (jx < 1 | jy < 1) col = col.lerp(colorJoints, (float)Math.pow(1.0 - NMath.saturate(jx) * NMath.saturate(jy), jointHardness));

                this.pixels[x + y * this.width] = col;
            }
        }

        return this;
    }

    public static void drawLine(final int x0, final int y0, final int x1, final int y1, final List<NPoint> points)
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

    public void drawLine(final int x0, final int y0, final int x1, final int y1, final NColor color)
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

    public void drawThickLine(final int x0, final int y0, final int x1, final int y1, final int w, final NColor color)
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

    public void drawHLine(final int x, final int y, final int w, final NColor color)
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

    public void drawVLine(final int x, final int y, final int h, final NColor color)
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

    public void drawRect(final int x, final int y, final int w, final int h, final NColor color)
    {
        this.drawHLine(x, y, w, color);
        this.drawVLine(x, y, h, color);
        this.drawHLine(x, y + h - 1, w, color);
        this.drawVLine(x + w - 1, y, h, color);
    }

    public void fillRect(final int x, final int y, final int w, final int h, final NColor color)
    {
        for (int i = y; i < y + h; i++)
        {
            this.drawHLine(x, i, w, color);
        }
    }

    public void drawPolygon(final NPoint[] points, final NColor color)
    {
        for (int i = 0; i < points.length - 1; i++)
            this.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, color);

        if (!points[0].equals(points[points.length - 1]))
            this.drawLine(points[points.length - 1].x, points[points.length - 1].y, points[0].x, points[0].y, color);
    }

    public void drawPolygon(final List<NPoint> points, final NColor color)
    {
        for (int i = 0; i < points.size() - 1; i++)
            this.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, color);

        if (!Colls.head(points).equals(Colls.last(points)))
            this.drawLine(Colls.last(points).x, Colls.last(points).y, Colls.head(points).x, Colls.head(points).y, color);
    }

    private void _fillPolygon(final List<NPoint> dpoints, final NColor color)
    {
        Colls.sort(dpoints);

        int i = 0;
        int oy = dpoints.get(i).y;
        for (; i < dpoints.size();)
        {
            final int sx = dpoints.get(i).x;
            int ex = 0;
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

    public void fillPolygon(final NPoint[] points, final NColor color)
    {
        final List<NPoint> dpoints = Colls.list();

        for (int i = 0; i < points.length - 1; i++)
            drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, dpoints);

        if (!points[0].equals(points[points.length - 1]))
            drawLine(points[points.length - 1].x, points[points.length - 1].y, points[0].x, points[0].y, dpoints);

        this._fillPolygon(dpoints, color);
    }

    public void fillPolygon(final List<NPoint> points, final NColor color)
    {
        final List<NPoint> dpoints = Colls.list();

        for (int i = 0; i < points.size() - 1; i++)
            drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, dpoints);

        if (!Colls.head(points).equals(Colls.last(points)))
            drawLine(Colls.last(points).x, Colls.last(points).y, Colls.head(points).x, Colls.head(points).y, dpoints);

        this._fillPolygon(dpoints, color);
    }

    public void drawCircle(final int x, final int y, final int radius, final NColor color)
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
            int nx = cx, ny = cy;

            this.setPixel(x + cx, y + cy, color);
            this.setPixel(x - cx, y + cy, color);
            this.setPixel(x + cx, y - cy, color);
            this.setPixel(x - cx, y - cy, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillCircle(final int x, final int y, final int radius, final NColor color)
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
            int nx = cx, ny = cy;

            this.drawHLine(x - cx, y - cy, cx * 2 + 1, color);
            this.drawHLine(x - cx, y + cy, cx * 2 + 1, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawEllipse(final int x, final int y, final int a, final int b, final NColor color)
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
            int nx = cx, ny = cy;

            this.setPixel(x + cx, y + cy, color);
            this.setPixel(x - cx, y + cy, color);
            this.setPixel(x + cx, y - cy, color);
            this.setPixel(x - cx, y - cy, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillEllipse(final int x, final int y, final int a, final int b, final NColor color)
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
            int nx = cx, ny = cy;

            this.drawHLine(x - cx, y - cy, cx * 2 + 1, color);
            this.drawHLine(x - cx, y + cy, cx * 2 + 1, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawArc(final int x, final int y, final int radius, final double start, final double end, final NColor color)
    {
        final double invRad = 180.0 / Math.PI;
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
            int nx = cx, ny = cy;

            final double wi0 = 90 - (Math.asin(cy / Math.sqrt(cx * cx + cy * cy)) * invRad);
            double wi;
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
                wi = 180.0 - wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x + cx, y + cy, color);
                }
                wi = 180.0 + wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x - cx, y + cy, color);
                }
                wi = 360.0 - wi0;
                if (wi >= rs && wi <= re)
                {
                    this.setPixel(x - cx, y - cy, color);
                }
            }
            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    private void fillArcHLine(final int sx, final int sy, final int cx, final int cy, final double rs, final double re, final NColor color)
    {
        final double invRad = 180.0 / Math.PI;
        final double cy2 = Math.abs(cy);

        for (int x = -cx; x <= cx; x++)
        {
            final double r = Math.sqrt(x * x + cy * cy);
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

    public void fillArc(final int x, final int y, final int radius, final double start, final double end, final NColor color)
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
            int nx = cx, ny = cy;
            this.fillArcHLine(x, y, cx, -cy, rs, re, color);
            this.fillArcHLine(x, y, cx, cy, rs, re, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(radius - Math.sqrt(tx * tx + ty * ty));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void drawRoundRect(final int x, final int y, final int w, final int h, final int arcsz, final NColor color)
    {
        this.drawRoundRect(x, y, w, h, arcsz, arcsz, color);
    }

    public void fillRoundRect(final int x, final int y, final int w, final int h, final int arcsz, final NColor color)
    {
        this.fillRoundRect(x, y, w, h, arcsz, arcsz, color);
    }

    public void drawRoundRect(final int x, final int y, final int w, final int h, final int arcWidth, final int arcHeight, final NColor color)
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
            int nx = cx, ny = cy;

            this.setPixel(x1 + cx, y1 + cy, color);
            this.setPixel(x0 - cx, y1 + cy, color);
            this.setPixel(x1 + cx, y0 - cy, color);
            this.setPixel(x0 - cx, y0 - cy, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public void fillRoundRect(final int x, final int y, final int w, final int h, final int arcWidth, final int arcHeight, final NColor color)
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
            int nx = cx, ny = cy;

            this.drawHLine(x0 - cx, y0 - cy, cx * 2 + w2, color);
            this.drawHLine(x0 - cx, y1 + cy, cx * 2 + w2, color);

            for (int i = 0; i < 3; i++)
            {
                final int tx = cx + PAINT_DX[i];
                final int ty = cy + PAINT_DY[i];
                d = Math.abs(1.0 - Math.sqrt(tx * tx * a2 + ty * ty * b2));
                if (d <= err)
                {
                    err = d;
                    nx = tx;
                    ny = ty;
                }
            }

            cx = nx;
            cy = ny;
        }
    }

    public static class NPoint implements Comparable<NPoint>
    {
        public final int x, y;

        public NPoint(final int x, final int y)
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
        public boolean equals(final Object obj)
        {
            if (!(obj instanceof NPoint)) return false;
            final NPoint point = (NPoint)obj;
            return this.x == point.x && this.y == point.y;
        }

        @Override
        public int compareTo(final NPoint o)
        {
            if (this.y == o.y) return this.x - o.x;
            return this.y - o.y;
        }
    }
}
