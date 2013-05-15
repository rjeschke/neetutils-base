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

import java.nio.FloatBuffer;

import com.github.rjeschke.neetutils.graphics.NColor;
import com.github.rjeschke.neetutils.math.NMath;

public class Vector3f
{
    public float x;
    public float y;
    public float z;

    public Vector3f()
    {
        // 0
    }

    public Vector3f(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(final float xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    public Vector3f(final Vector2f v, final float z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    public Vector3f(final Vector3f v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3f(final NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
    }

    public static Vector3f of(final float x, final float y, final float z)
    {
        return new Vector3f(x, y, z);
    }

    public static Vector3f of(final Vector2f v, final float z)
    {
        return new Vector3f(v, z);
    }

    public static Vector3f of(final Vector3f v)
    {
        return new Vector3f(v);
    }

    public Vector3f set(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(final Vector2f v, final float z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        return this;
    }

    public Vector3f set(final Vector3f v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3f set(final Vector4f v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public Vector3f setIdx(final int index, final float value)
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

    public float get(final int index)
    {
        if (index == 0) return this.x;
        if (index == 1) return this.y;
        return this.z;
    }

    public Vector3f scale(final float f)
    {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector3f add(final Vector3f v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3f add(final Vector3f v, final float scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }

    public Vector3f sub(final Vector3f v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector3f sub(final Vector3f v, final float scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }

    public Vector3f mul(final Vector3f v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector3f mul(final Vector3f v, final float scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }

    public Vector3f div(final Vector3f v)
    {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector3f div(final Vector3f v, final float scale)
    {
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        this.z /= v.z * scale;
        return this;
    }

    public Vector3f lerp(final Vector3f v, final float f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        return this;
    }

    public float dot(final Vector3f v)
    {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3f cross(final Vector3f v)
    {
        final float x = this.y * v.z - this.z * v.y;
        final float z = this.x * v.y - this.y * v.x;
        final float y = this.z * v.x - this.x * v.z;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3f min(final Vector3f other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        return this;
    }

    public Vector3f max(final Vector3f other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        return this;
    }

    public Vector3f clamp(final Vector3f min, final Vector3f max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        return this;
    }

    public Vector3f normalize()
    {
        float len = this.x * this.x + this.y * this.y + this.z * this.z;
        if (len != 0)
        {
            len = (float)(1.0 / Math.sqrt(len));
            this.x *= len;
            this.y *= len;
            this.z *= len;
        }
        return this;
    }

    public Vector3f negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public float length()
    {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector2f swizzle(final int a, final int b)
    {
        return Vector2f.of(this.get(a), this.get(b));
    }

    public Vector3f swizzle(final int a, final int b, final int c)
    {
        return Vector3f.of(this.get(a), this.get(b), this.get(c));
    }

    public Vector4f swizzle(final int a, final int b, final int c, final int d)
    {
        return Vector4f.of(this.get(a), this.get(b), this.get(c), this.get(d));
    }

    public void into(final float[] arr, final int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
    }

    public void into(final FloatBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
    }

    public Vector2f asVector2()
    {
        return Vector2f.of(this.x, this.y);
    }

    public NColor asNColor()
    {
        return new NColor(this.x, this.y, this.z);
    }

    public NColor asNColor(final float alpha)
    {
        return new NColor(alpha, this.x, this.y, this.z);
    }

    @Override
    public Vector3f clone()
    {
        return new Vector3f(this.x, this.y, this.z);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
