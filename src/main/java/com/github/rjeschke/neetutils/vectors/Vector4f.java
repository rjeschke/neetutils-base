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

public class Vector4f
{
    public float w;
    public float x;
    public float y;
    public float z;

    public Vector4f()
    {
        // 0
    }

    public Vector4f(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(final float xyzw)
    {
        this.x = xyzw;
        this.y = xyzw;
        this.z = xyzw;
        this.w = xyzw;
    }

    public Vector4f(final Vector2f v, final float z, final float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(final Vector3f v, final float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vector4f(final NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
        this.w = color.a;
    }

    public static Vector4f of(final float x, final float y, final float z, final float w)
    {
        return new Vector4f(x, y, z, w);
    }

    public static Vector4f of(final Vector2f v, final float z, final float w)
    {
        return new Vector4f(v, z, w);
    }

    public static Vector4f of(final Vector3f v, final float w)
    {
        return new Vector4f(v, w);
    }

    public Vector4f set(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4f set(final Vector2f v, final float z, final float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4f set(final Vector3f v, final float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
        return this;
    }

    public Vector4f set(final Vector4f v)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    public Vector4f set(final int index, final float value)
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

    public float get(final int index)
    {
        if (index == 0) return this.x;
        if (index == 1) return this.y;
        if (index == 2) return this.z;
        return this.w;
    }

    public Vector4f scale(final float f)
    {
        this.w *= f;
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Vector4f add(final Vector4f v)
    {
        this.w += v.w;
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector4f add(final Vector4f v, final float scale)
    {
        this.w += v.w * scale;
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }

    public Vector4f sub(final Vector4f v)
    {
        this.w -= v.w;
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public Vector4f sub(final Vector4f v, final float scale)
    {
        this.w -= v.w * scale;
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }

    public Vector4f mul(final Vector4f v)
    {
        this.w *= v.w;
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    public Vector4f mul(final Vector4f v, final float scale)
    {
        this.w *= v.w * scale;
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }

    public Vector4f div(final Vector4f v)
    {
        this.w /= v.w;
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
        return this;
    }

    public Vector4f div(final Vector4f v, final float scale)
    {
        this.w /= v.w * scale;
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        this.z /= v.z * scale;
        return this;
    }

    public Vector4f lerp(final Vector4f v, final float f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        this.w += (v.w - this.w) * f;
        return this;
    }

    public float dot(final Vector4f v)
    {
        return this.w * v.w + this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector4f min(final Vector4f other)
    {
        this.x = Math.min(this.x, other.x);
        this.y = Math.min(this.y, other.y);
        this.z = Math.min(this.z, other.z);
        this.w = Math.min(this.w, other.w);
        return this;
    }

    public Vector4f max(final Vector4f other)
    {
        this.x = Math.max(this.x, other.x);
        this.y = Math.max(this.y, other.y);
        this.z = Math.max(this.z, other.z);
        this.w = Math.max(this.w, other.w);
        return this;
    }

    public Vector4f clamp(final Vector4f min, final Vector4f max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        this.z = NMath.clamp(this.z, min.z, max.z);
        this.w = NMath.clamp(this.w, min.w, max.w);
        return this;
    }

    public Vector4f normalize()
    {
        float len = this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
        if (len != 0)
        {
            len = (float)(1.0 / Math.sqrt(len));
            this.w *= len;
            this.x *= len;
            this.y *= len;
            this.z *= len;
        }
        return this;
    }

    public Vector4f negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }

    public float length()
    {
        return (float)Math.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
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
        arr[offset + 3] = this.w;
    }

    public void into(final FloatBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
        buffer.put(offset + 2, this.z);
        buffer.put(offset + 3, this.w);
    }

    public Vector2f asVector2()
    {
        return Vector2f.of(this.x, this.y);
    }

    public Vector3f asVector3()
    {
        return Vector3f.of(this.x, this.y, this.z);
    }

    public Vector3f asVector3DivW(final Vector3f v)
    {
        v.x = this.x / this.w;
        v.y = this.y / this.w;
        v.z = this.z / this.w;
        return v;
    }

    public Vector3f asVector3DivW()
    {
        return this.asVector3DivW(new Vector3f());
    }

    public NColor asNColor()
    {
        return new NColor(this.w, this.x, this.y, this.z);
    }

    @Override
    public Vector4f clone()
    {
        return new Vector4f(this.x, this.y, this.z, this.w);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
    }
}
