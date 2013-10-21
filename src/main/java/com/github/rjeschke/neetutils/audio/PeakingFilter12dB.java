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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class PeakingFilter12dB
{
    private final SVF12dB svf;
    private double        fc, bg, gain, bandwidth;

    public PeakingFilter12dB(final double fs)
    {
        this.svf = new SVF12dB(fs);
        this.gain = 0;
        this.bandwidth = fs / 10.0;
        this.setCutoff(fs * 0.1);
    }

    public void setCutoff(final double cutoff)
    {
        this.svf.setCutoff(this.fc = cutoff);
        this.recalc();
    }

    public void setGain(final double db)
    {
        this.gain = db;
        this.recalc();
    }

    public void setBandwidth(final double bw)
    {
        this.bandwidth = bw;
        this.recalc();
    }

    public void reset()
    {
        this.svf.reset();
    }

    private void recalc()
    {
        final double b = this.bandwidth / this.fc;
        final double g = Math.pow(10.0, this.gain / 20.0);
        this.bg = b * g;
        this.svf.setRawQ(b);
    }

    public PeakingFilter12dB setClipper(final Clipper clipper)
    {
        this.svf.setClipper(clipper);
        return this;
    }

    public double process(final double input)
    {
        this.svf.process(input);
        return this.svf.low() + this.bg * this.svf.band() + this.svf.high();
    }
}
