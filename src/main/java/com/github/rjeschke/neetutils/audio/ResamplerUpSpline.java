package com.github.rjeschke.neetutils.audio;

import java.util.Arrays;

public class ResamplerUpSpline implements Resampler
{
    private final double[] buffer;
    private final int      step;
    private int            readPos;
    private int            writePos;
    private int            subPos;
    private double         a, b, c, d;

    public ResamplerUpSpline(final double srcFs, final double targetFs)
    {
        this.buffer = new double[16];

        final double f = srcFs / targetFs;
        this.step = (int)(0.5 + 1048576.0 * f);
        this.reset();
    }

    @Override
    public int available()
    {
        return (this.readPos <= this.writePos) ? this.writePos - this.readPos : 8 - this.readPos + this.writePos;
    }

    @Override
    public int size()
    {
        return 8;
    }

    @Override
    public int firSize()
    {
        return 8;
    }

    @Override
    public int needs()
    {
        return 4 - this.available();
    }

    @Override
    public void reset()
    {
        this.readPos = this.subPos = this.writePos = 0;
        Arrays.fill(this.buffer, 0);
    }

    @Override
    public void put(final double input)
    {
        this.buffer[this.writePos] = this.buffer[this.writePos + 8] = input;

        final double v0 = this.buffer[this.readPos];
        final double v1 = this.buffer[this.readPos + 1];
        final double v2 = this.buffer[this.readPos + 2];
        final double v3 = this.buffer[this.readPos + 3];

        this.a = (2.0 * v1) * 0.5;
        this.b = (v2 - v0) * 0.5;
        this.c = (2.0 * v0 - 5.0 * v1 + 4.0 * v2 - v3) * 0.5;
        this.d = (3.0 * v1 - 3.0 * v2 + v3 - v0) * 0.5;

        this.writePos = (this.writePos + 1) & 7;
    }

    @Override
    public double get()
    {
        final double f = this.subPos / 1048576.0;

        final double ret = ((this.d * f + this.c) * f + this.b) * f + this.a;

        this.subPos += this.step;
        this.readPos = (this.readPos + (this.subPos >> 20)) & 7;
        this.subPos &= 0xfffff;

        return ret;
    }
}
