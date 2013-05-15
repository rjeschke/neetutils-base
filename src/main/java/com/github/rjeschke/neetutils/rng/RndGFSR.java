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
 * Generalized Feedback Shift Register
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class RndGFSR extends AbstractRNG
{
    private final static int A       = 11, B = 103, C = 307, D = 1009, E = 3001;
    private int              pos     = 0;
    private final int[]      history = new int[4096];

    public RndGFSR()
    {
        this(RNGFactory.defaultSeed());
    }

    public RndGFSR(final long seed)
    {
        final RndLCG rnd = new RndLCG(seed);
        for (int i = 0; i < 4096; i++)
        {
            this.history[i] = rnd.nextInt();
        }
    }

    @Override
    public int nextInt()
    {
        final int p = this.pos = (this.pos + 1) & 4095;
        return this.history[p] = this.history[(p - A) & 4095] ^ this.history[(p - B) & 4095] ^ this.history[(p - C) & 4095] ^ this.history[(p - D) & 4095]
                ^ this.history[(p - E) & 4095];
    }
}
