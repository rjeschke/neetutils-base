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
 * A 12dB allpass filter realized using a 12dB SVF.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class Allpass12dB
{
    private final SVF12dB svf;

    /**
     * Constructor.
     * 
     * @param fs
     *            Sampling rate.
     */
    public Allpass12dB(double fs)
    {
        this.svf = new SVF12dB(fs);
        this.svf.setRawQ(1);
        this.setCutoff(fs * 0.1);
    }

    /**
     * Sets the cutoff of this filter in Hz.
     * 
     * @param cutoff
     *            The cutoff.
     */
    public void setCutoff(double cutoff)
    {
        this.svf.setCutoff(cutoff);
    }

    /**
     * Sets the clipper of this filter.
     * 
     * @param clipper
     *            The clipper.
     * @return this.
     */
    public Allpass12dB setClipper(Clipper clipper)
    {
        this.svf.setClipper(clipper);
        return this;
    }

    /**
     * Resets this filters state.
     */
    public void reset()
    {
        this.svf.reset();
    }

    /**
     * Processes an input sample.
     * 
     * @param input
     *            Input sample.
     * @return Processed input.
     */
    public double process(double input)
    {
        this.svf.process(input);
        return this.svf.high() - this.svf.band() + this.svf.low();
    }
}
