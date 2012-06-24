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

public class Vector4f
{
    public float w;
    public float x;
    public float y;
    public float z;

    public Vector4f(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4f(Vector2f v, float z, float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
        this.w = w;
    }
    
    public Vector4f(Vector3f v, float w)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vector4f(NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
        this.w = color.a;
    }
    
    public Vector4f scale(float f)
    {
        this.w *= f;
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }
    
    public Vector4f add(Vector4f v)
    {
        this.w += v.w;
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }
    
    public Vector4f add(Vector4f v, float scale)
    {
        this.w += v.w * scale;
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }
    
    public Vector4f sub(Vector4f v)
    {
        this.w -= v.w;
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }
    
    public Vector4f sub(Vector4f v, float scale)
    {
        this.w -= v.w * scale;
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }
    
    public Vector4f mul(Vector4f v)
    {
        this.w *= v.w;
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }
    
    public Vector4f mul(Vector4f v, float scale)
    {
        this.w *= v.w * scale;
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }
    
    public Vector4f lerp(Vector4f v, float f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        this.w += (v.w - this.w) * f;
        return this;
    }
    
    public float dot(Vector4f v)
    {
        return this.w * v.w + this.x * v.x + this.y * v.y + this.z * v.z;
    }
    
    public Vector4f normalize()
    {
        float len = this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
        if(len != 0)
        {
            len = 1.f / (float)Math.sqrt(len);
            this.w *= len;
            this.x *= len;
            this.y *= len;
            this.z *= len;
        }
        return this;
    }
    
    public float length()
    {
        return (float)Math.sqrt(this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public void intoArray(float[] arr, int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
        arr[offset + 3] = this.w;
    }
    
    public NColor toNColor()
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
