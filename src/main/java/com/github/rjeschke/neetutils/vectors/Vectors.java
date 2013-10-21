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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class Vectors
{
    private Vectors()
    {
        // meh!
    }

    public final static int X = 0;
    public final static int Y = 1;
    public final static int Z = 2;
    public final static int W = 3;

    public static Vector2f asFloat(final Vector2d in, final Vector2f out)
    {
        out.x = (float)in.x;
        out.y = (float)in.y;
        return out;
    }

    public static Vector2f asFloat(final Vector2d in)
    {
        return asFloat(in, new Vector2f());
    }

    public static Vector3f asFloat(final Vector3d in, final Vector3f out)
    {
        out.x = (float)in.x;
        out.y = (float)in.y;
        out.z = (float)in.z;
        return out;
    }

    public static Vector3f asFloat(final Vector3d in)
    {
        return asFloat(in, new Vector3f());
    }

    public static Vector4f asFloat(final Vector4d in, final Vector4f out)
    {
        out.x = (float)in.x;
        out.y = (float)in.y;
        out.z = (float)in.z;
        out.w = (float)in.w;
        return out;
    }

    public static Vector4f asFloat(final Vector4d in)
    {
        return asFloat(in, new Vector4f());
    }

    public static Vector2d asDouble(final Vector2f in, final Vector2d out)
    {
        out.x = in.x;
        out.y = in.y;
        return out;
    }

    public static Vector2d asDouble(final Vector2f in)
    {
        return asDouble(in, new Vector2d());
    }

    public static Vector3d asDouble(final Vector3f in, final Vector3d out)
    {
        out.x = in.x;
        out.y = in.y;
        out.z = in.z;
        return out;
    }

    public static Vector3d asDouble(final Vector3f in)
    {
        return asDouble(in, new Vector3d());
    }

    public static Vector4d asDouble(final Vector4f in, final Vector4d out)
    {
        out.x = in.x;
        out.y = in.y;
        out.z = in.z;
        out.w = in.w;
        return out;
    }

    public static Vector4d asDouble(final Vector4f in)
    {
        return asDouble(in, new Vector4d());
    }
}
