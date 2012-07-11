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

import com.github.rjeschke.neetutils.collections.Pair;
import com.github.rjeschke.neetutils.collections.Tuple;

public class Fns
{
    private Fns()
    {
        //
    }

    public final static <A extends Comparable<A>, B extends Comparable<B>> FnMap<Tuple<A, B>, Pair<A, B>> tuple2pair()
    {
        return new FnMap<Tuple<A, B>, Pair<A, B>>()
        {
            @Override
            public Pair<A, B> map(Tuple<A, B> value)
            {
                return Pair.of(value.a, value.b);
            }
        };
    }

    public final static <A> FnInstance<A> defaultInstanceFn(final A object)
    {
        return new FnInstance<A>()
        {
            @SuppressWarnings("unchecked")
            @Override
            public A create()
            {
                try
                {
                    return (A)object.getClass().newInstance();
                }
                catch (InstantiationException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
