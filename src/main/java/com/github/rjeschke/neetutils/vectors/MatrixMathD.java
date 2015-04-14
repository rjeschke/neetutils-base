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

/**
*
* @author René Jeschke (rene_jeschke@yahoo.de)
*
*/
final class MatrixMathD
{
    public final static boolean invert(final double[] m, final double[] out)
    {
        final double[] tmp = STATE.get().tmp;

        tmp[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6]
                * m[11] - m[13] * m[7] * m[10];
        tmp[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12]
                * m[6] * m[11] + m[12] * m[7] * m[10];
        tmp[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5]
                * m[11] - m[12] * m[7] * m[9];
        tmp[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12]
                * m[5] * m[10] + m[12] * m[6] * m[9];
        tmp[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13]
                * m[2] * m[11] + m[13] * m[3] * m[10];
        tmp[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2]
                * m[11] - m[12] * m[3] * m[10];
        tmp[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1]
                * m[11] + m[12] * m[3] * m[9];
        tmp[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1]
                * m[10] - m[12] * m[2] * m[9];
        tmp[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2]
                * m[7] - m[13] * m[3] * m[6];
        tmp[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2]
                * m[7] + m[12] * m[3] * m[6];
        tmp[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1]
                * m[7] - m[12] * m[3] * m[5];
        tmp[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1]
                * m[6] + m[12] * m[2] * m[5];
        tmp[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2]
                * m[7] + m[9] * m[3] * m[6];
        tmp[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2]
                * m[7] - m[8] * m[3] * m[6];
        tmp[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1]
                * m[7] + m[8] * m[3] * m[5];
        tmp[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1]
                * m[6] - m[8] * m[2] * m[5];

        double det = m[0] * tmp[0] + m[1] * tmp[4] + m[2] * tmp[8] + m[3] * tmp[12];
        if (det == 0)
        {
            return false;
        }
        det = 1 / det;

        for (int i = 0; i < 16; i++)
        {
            out[i] = tmp[i] * det;
        }

        return true;
    }

    public final static void multiply(final double[] a, final double[] b, final double[] out)
    {
        for (int i = 0; i < 4; i++)
        {
            final double ai0 = a[i + (0 << 2)];
            final double ai1 = a[i + (1 << 2)];
            final double ai2 = a[i + (2 << 2)];
            final double ai3 = a[i + (3 << 2)];
            out[i + (0 << 2)] = ai0 * b[0 + (0 << 2)] + ai1 * b[1 + (0 << 2)] + ai2 * b[2 + (0 << 2)] + ai3
                    * b[3 + (0 << 2)];
            out[i + (1 << 2)] = ai0 * b[0 + (1 << 2)] + ai1 * b[1 + (1 << 2)] + ai2 * b[2 + (1 << 2)] + ai3
                    * b[3 + (1 << 2)];
            out[i + (2 << 2)] = ai0 * b[0 + (2 << 2)] + ai1 * b[1 + (2 << 2)] + ai2 * b[2 + (2 << 2)] + ai3
                    * b[3 + (2 << 2)];
            out[i + (3 << 2)] = ai0 * b[0 + (3 << 2)] + ai1 * b[1 + (3 << 2)] + ai2 * b[2 + (3 << 2)] + ai3
                    * b[3 + (3 << 2)];
        }
    }

    public final static void multiplyInPlace(final double[] a, final double[] b, final double[] out)
    {
        final double[] tmp = STATE.get().tmp;
        for (int i = 0; i < 4; i++)
        {
            final double ai0 = a[i + (0 << 2)];
            final double ai1 = a[i + (1 << 2)];
            final double ai2 = a[i + (2 << 2)];
            final double ai3 = a[i + (3 << 2)];
            tmp[i + (0 << 2)] = ai0 * b[0 + (0 << 2)] + ai1 * b[1 + (0 << 2)] + ai2 * b[2 + (0 << 2)] + ai3
                    * b[3 + (0 << 2)];
            tmp[i + (1 << 2)] = ai0 * b[0 + (1 << 2)] + ai1 * b[1 + (1 << 2)] + ai2 * b[2 + (1 << 2)] + ai3
                    * b[3 + (1 << 2)];
            tmp[i + (2 << 2)] = ai0 * b[0 + (2 << 2)] + ai1 * b[1 + (2 << 2)] + ai2 * b[2 + (2 << 2)] + ai3
                    * b[3 + (2 << 2)];
            tmp[i + (3 << 2)] = ai0 * b[0 + (3 << 2)] + ai1 * b[1 + (3 << 2)] + ai2 * b[2 + (3 << 2)] + ai3
                    * b[3 + (3 << 2)];
        }
        System.arraycopy(tmp, 0, out, 0, 16);
    }

    public final static Matrix4x4d lookAtLH(final Vector3d pos, final Vector3d lookat, final Vector3d up,
            final Matrix4x4d out)
    {
        final State s = STATE.get();

        s.zaxis.set(lookat).sub(pos).normalize();
        s.xaxis.set(up).cross(s.zaxis).normalize();
        s.yaxis.set(s.zaxis).cross(s.xaxis).normalize();

        out.m[Matrix4x4d.M00] = s.xaxis.x;
        out.m[Matrix4x4d.M01] = s.xaxis.y;
        out.m[Matrix4x4d.M02] = s.xaxis.z;
        out.m[Matrix4x4d.M03] = -s.xaxis.dot(pos);

        out.m[Matrix4x4d.M10] = s.yaxis.x;
        out.m[Matrix4x4d.M11] = s.yaxis.y;
        out.m[Matrix4x4d.M12] = s.yaxis.z;
        out.m[Matrix4x4d.M13] = -s.yaxis.dot(pos);

        out.m[Matrix4x4d.M20] = s.zaxis.x;
        out.m[Matrix4x4d.M21] = s.zaxis.y;
        out.m[Matrix4x4d.M22] = s.zaxis.z;
        out.m[Matrix4x4d.M23] = -s.zaxis.dot(pos);

        out.m[Matrix4x4d.M30] = 0;
        out.m[Matrix4x4d.M31] = 0;
        out.m[Matrix4x4d.M32] = 0;
        out.m[Matrix4x4d.M33] = 1;

        return out;
    }

