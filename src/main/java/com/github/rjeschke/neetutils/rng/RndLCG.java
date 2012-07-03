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
public class RndLCG implements RNG
{
    private final static long A = 6364136223846793005L;
    private final static long C = 1442695040888963407L;
    private long value;
    
    public RndLCG()
    {
        this(RNGFactory.defaultSeed());
    }
    
    public RndLCG(final long seed)
    {
        this.value = seed;
    }
    
    @Override
    public int nextInt()
    {
        this.value = this.value * A + C;
        return (int)(this.value >> 32);
    }
    
    @Override
    public int nextInt(int max)
    {
        return (int)(nextDoubleUnipolar() * max);
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
