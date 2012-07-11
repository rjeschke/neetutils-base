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

public class Vector3f
{
    public float x;
    public float y;
    public float z;

    public Vector3f()
    {
        // 0
    }
    
    public Vector3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3f(float xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }
    
    public Vector3f(Vector3f v, float z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }
    
    public Vector3f(NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
    }
    
    public Vector3f scale(float f)
    {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }
    
    public Vector3f add(Vector3f v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }
    
    public Vector3f add(Vector3f v, float scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }
    
    public Vector3f sub(Vector3f v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }
    
    public Vector3f sub(Vector3f v, float scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }
    
    public Vector3f mul(Vector3f v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }
    
    public Vector3f mul(Vector3f v, float scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }
    
    public Vector3f lerp(Vector3f v, float f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        return this;
    }
    
    public float dot(Vector3f v)
    {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }
    
    public Vector3f cross(Vector3f v)
    {
        final float x = this.y * v.z - this.z * v.y;
        final float z = this.x * v.y - this.y * v.x;
        final float y = this.z * v.x - this.x * v.z;
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        return this;
    }

    public Vector3f normalize()
    {
        float len = this.x * this.x + this.y * this.y + this.z * this.z;
        if(len != 0)
        {
            len = 1.f / (float)Math.sqrt(len);
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

    public void intoArray(float[] arr, int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
    }

    public NColor toNColor()
    {
        return new NColor(this.x, this.y, this.z);
    }
    
    public NColor toNColor(float alpha)
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
