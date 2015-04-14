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

import com.github.rjeschke.neetutils.graphics.NColor;
import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class Vector3d
{
    public double x;
    public double y;
    public double z;

    public Vector3d()
    {
        // 0
    }

    public Vector3d(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(final double xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    public Vector3d(final Vector2d v, final double z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    public Vector3d(final Vector3d v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3d(final NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
    }

    public static Vector3d of(final double x, final double y, final double z)
    {
        return new Vector3d(x, y, z);
    }

    public static Vector3d of(final Vector2d v, final double z)
    {
        return new Vector3d(v, z);
    }

    public static Vector3d of(final Vector3d v)
    {
        return new Vector3d(v);
    }

    public Vector3d set(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3d set(final double xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
        return this;
    }

    public Vector3d set(final DoubleBuffer buffer, final int offset)
    {
        this.x = buffer.get(offset);
        this.y = buffer.get(offset + 1);
        this.z = buffer.get(offset + 2);
        return this;
    }

    public Vector3d set(final double[] buffer, final int offset)
    {
        this.x = buffer[offset];
        this.y = buffer[offset + 1];
        this.z = buffer[offset + 2];
        return this;
    }

    public Vector3d set(final Vector2d v, final double z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        return this;
    }

    public Vector3d set(final Vector3d v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3d set(final Vector4d v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3d setIdx(final int index, final double value)
    {
        if (index == 0)
        {
            this.x = value;
        }
        else if (index == 1)
        {
            this.y = value;
        }
        else
        {
            this.z = value;
        }
        return this;
    }

    public double get(final int index)
    {
        if (index == 0)
        {
            return this.x;
        }
        if (index == 1)
        {
            return this.y;
        }
        return this.z;
    }

    public Vector3d scale(final double f)
    {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector3d add(final Vector3d v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3d add(final Vector3d v, final double scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }

    public Vector3d sub(final Vector3d v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3d sub(final Vector3d v, final double scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }

    public Vector3d mul(final Vector3d v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3d mul(final Vector3d v, final double scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }

    public Vector3d div(final Vector3d v)
    {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector3d div(final Vector3d v, final double scale)
    {
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        this.z /= v.z * scale;
        return this;
    }

    public Vector3d lerp(final Vector3d v, final double f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        return this;
    }

    public double dot(final Vector3d v)
    {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3d cross(final Vector3d v)
    {
        final double x = this.y * v.z - this.z * v.y;
        final double z = this.x * v.y - this.y * v.x;
        final double y = this.z * v.x - this.x * v.z;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3d min(final Vector3d other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        return this;
    }

    public double min()
    {
        return Math.min(this.x, Math.min(this.y, this.z));
    }

    public Vector3d max(final Vector3d other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        return this;
    }

    public double max()
    {
        return Math.max(this.x, Math.max(this.y, this.z));
    }

    public Vector3d clamp(final Vector3d min, final Vector3d max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        return this;
    }

    public Vector3d abs()
    {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }

    public Vector3d pow(final double value)
    {
        this.x = Math.pow(this.x, value);
        this.y = Math.pow(this.y, value);
        this.z = Math.pow(this.z, value);
        return this;
    }

    public Vector3d normalize()
    {
        double len = this.x * this.x + this.y * this.y + this.z * this.z;
        if (len != 0)
        {
            len = 1.0 / Math.sqrt(len);
            this.x *= len;
            this.y *= len;
            this.z *= len;
        }
        return this;
    }

    public Vector3d negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public double length()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
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
        arr[offset + 2] = this.z;
    }

    public void into(final DoubleBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
    }

    public Vector2d toVector2(final Vector2d out)
    {
        return out.set(this.x, this.y);
    }

    public Vector2d toVector2()
    {
        return this.toVector2(new Vector2d());
    }

    public NColor asNColor()
    {
        return new NColor((float)this.x, (float)this.y, (float)this.z);
    }

    public NColor asNColor(final float alpha)
    {
        return new NColor(alpha, (float)this.x, (float)this.y, (float)this.z);
    }

    @Override
    public Vector3d clone()
    {
        return new Vector3d(this.x, this.y, this.z);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
