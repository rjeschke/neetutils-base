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

public final class Predicates
{
    private Predicates()
    {
        // 1up
    }

    public final static <A> FnPredicate<A> not(final FnPredicate<? super A> predicate)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(final A a)
            {
                return !predicate.applyPredicate(a);
            }
        };
    }

    public final static <A> FnPredicate<A> and(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(final A a)
            {
                return predicateA.applyPredicate(a) && predicateB.applyPredicate(a);
            }
        };
    }

    public final static <A> FnPredicate<A> and(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB,
            final FnPredicate<? super A> predicateC)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(final A a)
            {
                return predicateA.applyPredicate(a) && predicateB.applyPredicate(a) && predicateC.applyPredicate(a);
            }
        };
    }

    public final static <A> FnPredicate<A> or(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(final A a)
            {
                return predicateA.applyPredicate(a) || predicateB.applyPredicate(a);
            }
        };
    }

    public final static <A> FnPredicate<A> or(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB,
            final FnPredicate<? super A> predicateC)
    {
        return new FnPredicate<A>()
        {
            @Override
            public boolean applyPredicate(final A a)
            {
                return predicateA.applyPredicate(a) || predicateB.applyPredicate(a) || predicateC.applyPredicate(a);
            }
        };
    }
}