    public final static Matrix4x4d lookAtRH(final Vector3d pos, final Vector3d lookat, final Vector3d up,
            final Matrix4x4d out)
    {
        final State s = STATE.get();

        s.zaxis.set(pos).sub(lookat).normalize();
        s.xaxis.set(up).cross(s.zaxis).normalize();
        s.yaxis.set(s.zaxis).cross(s.xaxis);

        out.m[Matrix4x4d.M00] = s.xaxis.x;
        out.m[Matrix4x4d.M01] = s.xaxis.y;
        out.m[Matrix4x4d.M02] = s.xaxis.z;
        out.m[Matrix4x4d.M03] = -s.xaxis.dot(pos);

        out.m[Matrix4x4d.M10] = s.yaxis.x;
        out.m[Matrix4x4d.M11] = s.yaxis.y;
        out.m[Matrix4x4d.M12] = s.yaxis.z;
        out.m[Matrix4x4d.M13] = -s.yaxis.dot(pos);

        out.m[Matrix4x4d.M20] = s.zaxis.x;
        out.m[Matrix4x4d.M21] = s.zaxis.y;
        out.m[Matrix4x4d.M22] = s.zaxis.z;
        out.m[Matrix4x4d.M23] = -s.zaxis.dot(pos);

        out.m[Matrix4x4d.M30] = 0;
        out.m[Matrix4x4d.M31] = 0;
        out.m[Matrix4x4d.M32] = 0;
        out.m[Matrix4x4d.M33] = 1;

        return out;
    }

    public final static Matrix4x4d projectionLH(final double fov, final double aspect, final double near_z,
            final double far_z, final Matrix4x4d out)
    {
        final double yscale = 1.0 / Math.tan(fov * 0.5);
        final double t = 1.0 / (far_z - near_z);
        final double xscale = yscale / aspect;

        out.setIdentity();
        out.m[Matrix4x4d.M00] = xscale;

        out.m[Matrix4x4d.M11] = yscale;

        out.m[Matrix4x4d.M22] = far_z * t;
        out.m[Matrix4x4d.M23] = (-near_z * far_z) * t;

        out.m[Matrix4x4d.M32] = 1;

        return out;
    }

    public final static Matrix4x4d projectionRH(final double fov, final double aspect, final double near_z,
            final double far_z, final Matrix4x4d out)
    {
        final double yscale = 1.0 / Math.tan(fov * 0.5);
        final double t = 1.0 / (far_z - near_z);
        final double xscale = yscale / aspect;

        out.m[Matrix4x4d.M00] = xscale;

        out.m[Matrix4x4d.M11] = yscale;

        out.m[Matrix4x4d.M22] = far_z * t;
        out.m[Matrix4x4d.M23] = (-near_z * far_z) * t;

        out.m[Matrix4x4d.M32] = -1;

        return out;
    }

    public final static Matrix4x4d translate(final double x, final double y, final double z, final Matrix4x4d out)
    {
        out.setIdentity();
        out.m[Matrix4x4d.M03] = x;
        out.m[Matrix4x4d.M13] = y;
        out.m[Matrix4x4d.M23] = z;
        return out;
    }

    public final static Matrix4x4d scale(final double x, final double y, final double z, final Matrix4x4d out)
    {
        out.setIdentity();
        out.m[Matrix4x4d.M00] = x;
        out.m[Matrix4x4d.M11] = y;
        out.m[Matrix4x4d.M22] = z;
        return out;
    }

    public final static Matrix4x4d uniformScale(final double s, final Matrix4x4d out)
    {
        return scale(s, s, s, out);
    }

    public final static Matrix4x4d rotateX(final double a, final Matrix4x4d out)
    {
        final double sn = Math.sin(a);
        final double cs = Math.cos(a);

        out.setIdentity();
        out.m[Matrix4x4d.M11] = cs;
        out.m[Matrix4x4d.M21] = sn;
        out.m[Matrix4x4d.M12] = -sn;
        out.m[Matrix4x4d.M22] = cs;

        return out;
    }

    public final static Matrix4x4d rotateY(final double a, final Matrix4x4d out)
    {
        final double sn = Math.sin(a);
        final double cs = Math.cos(a);

        out.setIdentity();
        out.m[Matrix4x4d.M00] = cs;
        out.m[Matrix4x4d.M20] = -sn;
        out.m[Matrix4x4d.M02] = sn;
        out.m[Matrix4x4d.M22] = cs;

        return out;
    }

    public final static Matrix4x4d rotateZ(final double a, final Matrix4x4d out)
    {
        final double sn = Math.sin(a);
        final double cs = Math.cos(a);

        out.setIdentity();
        out.m[Matrix4x4d.M00] = cs;
        out.m[Matrix4x4d.M10] = sn;
        out.m[Matrix4x4d.M01] = -sn;
        out.m[Matrix4x4d.M11] = cs;

        return out;
    }

    private final static ThreadLocal<State> STATE = new ThreadLocal<State>()
                                                  {
                                                      @Override
                                                      protected State initialValue()
                                                      {
                                                          return new State();
                                                      }
                                                  };

    private final static class State
    {
        public final double[] tmp   = new double[16];
        public final Vector3d xaxis = new Vector3d();
        public final Vector3d yaxis = new Vector3d();
        public final Vector3d zaxis = new Vector3d();
    }
}
