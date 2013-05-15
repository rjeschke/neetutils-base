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
package com.github.rjeschke.neetutils.rng;

/**
 * Linear congruential random number generator.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class RndLCG32 extends AbstractRNG
{
    private final static long A = 1036229L;
    private final static long C = 404161;
    private long              value;

    public RndLCG32()
    {
        this(RNGFactory.defaultSeed());
    }

    public RndLCG32(final long seed)
    {
        this.value = seed & 0xffffffffL;
    }

    @Override
    public int nextInt()
    {
        this.value = (this.value * A + C) & 0xffffffffL;
        return (int)(this.value >> 32);
    }
}
