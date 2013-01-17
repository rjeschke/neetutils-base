package com.github.rjeschke.neetutils.audio;

import com.github.rjeschke.neetutils.audio.Clipper;
import com.github.rjeschke.neetutils.audio.DefaultClipper;

public class LPF6dBG
{
    private final double fs;
    private double       f;
    private double       b            = 0;
    private double       t            = 1;
    private Clipper      clipper      = new DefaultClipper();
    private GainModifier gainModifier = new DefaultGainModifier();

    public LPF6dBG(final double fs)
    {
        this.fs = fs;
        this.setCutoff(fs * 0.1);
    }

    public void reset()
    {
        this.b = 0;
        this.t = 1;
    }

    public void setCutoff(final double freq)
    {
        this.f = Math.tan(Math.PI * freq / this.fs);
    }

    public double coef(double previous)
    {
        return previous * this.t * this.f / (1 + this.t * this.f);
    }

    public double output(final double input)
    {
        return (this.b + this.t * this.f * input) / (1 + this.t * this.f);
    }

    public double process(final double input)
    {
        final double o = (this.b + this.f * this.t * input) / (1 + this.t * this.f);
        final double in = input - o;
        this.b = this.clipper.clip(o + this.t * this.f * in);
        this.t = this.gainModifier.calculateGain(in);
        return o;
    }

    public LPF6dBG setClipper(final Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    public LPF6dBG setGainModifier(final GainModifier gainModifier)
    {
        this.gainModifier = gainModifier;
        return this;
    }
}
