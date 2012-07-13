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
package com.github.rjeschke.neetutils;

//import sun.misc.Unsafe;

import com.github.rjeschke.neetutils.rng.RNG;
import com.github.rjeschke.neetutils.rng.RNGFactory;
import com.github.rjeschke.neetutils.rng.RNGType;

/**
 * Utility methods like in java.lang.System.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class SysUtils
{
    private final static ThreadLocal<Object> sleepObject = new ThreadLocal<Object>()
    {
        @Override
        protected Object initialValue()
        {
            return new Object();
        }
    };

    private final static ThreadLocal<RNG> RNG = new ThreadLocal<RNG>()
    {
        @Override
        protected RNG initialValue()
        {
            return RNGFactory.create(RNGType.LCG);
        }
    };

    private SysUtils()
    {
        //
    }

    public final static boolean sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            return false;
        }
        return true;
    }

    public final static boolean sleep(long millis, int nanos)
    {
        try
        {
            Thread.sleep(millis, nanos);
        }
        catch (InterruptedException e)
        {
            return false;
        }
        return true;
    }

    public final static boolean fineSleep(long millis)
    {
        final Object obj = sleepObject.get();

        try
        {
            synchronized (obj)
            {
                obj.wait(millis);
            }
        }
        catch (InterruptedException e)
        {
            return false;
        }

        return true;
    }

    public final static boolean fineSleep(long millis, int nanos)
    {
        final Object obj = sleepObject.get();

        try
        {
            synchronized (obj)
            {
                obj.wait(millis, nanos);
            }
        }
        catch (InterruptedException e)
        {
            return false;
        }

        return true;
    }

    public final static boolean threadJoin(final Thread t)
    {
        try
        {
            t.join();
        }
        catch (InterruptedException e)
        {
            return false;
        }
        return true;
    }

    public final static int rndInt()
    {
        return RNG.get().nextInt();
    }

    public final static int rndInt(int max)
    {
        return RNG.get().nextInt(max);
    }

    public final static float rndFloatUnipolar()
    {
        return RNG.get().nextFloatUnipolar();
    }

    public final static float rndFloatBipolar()
    {
        return RNG.get().nextFloatBipolar();
    }

    public final static double rndDoubleUnipolar()
    {
        return RNG.get().nextDoubleUnipolar();
    }

    public final static double rndDoubleBipolar()
    {
        return RNG.get().nextDoubleBipolar();
    }

    public final static int availableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public final static void gc()
    {
        System.gc();
    }

    public final static long usedMemory()
    {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public final static double usedMemoryRatio()
    {
        return (double)usedMemory() / (double)maxMemory();
    }

    public final static long freeMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    public final static long maxMemory()
    {
        return Runtime.getRuntime().maxMemory();
    }

    public final static long totalMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }

    /*
    public final static Unsafe getUnsafe()
    {
        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe)f.get(null);
    }
    */
}
