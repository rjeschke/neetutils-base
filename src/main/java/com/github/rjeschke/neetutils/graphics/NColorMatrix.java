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
package com.github.rjeschke.neetutils.graphics;

/**
 * Color transformation matrix in row-major order.
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public class NColorMatrix
{
    protected final float[]          m            = new float[3 * 3];

    public final static NColorMatrix RGB2YUV      = new NColorMatrix(0.299f, 0.587f, 0.114f, -0.14713f, -0.28886f, 0.436f, 0.615f, -0.51499f, -0.10001f);

    public final static NColorMatrix YUV2RGB      = new NColorMatrix(1.f, 0.f, 1.13983f, 1.f, -0.39465f, -0.58060f, 1.f, 2.03211f, 0.f);

    public final static NColorMatrix RGB2YUV_HDTV = new NColorMatrix(0.2126f, 0.7152f, 0.0722f, -0.09991f, -0.33609f, 0.436f, 0.615f, -0.55861f, -0.05639f);

    public final static NColorMatrix YUV2RGB_HDTV = new NColorMatrix(1.f, 0.f, 1.28033f, 1.f, -0.21482f, -0.38059f, 1.f, 2.12798f, 0.f);

    public final static NColorMatrix RGB2YPbPr    = new NColorMatrix(0.299f, 0.587f, 0.114f, -0.168736f, -0.331264f, 0.5f, 0.5f, -0.418688f, -0.081312f);

    public final static NColorMatrix YPbPr2RGB    = new NColorMatrix(1.f, 0.f, 1.402f, 1.f, -0.34414f, -0.71414f, 1.f, 1.772f, 0f);

    public final static NColorMatrix RGB2YIQ      = new NColorMatrix(0.299f, 0.587f, 0.114f, 0.596f, -0.274f, -0.322f, 0.212f, -0.523f, 0.311f);

    public final static NColorMatrix YIQ2RGB      = new NColorMatrix(1.f, 0.956f, 0.621f, 1.f, -0.272f, -0.647f, 1.f, -1.105f, 1.702f);

    public final static NColorMatrix XYZ2RGB_D65  = new NColorMatrix(3.240479f, -1.537150f, -0.498535f, -0.969256f, 1.875992f, 0.041556f, 0.055648f,
                                                          -0.204043f, 1.057311f);

    public final static NColorMatrix RGB2XYZ_D65  = new NColorMatrix(0.412453f, 0.357580f, 0.180423f, 0.212671f, 0.715160f, 0.072169f, 0.019334f, 0.119193f,
                                                          0.950227f);

    public NColorMatrix(final float... m)
    {
        System.arraycopy(m, 0, this.m, 0, Math.min(9, m.length));
    }

    public float m(final int index)
    {
        return this.m[index];
    }
}
