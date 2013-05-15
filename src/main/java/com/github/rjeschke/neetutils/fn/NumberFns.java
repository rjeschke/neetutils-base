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
package com.github.rjeschke.neetutils.fn;

import com.github.rjeschke.neetutils.math.Numbers;

public final class NumberFns
{
    private NumberFns()
    {
        // 1up
    }

    public final static FnCombine<Number, Number, Number> add()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(final Number a, final Number b)
            {
                return Numbers.add(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> addFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(final Number a, final Number b)
            {
                return Numbers.add(a, b);
            }
        };
    }

    public final static FnCombine<Number, Number, Number> sub()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(final Number a, final Number b)
            {
                return Numbers.sub(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> subFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(final Number a, final Number b)
            {
                return Numbers.sub(a, b);
            }
        };
    }

    public final static FnCombine<Number, Number, Number> mul()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(final Number a, final Number b)
            {
                return Numbers.mul(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> mulFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(final Number a, final Number b)
            {
                return Numbers.mul(a, b);
            }
        };
    }

    public final static FnCombine<Number, Number, Number> div()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(final Number a, final Number b)
            {
                return Numbers.div(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> divFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(final Number a, final Number b)
            {
                return Numbers.div(a, b);
            }
        };
    }
}
