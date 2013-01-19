package com.github.rjeschke.neetutils.audio;

public interface Resampler
{
    public int available();

    public int size();

    public int firSize();

    public int needs();

    public void reset();

    public void put(final double input);

    public double get();
}
