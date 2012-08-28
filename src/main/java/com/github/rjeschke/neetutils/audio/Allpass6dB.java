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
 * A 6dB allpass filter realized using 6dB low- and highpass
 * filters.
 *  
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class Allpass6dB
{
    private final LPF6dB lp;
    private final HPF6dB hp;
    
    /**
     * Constructor.
     * 
     * @param fs Sampling rate.
     */
    public Allpass6dB(double fs)
    {
        this.lp = new LPF6dB(fs);
        this.hp = new HPF6dB(fs);
        this.setCutoff(fs * 0.1);
    }
    
    /**
     * Sets the cutoff of this filter in Hz.
     * 
     * @param cutoff The cutoff.
     */
    public void setCutoff(double cutoff)
    {
        this.lp.setCutoff(cutoff);
        this.hp.setCutoff(cutoff);
    }
    
    /**
     * Sets the clipper of this filter.
     * 
     * @param clipper The clipper.
     * @return this.
     */
    public Allpass6dB setClipper(Clipper clipper)
    {
        this.lp.setClipper(clipper);
        this.hp.setClipper(clipper);
        return this;
    }
    
    /**
     * Resets this filters state.
     */
    public void reset()
    {
        this.lp.reset();
        this.hp.reset();
    }
    
    /**
     * Processes an input sample.
     * 
     * @param input Input sample.
     * @return Processed input.
     */
    public double process(double input)
    {
        return this.hp.tick(input) - this.lp.tick(input);
    }
}
