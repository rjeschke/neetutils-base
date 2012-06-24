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
 * Complementary Multiply-With-Carry 
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class RndCMWC implements RNG
{
	private int pos = 4095;
	private int accu;
	private final int[] history = new int[4096];
	
	public RndCMWC()
	{
        this(RNGFactory.defaultSeed());
	}
	
	public RndCMWC(long seed)
	{
        final RndLCG rnd = new RndLCG(seed);
		this.accu = rnd.nextInt();
		while(this.accu < 0)
			this.accu += 809430660L;
		while(this.accu > 809430660L)
			this.accu -= 809430660L;
		
		for(int i = 0; i < 4096; i++)
		{
			this.history[i] = rnd.nextInt();
		}
	}
	
	@Override
	public int nextInt()
	{
		final int p = this.pos = (this.pos + 1) & 4095;
		final long t = 18782L * (this.history[p] & 0xffffffffL) + this.accu;
		this.accu = (int)(t >> 32);
		int x = (int)t + this.accu;
		if(x < this.accu)
		{
			x++;
			this.accu++;
		}
		return this.history[p] = 0xfffffffe - x;
	}
	
    @Override
    public int nextInt(int max)
    {
        return (int)(nextFloatUnipolar() * max);
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
