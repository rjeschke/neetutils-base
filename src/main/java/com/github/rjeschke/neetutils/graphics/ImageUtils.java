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
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class ImageUtils
{
    public static BufferedImage toGrayscale(final BufferedImage image)
    {
        if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) return image;

        final int w = image.getWidth();
        final int h = image.getHeight();
        final BufferedImage work;

        if (image.getType() == BufferedImage.TYPE_INT_RGB)
        {
            work = image;
        }
        else
        {
            work = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            final Graphics2D g = work.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }

        final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        final int[] ip = ((DataBufferInt)work.getRaster().getDataBuffer()).getData();
        final byte[] op = ((DataBufferByte)out.getRaster().getDataBuffer()).getData();

        for (int y = 0; y < h; y++)
        {
            for (int x = 0; x < w; x++)
            {
                final int p = x + y * w;
                final int c = ip[p];
                final int r = (c >> 16) & 255;
                final int g = (c >> 8) & 255;
                final int b = c & 255;
                op[p] = (byte)((r * 77 + g * 150 + b * 29) >> 8);
            }
        }

        return out;
    }
}
