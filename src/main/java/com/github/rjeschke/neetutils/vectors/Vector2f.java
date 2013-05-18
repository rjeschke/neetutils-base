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

import com.github.rjeschke.neetutils.math.NMath;

public class Vector2f
{
    public float x;
    public float y;

    public Vector2f()
    {
        // default
    }

    public Vector2f(final float x, final float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2f(final float xy)
    {
        this.x = xy;
        this.y = xy;
    }

    public Vector2f(final Vector2f v)
    {
        this.x = v.x;
        this.y = v.y;
    }

    public static Vector2f of(final float x, final float y)
    {
        return new Vector2f(x, y);
    }

    public static Vector2f of(final Vector2f v)
    {
        return new Vector2f(v);
    }

    public Vector2f set(final float x, final float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(final float xy)
    {
        this.x = xy;
        this.y = xy;
        return this;
    }

    public Vector2f set(final FloatBuffer buffer, final int offset)
    {
        this.x = buffer.get(offset);
        this.y = buffer.get(offset + 1);
        return this;
    }

    public Vector2f set(final Vector2f v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f set(final Vector3f v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f set(final Vector4f v)
    {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f setIdx(final int index, final float value)
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

    public float get(final int index)
    {
        if (index == 0) return this.x;
        return this.y;
    }

    public Vector2f scale(final float f)
    {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Vector2f add(final Vector2f v)
    {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2f add(final Vector2f v, final float scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        return this;
    }

    public Vector2f sub(final Vector2f v)
    {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2f sub(final Vector2f v, final float scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        return this;
    }

    public Vector2f mul(final Vector2f v)
    {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    public Vector2f mul(final Vector2f v, final float scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        return this;
    }

    public Vector2f div(final Vector2f v)
    {
        this.x /= v.x;
        this.y /= v.y;
        return this;
    }

    public Vector2f div(final Vector2f v, final float scale)
    {
        this.x /= v.x * scale;
        this.y /= v.y * scale;
        return this;
    }

    public Vector2f lerp(final Vector2f v, final float f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        return this;
    }

    public float dot(final Vector2f v)
    {
        return this.x * v.x + this.y * v.y;
    }

    public Vector2f min(final Vector2f v)
    {
        this.x = Math.min(this.x, v.x);
        this.y = Math.min(this.y, v.y);
        return this;
    }

    public float min()
    {
        return Math.min(this.x, this.y);
    }

    public Vector2f max(final Vector2f v)
    {
        this.x = Math.max(this.x, v.x);
        this.y = Math.max(this.y, v.y);
        return this;
    }

    public float max()
    {
        return Math.min(this.x, this.y);
    }

    public Vector2f clamp(final Vector2f min, final Vector2f max)
    {
        this.x = NMath.clamp(this.x, min.x, max.x);
        this.y = NMath.clamp(this.y, min.y, max.y);
        return this;
    }

    public Vector2f abs()
    {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }

    public Vector2f pow(final double value)
    {
        this.x = (float)Math.pow(this.x, value);
        this.y = (float)Math.pow(this.y, value);
        return this;
    }

    public Vector2f normalize()
    {
        float len = this.x * this.x + this.y * this.y;
        if (len != 0)
        {
            len = (float)(1.0 / Math.sqrt(len));
            this.x *= len;
            this.y *= len;
        }
        return this;
    }

    public Vector2f negate()
    {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public float length()
    {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y);
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
    }

    public void into(final FloatBuffer buffer, final int offset)
    {
        buffer.put(offset, this.x);
        buffer.put(offset + 1, this.y);
    }

    @Override
    public Vector2f clone()
    {
        return new Vector2f(this.x, this.y);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }
}
