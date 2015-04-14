/*
 * Copyright (C) 2015 René Jeschke <rene_jeschke@yahoo.de>
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

import java.util.Arrays;

import com.github.rjeschke.neetutils.vectors.Vector3f;
import com.github.rjeschke.neetutils.vectors.Vector4f;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class Matrix4x4f
{
    // column major order
    public final static int M00 = 0 * 4 + 0;
    public final static int M01 = 1 * 4 + 0;
    public final static int M02 = 2 * 4 + 0;
    public final static int M03 = 3 * 4 + 0;
    public final static int M10 = 0 * 4 + 1;
    public final static int M11 = 1 * 4 + 1;
    public final static int M12 = 2 * 4 + 1;
    public final static int M13 = 3 * 4 + 1;
    public final static int M20 = 0 * 4 + 2;
    public final static int M21 = 1 * 4 + 2;
    public final static int M22 = 2 * 4 + 2;
    public final static int M23 = 3 * 4 + 2;
    public final static int M30 = 0 * 4 + 3;
    public final static int M31 = 1 * 4 + 3;
    public final static int M32 = 2 * 4 + 3;
    public final static int M33 = 3 * 4 + 3;
    public final float[]    m   = new float[16];

    public Matrix4x4f()
    {
        this.m[M00] = 1;
        this.m[M11] = 1;
        this.m[M22] = 1;
        this.m[M33] = 1;
    }

    public Matrix4x4f(final float[] m)
    {
        System.arraycopy(m, 0, this.m, 0, 16);
    }

    public Matrix4x4f(final float[] m, final int offset)
    {
        System.arraycopy(m, offset, this.m, 0, 16);
    }

    public Matrix4x4f(final Matrix4x4f m)
    {
        System.arraycopy(m.m, 0, this.m, 0, 16);
    }

    public Matrix4x4f preConcat(final Matrix4x4f other)
    {
        MatrixMathF.multiplyInPlace(this.m, other.m, this.m);
        return this;
    }

    public Matrix4x4f postConcat(final Matrix4x4f other)
    {
        MatrixMathF.multiplyInPlace(other.m, this.m, this.m);
        return this;
    }

    public Matrix4x4f set(final float[] arr, final int offs)
    {
        System.arraycopy(arr, offs, this.m, 0, 16);
        return this;
    }

    public Matrix4x4f set(final Matrix4x4f other)
    {
        return this.set(other.m, 0);
    }

    public Matrix4x4f set(final float[] arr)
    {
        return this.set(arr, 0);
    }

    public Matrix4x4f setTranslate(final float x, final float y, final float z)
    {
        return MatrixMathF.translate(x, y, z, this);
    }

    public Matrix4x4f preTranslate(final float x, final float y, final float z)
    {
        return this.preConcat(MatrixMathF.translate(x, y, z, STATE.get().temp));
    }

    public Matrix4x4f postTranslate(final float x, final float y, final float z)
    {
        return this.postConcat(MatrixMathF.translate(x, y, z, STATE.get().temp));
    }

    public Matrix4x4f setScale(final float x, final float y, final float z)
    {
        return MatrixMathF.scale(x, y, z, this);
    }

    public Matrix4x4f preScale(final float x, final float y, final float z)
    {
        return this.preConcat(MatrixMathF.scale(x, y, z, STATE.get().temp));
    }

    public Matrix4x4f postScale(final float x, final float y, final float z)
    {
        return this.postConcat(MatrixMathF.scale(x, y, z, STATE.get().temp));
    }

    public Matrix4x4f setRotateX(final float radians)
    {
        return MatrixMathF.rotateX(radians, this);
    }

    public Matrix4x4f preRotateX(final float radians)
    {
        return this.preConcat(MatrixMathF.rotateX(radians, STATE.get().temp));
    }

    public Matrix4x4f postRotateX(final float radians)
    {
        return this.postConcat(MatrixMathF.rotateX(radians, STATE.get().temp));
    }

    public Matrix4x4f setRotateY(final float radians)
    {
        return MatrixMathF.rotateY(radians, this);
    }

    public Matrix4x4f preRotateY(final float radians)
    {
        return this.preConcat(MatrixMathF.rotateY(radians, STATE.get().temp));
    }

    public Matrix4x4f postRotateY(final float radians)
    {
        return this.postConcat(MatrixMathF.rotateY(radians, STATE.get().temp));
    }

    public Matrix4x4f setRotateZ(final float radians)
    {
        return MatrixMathF.rotateZ(radians, this);
    }

    public Matrix4x4f preRotateZ(final float radians)
    {
        return this.preConcat(MatrixMathF.rotateZ(radians, STATE.get().temp));
    }

    public Matrix4x4f postRotateZ(final float radians)
    {
        return this.postConcat(MatrixMathF.rotateZ(radians, STATE.get().temp));
    }

    public Matrix4x4f setLookAtLH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return MatrixMathF.lookAtLH(pos, lookat, up, this);
    }

    public Matrix4x4f preLookAtLH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return this.preConcat(MatrixMathF.lookAtLH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4f postLookAtLH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return this.postConcat(MatrixMathF.lookAtLH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4f setLookAtRH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return MatrixMathF.lookAtRH(pos, lookat, up, this);
    }

    public Matrix4x4f preLookAtRH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return this.preConcat(MatrixMathF.lookAtRH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4f postLookAtRH(final Vector3f pos, final Vector3f lookat, final Vector3f up)
    {
        return this.postConcat(MatrixMathF.lookAtRH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4f setProjectionLH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return MatrixMathF.projectionLH(fovRadians, aspectRatio, near_z, far_z, this);
    }

    public Matrix4x4f preProjectionLH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return this.preConcat(MatrixMathF.projectionLH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4f postProjectionLH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return this.postConcat(MatrixMathF.projectionLH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4f setProjectionRH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return MatrixMathF.projectionRH(fovRadians, aspectRatio, near_z, far_z, this);
    }

    public Matrix4x4f preProjectionRH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return this.preConcat(MatrixMathF.projectionRH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4f postProjectionRH(final float fovRadians, final float aspectRatio, final float near_z,
            final float far_z)
    {
        return this.postConcat(MatrixMathF.projectionRH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4f setIdentity()
    {
        Arrays.fill(this.m, 0);
        this.m[M00] = 1;
        this.m[M11] = 1;
        this.m[M22] = 1;
        this.m[M33] = 1;
        return this;
    }

    public Matrix4x4f invert()
    {
        MatrixMathF.invert(this.m, this.m);
        return this;
    }

    public Matrix4x4f transpose()
    {
        final Matrix4x4f mat = STATE.get().temp;
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                mat.m[(y << 2) + x] = this.m[(x << 2) + y];
            }
        }
        return this.set(mat.m, 0);
    }

    public Matrix4x4f multiply(final Matrix4x4f mat)
    {
        return this.preConcat(mat);
    }

    public Matrix4x4f concat(final Matrix4x4f mat)
    {
        return this.preConcat(mat);
    }

    public Matrix4x4f toMatrix3x3()
    {
        final Matrix4x4f ret = new Matrix4x4f();

        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                if (x < 3 && y < 3)
                {
                    ret.m[x + (y << 2)] = this.m[x + (y << 2)];
                }
                else
                {
                    if (x == 3 && y == 3)
                    {
                        ret.m[x + (y << 2)] = 1;
                    }
                    else
                    {
                        ret.m[x + (y << 2)] = 0;
                    }
                }
            }
        }

        return ret;
    }

    public Vector3f multiply(final Vector3f vec)
    {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        final float rcpw = 1 / (x * this.m[M30] + y * this.m[M31] + z * this.m[M32] + this.m[M33]);
        vec.x = (x * this.m[M00] + y * this.m[M01] + z * this.m[M02] + this.m[M03]) * rcpw;
        vec.y = (x * this.m[M10] + y * this.m[M11] + z * this.m[M12] + this.m[M13]) * rcpw;
        vec.z = (x * this.m[M20] + y * this.m[M21] + z * this.m[M22] + this.m[M23]) * rcpw;
        return vec;
    }

    public Vector3f multiply3x3(final Vector3f vec)
    {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * this.m[M00] + y * this.m[M01] + z * this.m[M02];
        vec.y = x * this.m[M10] + y * this.m[M11] + z * this.m[M12];
        vec.z = x * this.m[M20] + y * this.m[M21] + z * this.m[M22];
        return vec;
    }

    public Vector4f multiply(final Vector4f vec)
    {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        final float w = vec.w;
        vec.x = x * this.m[M00] + y * this.m[M01] + z * this.m[M02] + w * this.m[M03];
        vec.y = x * this.m[M10] + y * this.m[M11] + z * this.m[M12] + w * this.m[M13];
        vec.z = x * this.m[M20] + y * this.m[M21] + z * this.m[M22] + w * this.m[M23];
        vec.w = x * this.m[M30] + y * this.m[M31] + z * this.m[M32] + w * this.m[M33];
        return vec;
    }

    public void writeTo(final float[] array, final int offset)
    {
        System.arraycopy(this.m, 0, array, offset, 16);
    }

    @Override
    public Matrix4x4f clone()
    {
        return new Matrix4x4f(this);
    }

    @Override
    public String toString()
    {
        return "{" + this.m[M00] + ", " + this.m[M01] + ", " + this.m[M02] + ", " + this.m[M03] + "\n"
                + this.m[M10] + ", " + this.m[M11] + ", " + this.m[M12] + ", " + this.m[M13] + "\n"
                + this.m[M20] + ", " + this.m[M21] + ", " + this.m[M22] + ", " + this.m[M23] + "\n"
                + this.m[M30] + ", " + this.m[M31] + ", " + this.m[M32] + ", " + this.m[M33] + "}";
    }

    private final static ThreadLocal<State> STATE = new ThreadLocal<State>()
                                                  {
                                                      @Override
                                                      protected State initialValue()
                                                      {
                                                          return new State();
                                                      };
                                                  };

    private final static class State
    {
        public Matrix4x4f temp = new Matrix4x4f();
    }
}
