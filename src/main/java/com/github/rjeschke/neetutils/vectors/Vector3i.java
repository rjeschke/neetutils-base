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
        if (index == 0) return this.x;
        if (index == 1) return this.y;
        return this.z;
    }

    public Vector3i add(final Vector3i v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3i sub(final Vector3i v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3i mul(final Vector3i v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3i div(final Vector3i v)
    {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector3i min(final Vector3i other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        return this;
    }

    public Vector3i max(final Vector3i other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        return this;
    }

    public Vector3i clamp(final Vector3i min, final Vector3i max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        return this;
    }

    public Vector2i swizzle(final int a, final int b)
    {
        return Vector2i.of(this.get(a), this.get(b));
    }

    public Vector3i swizzle(final int a, final int b, final int c)
    {
        return Vector3i.of(this.get(a), this.get(b), this.get(c));
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
    }

    public void into(final IntBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
    }

    public Vector2i asVector2()
    {
        return Vector2i.of(this.x, this.y);
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
