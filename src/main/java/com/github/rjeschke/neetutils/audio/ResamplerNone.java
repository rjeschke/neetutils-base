package com.github.rjeschke.neetutils.audio;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class ResamplerNone implements Resampler
{
    private double value;

    ResamplerNone()
    {
    }

    @Override
    public int available()
    {
        return 1;
    }

    @Override
    public int size()
    {
        return 1;
    }

    @Override
    public int firSize()
    {
        return 1;
    }

    @Override
    public int needs()
    {
        return 1;
    }

    @Override
    public void reset()
    {
        this.value = 0;
    }

    @Override
    public void put(final double input)
    {
        this.value = input;
    }

    @Override
    public double get()
    {
        return this.value;
    }
}
