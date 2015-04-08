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

import com.github.rjeschke.neetutils.math.NMath;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class Math3D
{
    private Math3D()
    { /* */
    }

    public final static float radians(final float x)
    {
        return (float)(Math.PI / 180.0) * x;
    }

    public final static float radians(final double x)
    {
        return (float)(x * Math.PI / 180.0);
    }

    public final static float degrees(final float x)
    {
        return (float)(180.0 / Math.PI) * x;
    }

    public final static float sin(final float x)
    {
        return (float)Math.sin(x);
    }

    public final static float cos(final float x)
    {
        return (float)Math.cos(x);
    }

    public final static float tan(final float x)
    {
        return (float)Math.tan(x);
    }

    public final static float asin(final float x)
    {
        return (float)Math.asin(x);
    }

    public final static float acos(final float x)
    {
        return (float)Math.acos(x);
    }

    public final static float atan(final float x)
    {
        return (float)Math.atan(x);
    }

    public final static float atan(final float y, final float x)
    {
        return (float)Math.atan2(y, x);
    }

    public final static float pow(final float x, final float y)
    {
        return (float)Math.pow(x, y);
    }

    public final static float exp(final float x)
    {
        return (float)Math.exp(x);
    }

    public final static float log(final float x)
    {
        return (float)Math.log(x);
    }

    public final static float exp2(final float x)
    {
        return (float)Math.pow(2, x);
    }

    public final static float log2(final float x)
    {
        return (float)(Math.log(x) / Math.log(2));
    }

    public final static float sqrt(final float x)
    {
        return (float)Math.sqrt(x);
    }

    public final static float inversesqrt(final float x)
    {
        return (float)(1.0 / Math.sqrt(x));
    }

    public final static float abs(final float x)
    {
        return Math.abs(x);
    }

    public final static float sign(final float x)
    {
        return x < 0 ? -1.0f : x > 0 ? 1.0f : 0.0f;
    }

    public final static float floor(final float x)
    {
        return (float)Math.floor(x);
    }

    public final static float ceil(final float x)
    {
        return (float)Math.ceil(x);
    }

    public final static float mod(final float x, final float y)
    {
        return (float)(x - y * Math.floor(x / y));
    }

    public final static Vector3f reflect(final Vector3f i, final Vector3f n)
    {
        return i.clone().sub(n, 2.0f * n.dot(i));
    }

    public final static Vector3d reflect(final Vector3d i, final Vector3d n)
    {
        return i.clone().sub(n, 2.0 * n.dot(i));
    }

    public final static Vector3f refract(final Vector3f i, final Vector3f n, final float eta)
    {
        final float d = n.dot(i);
        final float k = 1.0f - eta * eta * (1.0f - d * d);
        if (k < 0)
        {
            return new Vector3f(0, 0, 0);
        }
        return i.clone().scale(eta).sub(n, eta * d + Math3D.sqrt(k));
    }

    public final static Vector3d refract(final Vector3d i, final Vector3d n, final double eta)
    {
        final double d = n.dot(i);
        final double k = 1.0 - eta * eta * (1.0 - d * d);
        if (k < 0)
        {
            return new Vector3d(0, 0, 0);
        }
        return i.clone().scale(eta).sub(n, eta * d + Math.sqrt(k));
    }

    public final static Matrix4x4f matrixLookAtLH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        final float[] ret = new float[16];
        final Vector3f zaxis = lookat.clone().sub(pos).normalize();
        final Vector3f xaxis = up.clone().cross(zaxis).normalize();
        final Vector3f yaxis = zaxis.clone().cross(xaxis).normalize();

        ret[Matrix4x4f.M00] = xaxis.x;
        ret[Matrix4x4f.M01] = xaxis.y;
        ret[Matrix4x4f.M02] = xaxis.z;
        ret[Matrix4x4f.M03] = -xaxis.dot(pos);

        ret[Matrix4x4f.M10] = yaxis.x;
        ret[Matrix4x4f.M11] = yaxis.y;
        ret[Matrix4x4f.M12] = yaxis.z;
        ret[Matrix4x4f.M13] = -yaxis.dot(pos);

        ret[Matrix4x4f.M20] = zaxis.x;
        ret[Matrix4x4f.M21] = zaxis.y;
        ret[Matrix4x4f.M22] = zaxis.z;
        ret[Matrix4x4f.M23] = -zaxis.dot(pos);

        ret[Matrix4x4f.M33] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixLookAtRH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        final float[] ret = new float[16];
        final Vector3f zaxis = pos.clone().sub(lookat).normalize();
        final Vector3f xaxis = up.clone().cross(zaxis).normalize();
        final Vector3f yaxis = zaxis.clone().cross(xaxis);

        ret[Matrix4x4f.M00] = xaxis.x;
        ret[Matrix4x4f.M01] = xaxis.y;
        ret[Matrix4x4f.M02] = xaxis.z;
        ret[Matrix4x4f.M03] = -xaxis.dot(pos);

        ret[Matrix4x4f.M10] = yaxis.x;
        ret[Matrix4x4f.M11] = yaxis.y;
        ret[Matrix4x4f.M12] = yaxis.z;
        ret[Matrix4x4f.M13] = -yaxis.dot(pos);

        ret[Matrix4x4f.M20] = zaxis.x;
        ret[Matrix4x4f.M21] = zaxis.y;
        ret[Matrix4x4f.M22] = zaxis.z;
        ret[Matrix4x4f.M23] = -zaxis.dot(pos);

        ret[Matrix4x4f.M33] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixProjectionLH(final float fov, final float aspect, final float near_z,
            final float far_z)
    {
        final float[] ret = new float[16];
        final float yscale = 1.0f / (float)Math.tan(fov * 0.5f), t = 1.0f / (far_z - near_z);
        final float xscale = yscale / aspect;

        ret[Matrix4x4f.M00] = xscale;

        ret[Matrix4x4f.M11] = yscale;

        ret[Matrix4x4f.M22] = far_z * t;
        ret[Matrix4x4f.M23] = (-near_z * far_z) * t;

        ret[Matrix4x4f.M32] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixProjectionRH(final float fov, final float aspect, final float near_z,
            final float far_z)
    {
        final float[] ret = new float[16];
        final float yscale = 1.0f / (float)Math.tan(fov * 0.5f), t = 1.0f / (far_z - near_z);
        final float xscale = yscale / aspect;

        ret[Matrix4x4f.M00] = xscale;

        ret[Matrix4x4f.M11] = yscale;

        ret[Matrix4x4f.M22] = far_z * t;
        ret[Matrix4x4f.M23] = (-near_z * far_z) * t;

        ret[Matrix4x4f.M32] = -1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixTranslate(final float x, final float y, final float z)
    {
        final float[] ret = new float[16];
        ret[Matrix4x4f.M00] = 1.0f;
        ret[Matrix4x4f.M11] = 1.0f;
        ret[Matrix4x4f.M22] = 1.0f;
        ret[Matrix4x4f.M33] = 1.0f;

        ret[Matrix4x4f.M03] = x;
        ret[Matrix4x4f.M13] = y;
        ret[Matrix4x4f.M23] = z;
        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixScale(final float x, final float y, final float z)
    {
        final float[] ret = new float[16];
        ret[Matrix4x4f.M00] = x;
        ret[Matrix4x4f.M11] = y;
        ret[Matrix4x4f.M22] = z;
        ret[Matrix4x4f.M33] = 1.0f;
        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixUniformScale(final float s)
    {
        final float[] ret = new float[16];
        ret[Matrix4x4f.M00] = s;
        ret[Matrix4x4f.M11] = s;
        ret[Matrix4x4f.M22] = s;
        ret[Matrix4x4f.M33] = 1.0f;
        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixRotateX(final float a)
    {
        final float[] ret = new float[16];
        final float sn = (float)Math.sin(a);
        final float cs = (float)Math.cos(a);

        ret[Matrix4x4f.M11] = cs;
        ret[Matrix4x4f.M21] = sn;
        ret[Matrix4x4f.M12] = -sn;
        ret[Matrix4x4f.M22] = cs;

        ret[Matrix4x4f.M00] = 1.0f;
        ret[Matrix4x4f.M33] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixRotateY(final float a)
    {
        final float[] ret = new float[16];
        final float sn = (float)Math.sin(a);
        final float cs = (float)Math.cos(a);

        ret[Matrix4x4f.M00] = cs;
        ret[Matrix4x4f.M20] = -sn;
        ret[Matrix4x4f.M02] = sn;
        ret[Matrix4x4f.M22] = cs;

        ret[Matrix4x4f.M11] = 1.0f;
        ret[Matrix4x4f.M33] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Matrix4x4f matrixRotateZ(final float a)
    {
        final float[] ret = new float[16];
        final float sn = (float)Math.sin(a);
        final float cs = (float)Math.cos(a);

        ret[Matrix4x4f.M00] = cs;
        ret[Matrix4x4f.M10] = sn;
        ret[Matrix4x4f.M01] = -sn;
        ret[Matrix4x4f.M11] = cs;

        ret[Matrix4x4f.M22] = 1.0f;
        ret[Matrix4x4f.M33] = 1.0f;

        return new Matrix4x4f(ret);
    }

    public final static Vector2d min(final Vector2d a, final Vector2d b, final Vector2d out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    public final static Vector3d min(final Vector3d a, final Vector3d b, final Vector3d out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }

    public final static Vector4d min(final Vector4d a, final Vector4d b, final Vector4d out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.min(a.w, b.w));
    }

    public final static Vector2f min(final Vector2f a, final Vector2f b, final Vector2f out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    public final static Vector3f min(final Vector3f a, final Vector3f b, final Vector3f out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }

    public final static Vector4f min(final Vector4f a, final Vector4f b, final Vector4f out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.min(a.w, b.w));
    }

    public final static Vector2i min(final Vector2i a, final Vector2i b, final Vector2i out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    public final static Vector3i min(final Vector3i a, final Vector3i b, final Vector3i out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }

    public final static Vector4i min(final Vector4i a, final Vector4i b, final Vector4i out)
    {
        return out.set(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.min(a.w, b.w));
    }

    public final static Vector2d max(final Vector2d a, final Vector2d b, final Vector2d out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    public final static Vector3d max(final Vector3d a, final Vector3d b, final Vector3d out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }

    public final static Vector4d max(final Vector4d a, final Vector4d b, final Vector4d out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z), Math.max(a.w, b.w));
    }

    public final static Vector2f max(final Vector2f a, final Vector2f b, final Vector2f out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    public final static Vector3f max(final Vector3f a, final Vector3f b, final Vector3f out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }

    public final static Vector4f max(final Vector4f a, final Vector4f b, final Vector4f out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z), Math.max(a.w, b.w));
    }

    public final static Vector2i max(final Vector2i a, final Vector2i b, final Vector2i out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    public final static Vector3i max(final Vector3i a, final Vector3i b, final Vector3i out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }

    public final static Vector4i max(final Vector4i a, final Vector4i b, final Vector4i out)
    {
        return out.set(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z), Math.max(a.w, b.w));
    }

    public final static Vector2d clamp(final Vector2d x, final Vector2d min, final Vector2d max, final Vector2d out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y));
    }

    public final static Vector3d clamp(final Vector3d x, final Vector3d min, final Vector3d max, final Vector3d out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z));
    }

    public final static Vector4d clamp(final Vector4d x, final Vector4d min, final Vector4d max, final Vector4d out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z),
                NMath.clamp(x.w, min.w, max.w));
    }

    public final static Vector2f clamp(final Vector2f x, final Vector2f min, final Vector2f max, final Vector2f out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y));
    }

    public final static Vector3f clamp(final Vector3f x, final Vector3f min, final Vector3f max, final Vector3f out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z));
    }

    public final static Vector4f clamp(final Vector4f x, final Vector4f min, final Vector4f max, final Vector4f out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z),
                NMath.clamp(x.w, min.w, max.w));
    }

    public final static Vector2i clamp(final Vector2i x, final Vector2i min, final Vector2i max, final Vector2i out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y));
    }

    public final static Vector3i clamp(final Vector3i x, final Vector3i min, final Vector3i max, final Vector3i out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z));
    }

    public final static Vector4i clamp(final Vector4i x, final Vector4i min, final Vector4i max, final Vector4i out)
    {
        return out.set(NMath.clamp(x.x, min.x, max.x), NMath.clamp(x.y, min.y, max.y), NMath.clamp(x.z, min.z, max.z),
                NMath.clamp(x.w, min.w, max.w));
    }
}
