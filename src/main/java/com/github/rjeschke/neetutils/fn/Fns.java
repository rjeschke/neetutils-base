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

import java.util.Map;

import com.github.rjeschke.neetutils.math.Numbers;

public final class Fns
{
    private Fns()
    {
        //
    }

    public final static <A> FnFoldStep<A, Map<A, Integer>> objectFrequencies()
    {
        return new FnFoldStep<A, Map<A, Integer>>()
        {
            @Override
            public Map<A, Integer> applyFoldStep(A a, Map<A, Integer> b)
            {
                final Integer i = b.get(a);
                b.put(a, i == null ? Numbers.integerOf(1) : Numbers.integerOf(i + 1));
                return b;
            }
        };
    }

    public final static <A> FnPredicate<A> invert(final FnPredicate<A> predicate)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(A a)
            {
                return !predicate.applyPredicate(a);
            }
        };
    }

    public final static <A> FnEquals<A> examineEquals()
    {
        return new FnEquals<A>()
        {
            @Override
            public boolean applyEquals(A a, A b)
            {
                if (a == null) return b == null;
                return a.equals(b);
            }
        };
    }
}
