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

public class Vector4i
{
    public int w;
    public int x;
    public int y;
    public int z;

    public Vector4i()
    {
        // 0
    }

    public Vector4i(final int x, final int y, final int z, final int w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4i(final int xyzw)
    {
        this.x = xyzw;
        this.y = xyzw;
        this.z = xyzw;
        this.w = xyzw;
    }

    public Vector4i(final Vector2i v, final int z, final int w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
    }

    public Vector4i(final Vector3i v, final int w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public static Vector4i of(final int x, final int y, final int z, final int w)
    {
        return new Vector4i(x, y, z, w);
    }

    public static Vector4i of(final Vector2i v, final int z, final int w)
    {
        return new Vector4i(v, z, w);
    }

    public static Vector4i of(final Vector3i v, final int w)
    {
        return new Vector4i(v, w);
    }

    public Vector4i set(final int x, final int y, final int z, final int w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4i set(final int xyzw)
    {
        this.x = xyzw;
        this.y = xyzw;
        this.z = xyzw;
        this.w = xyzw;
        return this;
    }

    public Vector4i set(final Vector2i v, final int z, final int w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4i set(final Vector3i v, final int w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
        return this;
    }

    public Vector4i set(final Vector4i v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public Vector4i setIdx(final int index, final int value)
    {
        if (index == 0)
        {
            this.x = value;
        }
        else if (index == 1)
        {
            this.y = value;
        }
        else if (index == 2)
        {
            this.z = value;
        }
        else
        {
            this.w = value;
        }
        return this;
    }

    public int get(final int index)
    {
        if (index == 0) return this.x;
        if (index == 1) return this.y;
        if (index == 2) return this.z;
        return this.w;
    }

    public Vector4i add(final Vector4i v)
    {
        this.w += v.w;
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector4i sub(final Vector4i v)
    {
        this.w -= v.w;
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector4i mul(final Vector4i v)
    {
        this.w *= v.w;
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector4i div(final Vector4i v)
    {
        this.w /= v.w;
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector4i min(final Vector4i other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        this.w = Math.min(this.w, other.w);
        return this;
    }

    public Vector4i max(final Vector4i other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        this.w = Math.max(this.w, other.w);
        return this;
    }

    public Vector4i clamp(final Vector4i min, final Vector4i max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        this.w = NMath.clamp(this.w, min.w, max.w);
        return this;
    }

    public Vector2d swizzle(final int a, final int b)
    {
        return Vector2d.of(this.get(a), this.get(b));
    }

    public Vector3d swizzle(final int a, final int b, final int c)
    {
        return Vector3d.of(this.get(a), this.get(b), this.get(c));
    }

    public Vector4i swizzle(final int a, final int b, final int c, final int d)
    {
        return Vector4i.of(this.get(a), this.get(b), this.get(c), this.get(d));
    }

    public void into(final int[] arr, final int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
        arr[offset + 3] = this.w;
    }

    public void into(final IntBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
        buffer.put(offset + 3, this.w);
    }

    public Vector2i asVector2()
    {
        return Vector2i.of(this.x, this.y);
    }

    public Vector3i asVector3()
    {
        return Vector3i.of(this.x, this.y, this.z);
    }

    @Override
    public Vector4i clone()
    {
        return new Vector4i(this.x, this.y, this.z, this.w);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
}
