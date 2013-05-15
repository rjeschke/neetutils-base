package com.github.rjeschke.neetutils.rng;

public abstract class AbstractRNG implements RNG
{
    @Override
    public int nextInt(final int max)
    {
        return (int)(this.nextDoubleUnipolar() * max);
    }

    @Override
    public float nextFloatUnipolar()
    {
        return (this.nextInt() / 4294967296.f) + 0.5f;
    }

    @Override
    public float nextFloatBipolar()
    {
        return this.nextInt() / 2147483648.f;
    }

    @Override
    public double nextDoubleUnipolar()
    {
        return (this.nextInt() / 4294967296.0) + 0.5;
    }

    @Override
    public double nextDoubleBipolar()
    {
        return this.nextInt() / 2147483648.0;
    }
}
