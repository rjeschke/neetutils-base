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
package com.github.rjeschke.neetutils.vectors;

import java.nio.DoubleBuffer;

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class Vector2d
{
    public double x;
    public double y;

    public Vector2d()
    {
        // default
    }

    public Vector2d(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2d(final double xy)
    {
        this.x = xy;
        this.y = xy;
    }

    public Vector2d(final Vector2d v)
    {
        this.x = v.x;
        this.y = v.y;
    }

    public static Vector2d of(final double x, final double y)
    {
        return new Vector2d(x, y);
    }

    public static Vector2d of(final Vector2d v)
    {
        return new Vector2d(v);
    }

    public Vector2d set(final double x, final double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2d set(final double xy)
    {
        this.x = xy;
        this.y = xy;
        return this;
    }

    public Vector2d set(final DoubleBuffer buffer, final int offset)
    {
        this.x = buffer.get(offset);
        this.y = buffer.get(offset + 1);
        return this;
    }

    public Vector2d set(final double[] buffer, final int offset)
    {
        this.x = buffer[offset];
        this.y = buffer[offset + 1];
        return this;
    }

    public Vector2d set(final Vector2d v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2d set(final Vector3d v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2d set(final Vector4d v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2d setIdx(final int index, final double value)
    {
        if (index == 0)
        {
            this.x = value;
        }
        else
        {
            this.y = value;
        }
        return this;
    }

    public double get(final int index)
    {
        if (index == 0)
        {
            return this.x;
        }
        return this.y;
    }

    public Vector2d scale(final double f)
    {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Vector2d add(final Vector2d v)
    {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2d add(final Vector2d v, final double scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        return this;
    }

    public Vector2d sub(final Vector2d v)
    {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2d sub(final Vector2d v, final double scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        return this;
    }

    public Vector2d mul(final Vector2d v)
    {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2d mul(final Vector2d v, final double scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        return this;
    }

    public Vector2d div(final Vector2d v)
    {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public Vector2d div(final Vector2d v, final double scale)
    {
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        return this;
    }

    public Vector2d lerp(final Vector2d v, final double f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        return this;
    }

    public double dot(final Vector2d v)
    {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2d min(final Vector2d v)
    {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        return this;
    }

    public double min()
    {
        return Math.min(this.x, this.y);
    }

    public Vector2d max(final Vector2d v)
    {
        this.x = Math.max(this.x, v.x);
        this.y = Math.max(this.y, v.y);
        return this;
    }

    public double max()
    {
        return Math.min(this.x, this.y);
    }

    public Vector2d clamp(final Vector2d min, final Vector2d max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        return this;
    }

    public Vector2d abs()
    {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }

    public Vector2d pow(final double value)
    {
        this.x = Math.pow(this.x, value);
        this.y = Math.pow(this.y, value);
        return this;
    }

    public Vector2d normalize()
    {
        double len = this.x * this.x + this.y * this.y;
        if (len != 0)
        {
            len = 1 / Math.sqrt(len);
            this.x *= len;
            this.y *= len;
        }
        return this;
    }

    public Vector2d negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public double length()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2d swizzle(final int a, final int b, final Vector2d out)
    {
        return out.set(this.get(a), this.get(b));
    }

    public Vector2d swizzle(final int a, final int b)
    {
        return this.swizzle(a, b, new Vector2d());
    }

    public Vector3d swizzle(final int a, final int b, final int c, final Vector3d out)
    {
        return out.set(this.get(a), this.get(b), this.get(c));
    }

    public Vector3d swizzle(final int a, final int b, final int c)
    {
        return this.swizzle(a, b, c, new Vector3d());
    }

    public Vector4d swizzle(final int a, final int b, final int c, final int d, final Vector4d out)
    {
        return out.set(this.get(a), this.get(b), this.get(c), this.get(d));
    }

    public Vector4d swizzle(final int a, final int b, final int c, final int d)
    {
        return this.swizzle(a, b, c, d, new Vector4d());
    }

    public void into(final double[] arr, final int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
    }

    public void into(final DoubleBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
    }

    @Override
    public Vector2d clone()
    {
        return new Vector2d(this.x, this.y);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }
}
