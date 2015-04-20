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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class Matrix4x4d
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
    public final double[]   m   = new double[16];

    public Matrix4x4d()
    {
        this.m[M00] = 1;
        this.m[M11] = 1;
        this.m[M22] = 1;
        this.m[M33] = 1;
    }

    public Matrix4x4d(final double[] m)
    {
        System.arraycopy(m, 0, this.m, 0, 16);
    }

    public Matrix4x4d(final double[] m, final int offset)
    {
        System.arraycopy(m, offset, this.m, 0, 16);
    }

    public Matrix4x4d(final Matrix4x4d m)
    {
        System.arraycopy(m.m, 0, this.m, 0, 16);
    }

    public Matrix4x4d preConcat(final Matrix4x4d other)
    {
        MatrixMathD.multiplyInPlace(this.m, other.m, this.m);
        return this;
    }

    public Matrix4x4d postConcat(final Matrix4x4d other)
    {
        MatrixMathD.multiplyInPlace(other.m, this.m, this.m);
        return this;
    }

    public Matrix4x4d set(final double[] arr, final int offs)
    {
        System.arraycopy(arr, offs, this.m, 0, 16);
        return this;
    }

    public Matrix4x4d set(final Matrix4x4d other)
    {
        return this.set(other.m, 0);
    }

    public Matrix4x4d set(final double[] arr)
    {
        return this.set(arr, 0);
    }

    public static Matrix4x4d identity()
    {
        return new Matrix4x4d();
    }

    public static Matrix4x4d translate(final double x, final double y, final double z)
    {
        return new Matrix4x4d().setTranslate(x, y, z);
    }

    public static Matrix4x4d translate(final Vector3d v)
    {
        return new Matrix4x4d().setTranslate(v);
    }

    public static Matrix4x4d scale(final double x, final double y, final double z)
    {
        return new Matrix4x4d().setScale(x, y, z);
    }

    public static Matrix4x4d scale(final Vector3d v)
    {
        return new Matrix4x4d().setScale(v);
    }

    public static Matrix4x4d rotateX(final double radians)
    {
        return MatrixMathD.rotateX(radians, new Matrix4x4d());
    }

    public static Matrix4x4d rotateY(final double radians)
    {
        return MatrixMathD.rotateY(radians, new Matrix4x4d());
    }

    public static Matrix4x4d rotateZ(final double radians)
    {
        return MatrixMathD.rotateZ(radians, new Matrix4x4d());
    }

    public static Matrix4x4d lookAtLH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return MatrixMathD.lookAtLH(pos, lookat, up, new Matrix4x4d());
    }

    public static Matrix4x4d lookAtRH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return MatrixMathD.lookAtRH(pos, lookat, up, new Matrix4x4d());
    }

    public static Matrix4x4d projectionLH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return MatrixMathD.projectionLH(fovRadians, aspectRatio, near_z, far_z, new Matrix4x4d());
    }

    public static Matrix4x4d projectionRH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return MatrixMathD.projectionRH(fovRadians, aspectRatio, near_z, far_z, new Matrix4x4d());
    }

    public Matrix4x4d setTranslate(final double x, final double y, final double z)
    {
        return MatrixMathD.translate(x, y, z, this);
    }

    public Matrix4x4d preTranslate(final double x, final double y, final double z)
    {
        return this.preConcat(MatrixMathD.translate(x, y, z, STATE.get().temp));
    }

    public Matrix4x4d postTranslate(final double x, final double y, final double z)
    {
        return this.postConcat(MatrixMathD.translate(x, y, z, STATE.get().temp));
    }

    public Matrix4x4d setTranslate(final Vector3d v)
    {
        return MatrixMathD.translate(v.x, v.y, v.z, this);
    }

    public Matrix4x4d preTranslate(final Vector3d v)
    {
        return this.preConcat(MatrixMathD.translate(v.x, v.y, v.z, STATE.get().temp));
    }

    public Matrix4x4d postTranslate(final Vector3d v)
    {
        return this.postConcat(MatrixMathD.translate(v.x, v.y, v.z, STATE.get().temp));
    }

    public Matrix4x4d setScale(final double x, final double y, final double z)
    {
        return MatrixMathD.scale(x, y, z, this);
    }

    public Matrix4x4d preScale(final double x, final double y, final double z)
    {
        return this.preConcat(MatrixMathD.scale(x, y, z, STATE.get().temp));
    }

    public Matrix4x4d postScale(final double x, final double y, final double z)
    {
        return this.postConcat(MatrixMathD.scale(x, y, z, STATE.get().temp));
    }

    public Matrix4x4d setScale(final Vector3d v)
    {
        return MatrixMathD.scale(v.x, v.y, v.z, this);
    }

    public Matrix4x4d preScale(final Vector3d v)
    {
        return this.preConcat(MatrixMathD.scale(v.x, v.y, v.z, STATE.get().temp));
    }

    public Matrix4x4d postScale(final Vector3d v)
    {
        return this.postConcat(MatrixMathD.scale(v.x, v.y, v.z, STATE.get().temp));
    }

    public Matrix4x4d setRotateX(final double radians)
    {
        return MatrixMathD.rotateX(radians, this);
    }

    public Matrix4x4d preRotateX(final double radians)
    {
        return this.preConcat(MatrixMathD.rotateX(radians, STATE.get().temp));
    }

    public Matrix4x4d postRotateX(final double radians)
    {
        return this.postConcat(MatrixMathD.rotateX(radians, STATE.get().temp));
    }

    public Matrix4x4d setRotateY(final double radians)
    {
        return MatrixMathD.rotateY(radians, this);
    }

    public Matrix4x4d preRotateY(final double radians)
    {
        return this.preConcat(MatrixMathD.rotateY(radians, STATE.get().temp));
    }

    public Matrix4x4d postRotateY(final double radians)
    {
        return this.postConcat(MatrixMathD.rotateY(radians, STATE.get().temp));
    }

    public Matrix4x4d setRotateZ(final double radians)
    {
        return MatrixMathD.rotateZ(radians, this);
    }

    public Matrix4x4d preRotateZ(final double radians)
    {
        return this.preConcat(MatrixMathD.rotateZ(radians, STATE.get().temp));
    }

    public Matrix4x4d postRotateZ(final double radians)
    {
        return this.postConcat(MatrixMathD.rotateZ(radians, STATE.get().temp));
    }

    public Matrix4x4d setLookAtLH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return MatrixMathD.lookAtLH(pos, lookat, up, this);
    }

    public Matrix4x4d preLookAtLH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return this.preConcat(MatrixMathD.lookAtLH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4d postLookAtLH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return this.postConcat(MatrixMathD.lookAtLH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4d setLookAtRH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return MatrixMathD.lookAtRH(pos, lookat, up, this);
    }

    public Matrix4x4d preLookAtRH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return this.preConcat(MatrixMathD.lookAtRH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4d postLookAtRH(final Vector3d pos, final Vector3d lookat, final Vector3d up)
    {
        return this.postConcat(MatrixMathD.lookAtRH(pos, lookat, up, STATE.get().temp));
    }

    public Matrix4x4d setProjectionLH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return MatrixMathD.projectionLH(fovRadians, aspectRatio, near_z, far_z, this);
    }

    public Matrix4x4d preProjectionLH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return this.preConcat(MatrixMathD.projectionLH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4d postProjectionLH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return this.postConcat(MatrixMathD.projectionLH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4d setProjectionRH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return MatrixMathD.projectionRH(fovRadians, aspectRatio, near_z, far_z, this);
    }

    public Matrix4x4d preProjectionRH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return this.preConcat(MatrixMathD.projectionRH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4d postProjectionRH(final double fovRadians, final double aspectRatio, final double near_z,
            final double far_z)
    {
        return this.postConcat(MatrixMathD.projectionRH(fovRadians, aspectRatio, near_z, far_z, STATE.get().temp));
    }

    public Matrix4x4d setIdentity()
    {
        Arrays.fill(this.m, 0);
        this.m[M00] = 1;
        this.m[M11] = 1;
        this.m[M22] = 1;
        this.m[M33] = 1;
        return this;
    }

    public Matrix4x4d invert()
    {
        MatrixMathD.invert(this.m, this.m);
        return this;
    }

    public Matrix4x4d transpose()
    {
        final Matrix4x4d mat = STATE.get().temp;
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                mat.m[(y << 2) + x] = this.m[(x << 2) + y];
            }
        }
        return this.set(mat.m, 0);
    }

    public Matrix4x4d multiply(final Matrix4x4d mat)
    {
        return this.preConcat(mat);
    }

    public Matrix4x4d concat(final Matrix4x4d mat)
    {
        return this.preConcat(mat);
    }

    public Matrix4x4d toMatrix3x3()
    {
        final Matrix4x4d ret = new Matrix4x4d();

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

    public Vector3d multiply(final Vector3d vec)
    {
        final double x = vec.x;
        final double y = vec.y;
        final double z = vec.z;
        final double rcpw = 1.0 / (x * this.m[M30] + y * this.m[M31] + z * this.m[M32] + this.m[M33]);
        vec.x = (x * this.m[M00] + y * this.m[M01] + z * this.m[M02] + this.m[M03]) * rcpw;
        vec.y = (x * this.m[M10] + y * this.m[M11] + z * this.m[M12] + this.m[M13]) * rcpw;
        vec.z = (x * this.m[M20] + y * this.m[M21] + z * this.m[M22] + this.m[M23]) * rcpw;
        return vec;
    }

    public Vector3d multiply3x3(final Vector3d vec)
    {
        final double x = vec.x;
        final double y = vec.y;
        final double z = vec.z;
        vec.x = x * this.m[M00] + y * this.m[M01] + z * this.m[M02];
        vec.y = x * this.m[M10] + y * this.m[M11] + z * this.m[M12];
        vec.z = x * this.m[M20] + y * this.m[M21] + z * this.m[M22];
        return vec;
    }

    public Vector4d multiply(final Vector4d vec)
    {
        final double x = vec.x;
        final double y = vec.y;
        final double z = vec.z;
        final double w = vec.w;
        vec.x = x * this.m[M00] + y * this.m[M01] + z * this.m[M02] + w * this.m[M03];
        vec.y = x * this.m[M10] + y * this.m[M11] + z * this.m[M12] + w * this.m[M13];
        vec.z = x * this.m[M20] + y * this.m[M21] + z * this.m[M22] + w * this.m[M23];
        vec.w = x * this.m[M30] + y * this.m[M31] + z * this.m[M32] + w * this.m[M33];
        return vec;
    }

    public void writeTo(final double[] array, final int offset)
    {
        System.arraycopy(this.m, 0, array, offset, 16);
    }

    @Override
    public Matrix4x4d clone()
    {
        return new Matrix4x4d(this);
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
        public Matrix4x4d temp = new Matrix4x4d();
    }
}
