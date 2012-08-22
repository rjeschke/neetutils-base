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
 * A soft clipper with linear transfer characteristics from -1.0 to 1.0 and
 * a maximum output level of +/- 2.0.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class SoftSaturator2_4 implements Clipper
{
    private final TubeCompress tube;
    
    public SoftSaturator2_4()
    {
        this.tube = new TubeCompress(0, 1, 2.1, 0.2, 0.25, 1);
    }
    
    @Override
    public double clip(double value)
    {
        return Math.abs(value) < 1e-100 ? 0 : this.tube.process(value);
    }
}
