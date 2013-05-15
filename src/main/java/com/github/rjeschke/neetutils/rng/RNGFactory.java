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

public final class RNGFactory
{
    private RNGFactory()
    { /* forbidden */
    }

    public final static long defaultSeed()
    {
        return System.nanoTime() + Thread.currentThread().getId();
    }

    public final static RNG create(final RNGType type)
    {
        return create(type, defaultSeed());
    }

    public final static RNG create(final RNGType type, final long seed)
    {
        switch (type)
        {
        case LCG:
            return new RndLCG(seed);
        case LCG32:
            return new RndLCG32(seed);
        case GFSR:
            return new RndGFSR(seed);
        case CMWC:
            return new RndCMWC(seed);
        }
        throw new IllegalArgumentException("This should never happen");
    }
}
