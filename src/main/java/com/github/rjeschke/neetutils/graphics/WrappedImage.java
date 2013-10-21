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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class WrappedImage
{
    private int           width;
    private int           height;
    private BufferedImage image;
    private int[]         pixels;
    private final boolean hasTransparency;

    public WrappedImage(final int width, final int height, final boolean hasTransparency)
    {
        this.hasTransparency = hasTransparency;
        this.alloc(width, height);
    }

    public WrappedImage(final int width, final int height)
    {
        this(width, height, false);
    }

    public WrappedImage(final BufferedImage image, final boolean hasTransparency)
    {
        this.image = sanitize(image, hasTransparency);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
        this.hasTransparency = hasTransparency;
    }

    public final static BufferedImage sanitize(final BufferedImage image, final boolean hasTransparency)
    {
        BufferedImage ret;
        if (hasTransparency)
        {
            if (image.getType() == BufferedImage.TYPE_INT_ARGB) return image;
            ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        else
        {
            if (image.getType() == BufferedImage.TYPE_INT_RGB) return image;
            ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        ret.createGraphics().drawImage(image, 0, 0, null);

        return ret;
    }

    private void alloc(final int width, final int height)
    {
        final int w = Math.max(1, width);
        final int h = Math.max(1, height);
        if (this.image == null || this.width != w || this.height != h)
        {
            this.width = w;
            this.height = h;
            this.image = new BufferedImage(w, h, this.hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
            this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
        }
    }

    public boolean hasTransparency()
    {
        return this.hasTransparency;
    }

    public void resize(final int width, final int height)
    {
        this.alloc(width, height);
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public BufferedImage getImage()
    {
        return this.image;
    }

    public int[] getPixels()
    {
        return this.pixels;
    }

    public int getPixel(final int x, final int y)
    {
        return this.pixels[NMath.clamp(x, 0, this.width - 1) + NMath.clamp(y, 0, this.height - 1) * this.width];
    }

    public void setPixel(final int x, final int y, final int c)
    {
        this.pixels[NMath.clamp(x, 0, this.width - 1) + NMath.clamp(y, 0, this.height - 1) * this.width] = c;
    }

    public Graphics2D createGraphics()
    {
        return this.image.createGraphics();
    }

    public Graphics2D createGraphicsClipped()
    {
        final Graphics2D g = this.image.createGraphics();
        g.setClip(0, 0, this.width, this.height);
        return g;
    }

    public Graphics2D createGraphicsClipped(final int tx, final int ty, final int clipWidth, final int clipHeight)
    {
        final Graphics2D g = this.image.createGraphics();
        g.translate(tx, ty);
        g.setClip(0, 0, clipWidth, clipHeight);
        return g;
    }

    public static WrappedImage fromFile(final File file, final boolean hasTransparency) throws IOException
    {
        return new WrappedImage(ImageIO.read(file), hasTransparency);
    }

    public static WrappedImage fromFile(final String filename, final boolean hasTransparency) throws IOException
    {
        return fromFile(new File(filename), hasTransparency);
    }

    public static WrappedImage fromResource(final String resourcename, final boolean hasTransparency) throws IOException
    {
        try (final InputStream in = WrappedImage.class.getResourceAsStream(resourcename))
        {
            final WrappedImage img = new WrappedImage(ImageIO.read(in), hasTransparency);
            return img;
        }
    }
}
