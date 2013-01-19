package com.github.rjeschke.neetutils.audio;

import java.util.Arrays;

import com.github.rjeschke.neetutils.math.NMath;

public class ResamplerUp implements Resampler
{
    private final double[] buffer;
    private final int      bufferSize;
    private final int      bufferMask;
    private final double[] fir;
    private final int      firLength;
    private final int      step;
    private final int      firNeeds;
    private int            readPos;
    private int            writePos;
    private int            subPos;

    ResamplerUp(final double srcFs, final double targetFs, final double[] firFilter)
    {
        this.bufferSize = NMath.nextPow2(firFilter.length * 2 + 1);
        this.bufferMask = this.bufferSize - 1;
        this.buffer = new double[this.bufferSize];
        this.firLength = firFilter.length;
        this.fir = Arrays.copyOf(firFilter, this.firLength);

        final double f = srcFs / targetFs;
        this.firNeeds = (int)Math.ceil(f * this.firLength + 1);
        this.step = (int)(0.5 + 1048576.0 * f);
        this.reset();
    }

    @Override
    public int available()
    {
        return (this.readPos <= this.writePos) ? this.writePos - this.readPos : this.bufferSize - this.readPos
                + this.writePos;
    }

    @Override
    public int size()
    {
        return this.bufferSize;
    }

    @Override
    public int firSize()
    {
        return this.firLength;
    }

    @Override
    public int needs()
    {
        return this.firNeeds - this.available();
    }

    @Override
    public void reset()
    {
        this.readPos = this.bufferSize - this.firNeeds / 2;
        this.subPos = this.writePos = 0;
        Arrays.fill(this.buffer, 0);
    }

    @Override
    public void put(final double input)
    {
        this.buffer[this.writePos] = input;
        this.writePos = (this.writePos + 1) & this.bufferMask;
    }

    @Override
    public double get()
    {
        double ret = 0;
        int r = this.readPos, s = this.subPos;

        for (int i = 0; i < this.firLength; i++)
        {
            final double f = s / 1048576.0;

            final double f0 = this.buffer[r];
            ret += this.fir[i] * (f0 + f * (this.buffer[(r + 1) & this.bufferMask] - f0));

            s += this.step;
            r = (r + (s >> 20)) & this.bufferMask;
            s &= 0xfffff;
        }

        this.subPos += this.step;
        this.readPos = (this.readPos + (this.subPos >> 20)) & this.bufferMask;
        this.subPos &= 0xfffff;

        return ret;
    }
}
