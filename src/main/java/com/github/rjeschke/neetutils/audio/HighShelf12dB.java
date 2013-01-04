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

public class HighShelf12dB
{
    private final SVF12dB svf;
    private double        gain  = 1;
    private double        gain2 = Math.sqrt(2);

    public HighShelf12dB(double fs)
    {
        this.svf = new SVF12dB(fs);
        this.svf.setRawQ(Math.sqrt(2));
        this.setCutoff(fs * 0.1);
    }

    public void setCutoff(double cutoff)
    {
        this.svf.setCutoff(cutoff);
    }

    public void setGain(double db)
    {
        this.gain = Math.pow(10.0, db / 20.0);
        this.gain2 = Math.sqrt(2 * this.gain);
    }

    public HighShelf12dB setClipper(Clipper clipper)
    {
        this.svf.setClipper(clipper);
        return this;
    }

    public void reset()
    {
        this.svf.reset();
    }

    public double process(double input)
    {
        this.svf.process(input);
        return this.gain * this.svf.high() + this.gain2 * this.svf.band() + this.svf.low();
    }
}
