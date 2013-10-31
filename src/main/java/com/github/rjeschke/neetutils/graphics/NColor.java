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

import java.awt.Color;

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class NColor
{
    public final float         a, r, g, b;

    public final static NColor WHITE       = new NColor(0xffffffff);
    public final static NColor BLACK       = new NColor(0xff000000);
    public final static NColor RED         = new NColor(0xffff0000);
    public final static NColor GREEN       = new NColor(0xff00ff00);
    public final static NColor BLUE        = new NColor(0xff0000ff);
    public final static NColor WHITE_TRANS = new NColor(0x00ffffff);
    public final static NColor BLACK_TRANS = new NColor(0x00000000);

    public NColor(final int argb)
    {
        this.a = (argb >>> 24) / 255.f;
        this.r = ((argb >> 16) & 255) / 255.f;
        this.g = ((argb >> 8) & 255) / 255.f;
        this.b = (argb & 255) / 255.f;
    }

    public NColor(final Color c)
    {
        this.a = c.getAlpha() / 255.f;
        this.r = c.getRed() / 255.f;
        this.g = c.getGreen() / 255.f;
        this.b = c.getBlue() / 255.f;
    }

    public NColor(final NColor c, final float a)
    {
        this.a = a;
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public NColor(final int argb, final double exp)
    {
        this.a = (argb >>> 24) / 255.f;
        this.r = (float)Math.pow(((argb >> 16) & 255) / 255.0, exp);
        this.g = (float)Math.pow(((argb >> 8) & 255) / 255.0, exp);
        this.b = (float)Math.pow((argb & 255) / 255.0, exp);
    }

    public NColor(final int a, final int r, final int g, final int b)
    {
        this.a = a / 255.f;
        this.r = r / 255.f;
        this.g = g / 255.f;
        this.b = b / 255.f;
    }

    public NColor(final float a, final float r, final float g, final float b)
    {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public NColor(final double a, final double r, final double g, final double b)
    {
        this.a = (float)a;
        this.r = (float)r;
        this.g = (float)g;
        this.b = (float)b;
    }

    public NColor(final int r, final int g, final int b)
    {
        this.a = 1;
        this.r = r / 255.f;
        this.g = g / 255.f;
        this.b = b / 255.f;
    }

    public NColor(final float r, final float g, final float b)
    {
        this.a = 1;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public NColor(final double r, final double g, final double b)
    {
        this.a = 1;
        this.r = (float)r;
        this.g = (float)g;
        this.b = (float)b;
    }

    public NColor(final float a, final NColor c)
    {
        this.a = a;
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public NColor(final double a, final NColor c)
    {
        this.a = (float)a;
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public float dot(final NColor c)
    {
        return dot(this, c);
    }

    public static float dot(final NColor a, final NColor b)
    {
        return a.a * b.a + a.r * b.r + a.g * b.g + a.b + b.b;
    }

    public float dotRGB(final NColor c)
    {
        return dotRGB(this, c);
    }

    public static float dotRGB(final NColor a, final NColor b)
    {
        return a.r * b.r + a.g * b.g + a.b + b.b;
    }

    public NColor add(final NColor c)
    {
        return add(this, c);
    }

    public static NColor add(final NColor a, final NColor b)
    {
        return new NColor(a.a + b.a, a.r + b.r, a.g + b.g, a.b + b.b);
    }

    public NColor addRGB(final NColor c)
    {
        return addRGB(this, c);
    }

    public static NColor addRGB(final NColor a, final NColor b)
    {
        return new NColor(a.a, a.r + b.r, a.g + b.g, a.b + b.b);
    }

    public NColor add(final NColor c, final float scale)
    {
        return add(this, c, scale);
    }

    public static NColor add(final NColor a, final NColor b, final float scale)
    {
        return new NColor(a.a + b.a * scale, a.r + b.r * scale, a.g + b.g * scale, a.b + b.b * scale);
    }

    public NColor addRGB(final NColor c, final float scale)
    {
        return addRGB(this, c, scale);
    }

    public static NColor addRGB(final NColor a, final NColor b, final float scale)
    {
        return new NColor(a.a, a.r + b.r * scale, a.g + b.g * scale, a.b + b.b * scale);
    }

    public NColor sub(final NColor c)
    {
        return sub(this, c);
    }

    public static NColor sub(final NColor a, final NColor b)
    {
        return new NColor(a.a - b.a, a.r - b.r, a.g - b.g, a.b - b.b);
    }

    public NColor subRGB(final NColor c)
    {
        return subRGB(this, c);
    }

    public static NColor subRGB(final NColor a, final NColor b)
    {
        return new NColor(a.a, a.r - b.r, a.g - b.g, a.b - b.b);
    }

    public NColor mul(final NColor c)
    {
        return mul(this, c);
    }

    public static NColor mul(final NColor a, final NColor b)
    {
        return new NColor(a.a * b.a, a.r * b.r, a.g * b.g, a.b * b.b);
    }

    public NColor mulRGB(final NColor c)
    {
        return mulRGB(this, c);
    }

    public static NColor mulRGB(final NColor a, final NColor b)
    {
        return new NColor(a.a, a.r * b.r, a.g * b.g, a.b * b.b);
    }

    public NColor scale(final float scale)
    {
        return scale(this, scale);
    }

    public static NColor scale(final NColor c, final float scale)
    {
        return new NColor(c.a * scale, c.r * scale, c.g * scale, c.b * scale);
    }

    public NColor scaleRGB(final float scale)
    {
        return scaleRGB(this, scale);
    }

    public static NColor scaleRGB(final NColor c, final float scale)
    {
        return new NColor(c.a, c.r * scale, c.g * scale, c.b * scale);
    }

    public NColor lerp(final NColor b, final float f)
    {
        return lerp(this, b, f);
    }

    public static NColor lerp(final NColor a, final NColor b, final float f)
    {
        return new NColor(a.a + (b.a - a.a) * f, a.r + (b.r - a.r) * f, a.g + (b.g - a.g) * f, a.b + (b.b - a.b) * f);
    }

    public NColor lerpRGB(final NColor b, final float f)
    {
        return lerpRGB(this, b, f);
    }

    public static NColor lerpRGB(final NColor a, final NColor b, final float f)
    {
        return new NColor(a.a, a.r + (b.r - a.r) * f, a.g + (b.g - a.g) * f, a.b + (b.b - a.b) * f);
    }

    public NColor blendOver(final NColor src)
    {
        return blendOver(src, this);
    }

    public static NColor blendOver(final NColor src, final NColor dst)
    {
        final float da = NMath.clamp(dst.a, 0.f, 1.f);
        final float sa = NMath.clamp(src.a, 0.f, 1.f);

        if (da == 1.f)
        {
            final float a = 1.f - sa;
            final float r = sa * src.r + dst.r * a;
            final float g = sa * src.g + dst.g * a;
            final float b = sa * src.b + dst.b * a;
            return new NColor(1, r, g, b);
        }

        if (da == 0.f)
        {
            final float r = sa * src.r;
            final float g = sa * src.g;
            final float b = sa * src.b;
            return new NColor(sa, r, g, b);
        }

        final float a = 1.f - sa;
        final float outa = sa + da * a;
        if (outa <= 0) return BLACK_TRANS;

        final float outar = 1.f / outa;
        final float r = (sa * src.r + da * dst.r * a) * outar;
        final float g = (sa * src.g + da * dst.g * a) * outar;
        final float b = (sa * src.b + da * dst.b * a) * outar;

        return new NColor(outa, r, g, b);
    }

    public NColor saturate()
    {
        return saturate(this);
    }

    public static NColor saturate(final NColor c)
    {
        return new NColor(NMath.clamp(c.a, 0, 1), NMath.clamp(c.r, 0, 1), NMath.clamp(c.g, 0, 1), NMath.clamp(c.b, 0, 1));
    }

    public NColor powRGB(final double exp)
    {
        return powRGB(this, exp);
    }

    public static NColor powRGB(final NColor c, final double exp)
    {
        return new NColor(NMath.clamp(c.a, 0, 1), (float)Math.pow(NMath.clamp(c.r, 0, 1), exp), (float)Math.pow(NMath.clamp(c.g, 0, 1), exp), (float)Math.pow(
                NMath.clamp(c.b, 0, 1), exp));
    }

    public float luminance()
    {
        return luminance(this);
    }

    public static float luminance(final NColor c)
    {
        return 0.299f * c.r + 0.587f * c.g + 0.114f * c.b;
    }

    public int toARGB()
    {
        return toARGB(this);
    }

    public int toARGB(final double exp)
    {
        return toARGB(this, exp);
    }

    public static int toARGB(final NColor c)
    {
        final int a = NMath.clamp((int)(c.a * 255.f), 0, 255);
        final int r = NMath.clamp((int)(c.r * 255.f), 0, 255);
        final int g = NMath.clamp((int)(c.g * 255.f), 0, 255);
        final int b = NMath.clamp((int)(c.b * 255.f), 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int toARGB(final NColor c, final double exp)
    {
        final int a = NMath.clamp((int)(c.a * 255.f), 0, 255);
        final int r = (int)(Math.pow(NMath.clamp(c.r, 0.f, 1.f), exp) * 255.0);
        final int g = (int)(Math.pow(NMath.clamp(c.g, 0.f, 1.f), exp) * 255.0);
        final int b = (int)(Math.pow(NMath.clamp(c.b, 0.f, 1.f), exp) * 255.0);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int toARGB(final double r, final double g, final double b)
    {
        final int ir = NMath.clamp((int)(r * 255.0), 0, 255);
        final int ig = NMath.clamp((int)(g * 255.0), 0, 255);
        final int ib = NMath.clamp((int)(b * 255.0), 0, 255);
        return 0xff000000 | (ir << 16) | (ig << 8) | ib;
    }

    public static int toARGBg(final double r, final double g, final double b, final double exp)
    {
        final int ir = (int)(Math.pow(NMath.clamp(r, 0.0, 1.0), exp) * 255.0);
        final int ig = (int)(Math.pow(NMath.clamp(g, 0.0, 1.0), exp) * 255.0);
        final int ib = (int)(Math.pow(NMath.clamp(b, 0.0, 1.0), exp) * 255.0);
        return 0xff000000 | (ir << 16) | (ig << 8) | ib;
    }

    public static int toARGB(final double r, final double g, final double b, final double a)
    {
        final int ia = NMath.clamp((int)(a * 255.0), 0, 255);
        final int ir = NMath.clamp((int)(r * 255.0), 0, 255);
        final int ig = NMath.clamp((int)(g * 255.0), 0, 255);
        final int ib = NMath.clamp((int)(b * 255.0), 0, 255);
        return (ia << 24) | (ir << 16) | (ig << 8) | ib;
    }

    public static int toARGBg(final double r, final double g, final double b, final double a, final double exp)
    {
        final int ia = NMath.clamp((int)(a * 255.0), 0, 255);
        final int ir = (int)(Math.pow(NMath.clamp(r, 0.0, 1.0), exp) * 255.0);
        final int ig = (int)(Math.pow(NMath.clamp(g, 0.0, 1.0), exp) * 255.0);
        final int ib = (int)(Math.pow(NMath.clamp(b, 0.0, 1.0), exp) * 255.0);
        return (ia << 24) | (ir << 16) | (ig << 8) | ib;
    }

    public Color toColor()
    {
        return toColor(this);
    }

    public static Color toColor(final NColor c)
    {
        return new Color(toARGB(c), true);
    }

    public NColor transform(final NColorMatrix m)
    {
        return transform(this, m);
    }

    public static NColor transform(final NColor c, final NColorMatrix m)
    {
        return new NColor(c.a, c.r * m.m[0 + 0 * 3] + c.g * m.m[1 + 0 * 3] + c.b * m.m[2 + 0 * 3], c.r * m.m[0 + 1 * 3] + c.g * m.m[1 + 1 * 3] + c.b
                * m.m[2 + 1 * 3], c.r * m.m[0 + 2 * 3] + c.g * m.m[1 + 2 * 3] + c.b * m.m[2 + 2 * 3]);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append(this.a);
        sb.append(", ");
        sb.append(this.r);
        sb.append(", ");
        sb.append(this.g);
        sb.append(", ");
        sb.append(this.b);
        sb.append('}');
        return sb.toString();
    }
}
