package com.github.rjeschke.neetutils.audio;

public class DefaultGainModifier implements GainModifier
{
    @Override
    public double calculateGain(double x)
    {
        return 1;
    }
}
