package com.github.rjeschke.neetutils.audio;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class LPF6dBGVar
{
    private final double fs;
    private double       f;
    private double       b            = 0;
    private double       t            = 1;
    private double       fb           = 1;
    private double       feedback     = 1;
    private Clipper      clipper      = new DefaultClipper();
    private GainModifier gainModifier = new DefaultGainModifier();

    public LPF6dBGVar(final double fs)
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
        this.fb = this.f * this.feedback;
    }

    public void setFeedback(final double fb)
    {
        this.feedback = fb;
        this.fb = this.f * this.feedback;
    }

    public double coef(final double previous)
    {
        return previous * this.t * this.f / (1 + this.t * this.fb);
    }

    public double output(final double input)
    {
        return (this.b + this.t * this.f * input) / (1 + this.t * this.fb);
    }

    public double process(final double input)
    {
        final double o = (this.b + this.f * this.t * input) / (1 + this.t * this.fb);
        final double in = input - this.feedback * o;
        this.b = this.clipper.clip(o + this.t * this.f * in);
        this.t = this.gainModifier.calculateGain(in);
        return o;
    }

    public LPF6dBGVar setClipper(final Clipper clipper)
    {
        this.clipper = clipper;
        return this;
    }

    public LPF6dBGVar setGainModifier(final GainModifier gainModifier)
    {
        this.gainModifier = gainModifier;
        return this;
    }
}
