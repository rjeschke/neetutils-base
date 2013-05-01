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

import com.github.rjeschke.neetutils.graphics.NColor;

public class Vector4d
{
    public double w;
    public double x;
    public double y;
    public double z;

    public Vector4d()
    {
        // 0
    }

    public Vector4d(final double x, final double y, final double z, final double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4d(final double xyzw)
    {
        this.x = xyzw;
        this.y = xyzw;
        this.z = xyzw;
        this.w = xyzw;
    }

    public Vector4d(final Vector2d v, final double z, final double w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
    }

    public Vector4d(final Vector3d v, final double w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vector4d(final NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
        this.w = color.a;
    }

    public static Vector4d of(final double x, final double y, final double z, final double w)
    {
        return new Vector4d(x, y, z, w);
    }

    public Vector4d set(final double x, final double y, final double z, final double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4d set(final Vector4d v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public Vector4d scale(final double f)
    {
        this.w *= f;
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector4d add(final Vector4d v)
    {
        this.w += v.w;
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector4d add(final Vector4d v, final double scale)
    {
        this.w += v.w * scale;
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }

    public Vector4d sub(final Vector4d v)
    {
        this.w -= v.w;
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector4d sub(final Vector4d v, final double scale)
    {
        this.w -= v.w * scale;
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }

    public Vector4d mul(final Vector4d v)
    {
        this.w *= v.w;
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector4d mul(final Vector4d v, final double scale)
    {
        this.w *= v.w * scale;
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }

    public Vector4d lerp(final Vector4d v, final double f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        this.w += (v.w - this.w) * f;
        return this;
    }

    public double dot(final Vector4d v)
    {
        return this.w * v.w + this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector4d normalize()
    {
        double len = this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
        if (len != 0)
        {
            len = 1.0 / Math.sqrt(len);
            this.w *= len;
            this.x *= len;
            this.y *= len;
            this.z *= len;
        }
        return this;
    }

    public Vector4d negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }

    public double length()
    {
        return Math.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public void intoArray(final double[] arr, final int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
        arr[offset + 3] = this.w;
    }

    public Vector3d toVector3d(final Vector3d v)
    {
        v.x = this.x;
        v.y = this.y;
        v.z = this.z;
        return v;
    }

    public Vector3d toVector3d()
    {
        return toVector3d(new Vector3d());
    }

    public Vector3d toVector3dN(final Vector3d v)
    {
        v.x = this.x / this.w;
        v.y = this.y / this.w;
        v.z = this.z / this.w;
        return v;
    }

    public Vector3d toVector3dN()
    {
        return toVector3dN(new Vector3d());
    }

    public NColor toNColor()
    {
        return new NColor((float)this.w, (float)this.x, (float)this.y, (float)this.z);
    }

    @Override
    public Vector4d clone()
    {
        return new Vector4d(this.x, this.y, this.z, this.w);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
}
