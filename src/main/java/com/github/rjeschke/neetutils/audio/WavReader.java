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
package com.github.rjeschke.neetutils.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.rjeschke.neetutils.io.NInputStreamLE;

public final class WavReader
{
    private int     sampleRate;
    private int     channels;
    private float[] data;
    private int[]   originalData;

    private WavReader(final int sampleRate, final int channels, final float[] data, final int[] originalData)
    {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.data = data;
        this.originalData = originalData;
    }

    public int getSamplerate()
    {
        return this.sampleRate;
    }

    public int getChannelCount()
    {
        return this.channels;
    }

    public float[] getData()
    {
        return this.data;
    }

    public int[] getOriginalData()
    {
        return this.originalData;
    }

    public static WavReader load(final String filename) throws IOException
    {
        return load(new File(filename));
    }

    public static WavReader load(final File file) throws IOException
    {
        final FileInputStream fis = new FileInputStream(file);
        try
        {
            return load(new BufferedInputStream(fis));
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            fis.close();
        }
    }

    public static WavReader load(final InputStream input) throws IOException
    {
        final NInputStreamLE in = new NInputStreamLE(input);
        if (!in.readString(4, 0).equals("RIFF")) throw new IOException("Not a WAV file.");
        in.readI32();
        if (!in.readString(4, 0).equals("WAVE")) throw new IOException("Not a WAV file.");
        if (!in.readString(4, 0).equals("fmt ")) throw new IOException("Not a WAV file.");
        final int fmtSz = in.readI32();
        final int fmt = in.readU16();
        if (fmt != 1 || fmtSz != 16) throw new IOException("Unsupported WAV format");

        final int channels = in.readU16();
        final int samplerate = in.readI32();
        in.readI32();
        in.readU16();
        final int bits = in.readU16();
        String tmp = in.readString(4, 0);
        while (!tmp.equals("data"))
        {
            final int toskip = in.readI32();
            if (in.skip(toskip) != toskip) throw new IOException("Unsupported WAV format");
            tmp = in.readString(4, 0);
        }
        final int length = in.readI32() / ((bits * channels) >> 3);
        final float[] ret = new float[length * channels];
        final int[] iret = new int[length * channels];

        for (int i = 0; i < ret.length; i++)
        {
            int v = 0;
            if (bits == 8) v = (iret[i] = (in.readU8() - 128)) << 16;
            else if (bits == 16) v = (iret[i] = in.readI16()) << 8;
            else if (bits == 24) iret[i] = v = in.readI24();
            else throw new IOException("Unsupported bit depth: " + bits);
            ret[i] = v / 8388608.0f;
        }

        return new WavReader(samplerate, channels, ret, iret);
    }
}
