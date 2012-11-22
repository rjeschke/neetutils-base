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

public class Vector3d
{
    public double x;
    public double y;
    public double z;

    public Vector3d()
    {
        // 0
    }
    
    public Vector3d(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3d(double xyz)
    {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }
    
    public Vector3d(Vector3d v, double z)
    {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }
    
    public Vector3d(NColor color)
    {
        this.x = color.r;
        this.y = color.g;
        this.z = color.b;
    }
    
    public Vector3d scale(double f)
    {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }
    
    public Vector3d add(Vector3d v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }
    
    public Vector3d add(Vector3d v, double scale)
    {
        this.x += v.x * scale;
        this.y += v.y * scale;
        this.z += v.z * scale;
        return this;
    }
    
    public Vector3d sub(Vector3d v)
    {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }
    
    public Vector3d sub(Vector3d v, double scale)
    {
        this.x -= v.x * scale;
        this.y -= v.y * scale;
        this.z -= v.z * scale;
        return this;
    }
    
    public Vector3d mul(Vector3d v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }
    
    public Vector3d mul(Vector3d v, double scale)
    {
        this.x *= v.x * scale;
        this.y *= v.y * scale;
        this.z *= v.z * scale;
        return this;
    }
    
    public Vector3d lerp(Vector3d v, double f)
    {
        this.x += (v.x - this.x) * f;
        this.y += (v.y - this.y) * f;
        this.z += (v.z - this.z) * f;
        return this;
    }
    
    public double dot(Vector3d v)
    {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }
    
    public Vector3d cross(Vector3d v)
    {
        final double x = this.y * v.z - this.z * v.y;
        final double z = this.x * v.y - this.y * v.x;
        final double y = this.z * v.x - this.x * v.z;
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        return this;
    }

    public Vector3d normalize()
    {
        double len = this.x * this.x + this.y * this.y + this.z * this.z;
        if(len != 0)
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

    public void intoArray(double[] arr, int offset)
    {
        arr[offset] = this.x;
        arr[offset + 1] = this.y;
        arr[offset + 2] = this.z;
    }

    public NColor toNColor()
    {
        return new NColor((float)this.x, (float)this.y, (float)this.z);
    }
    
    public NColor toNColor(float alpha)
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
