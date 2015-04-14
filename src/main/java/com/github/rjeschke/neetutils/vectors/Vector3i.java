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

import java.nio.IntBuffer;

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class Vector3i
{
    public int x;
    public int y;
    public int z;

    public Vector3i()
    {
        // 0
    }

    public Vector3i(final int x, final int y, final int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(final int xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    public Vector3i(final Vector2i v, final int z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    public Vector3i(final Vector3i v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public static Vector3i of(final int x, final int y, final int z)
    {
        return new Vector3i(x, y, z);
    }

    public static Vector3i of(final Vector2i v, final int z)
    {
        return new Vector3i(v, z);
    }

    public static Vector3i of(final Vector3i v)
    {
        return new Vector3i(v);
    }

    public Vector3i set(final int x, final int y, final int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3i set(final int xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
        return this;
    }

    public Vector3i set(final IntBuffer buffer, final int offset)
    {
        this.x = buffer.get(offset);
        this.y = buffer.get(offset + 1);
        this.z = buffer.get(offset + 2);
        return this;
    }

    public Vector3i set(final int[] buffer, final int offset)
    {
        this.x = buffer[offset];
        this.y = buffer[offset + 1];
        this.z = buffer[offset + 2];
        return this;
    }

    public Vector3i set(final Vector2i v, final int z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        return this;
    }

    public Vector3i set(final Vector3i v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3i set(final Vector4i v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3i setIdx(final int index, final int value)
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

    public int get(final int index)
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

    public Vector3i scale(final int f)
    {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector3i add(final Vector3i v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3i add(final Vector3i v, final int scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }

    public Vector3i sub(final Vector3i v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3i sub(final Vector3i v, final int scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }

    public Vector3i mul(final Vector3i v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3i mul(final Vector3i v, final int scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }

    public Vector3i div(final Vector3i v)
    {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector3i div(final Vector3i v, final int scale)
    {
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        this.z /= v.z * scale;
        return this;
    }

    public Vector3i lerp(final Vector3i v, final int f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        return this;
    }

    public int dot(final Vector3i v)
    {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3i cross(final Vector3i v)
    {
        final int x = this.y * v.z - this.z * v.y;
        final int z = this.x * v.y - this.y * v.x;
        final int y = this.z * v.x - this.x * v.z;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3i min(final Vector3i other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        return this;
    }

    public int min()
    {
        return Math.min(this.x, Math.min(this.y, this.z));
    }

    public Vector3i max(final Vector3i other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        return this;
    }

    public int max()
    {
        return Math.max(this.x, Math.max(this.y, this.z));
    }

    public Vector3i clamp(final Vector3i min, final Vector3i max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        return this;
    }

    public Vector3i abs()
    {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }

    public Vector3i negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public double length()
    {
        return Math.sqrt((double)this.x * (double)this.x
                + (double)this.y * (double)this.y
                + (double)this.z * (double)this.z);
    }

    public Vector2i swizzle(final int a, final int b, final Vector2i out)
    {
        return out.set(this.get(a), this.get(b));
    }

    public Vector2i swizzle(final int a, final int b)
    {
        return this.swizzle(a, b, new Vector2i());
    }

    public Vector3i swizzle(final int a, final int b, final int c, final Vector3i out)
    {
        return out.set(this.get(a), this.get(b), this.get(c));
    }

    public Vector3i swizzle(final int a, final int b, final int c)
    {
        return this.swizzle(a, b, c, new Vector3i());
    }

    public Vector4i swizzle(final int a, final int b, final int c, final int d, final Vector4i out)
    {
        return out.set(this.get(a), this.get(b), this.get(c), this.get(d));
    }

    public Vector4i swizzle(final int a, final int b, final int c, final int d)
    {
        return this.swizzle(a, b, c, d, new Vector4i());
    }

    public void into(final int[] arr, final int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
    }

    public void into(final IntBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
    }

    public Vector2i toVector2(final Vector2i out)
    {
        return out.set(this.x, this.y);
    }

    public Vector2i toVector2()
    {
        return this.toVector2(new Vector2i());
    }

    @Override
    public Vector3i clone()
    {
        return new Vector3i(this.x, this.y, this.z);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
