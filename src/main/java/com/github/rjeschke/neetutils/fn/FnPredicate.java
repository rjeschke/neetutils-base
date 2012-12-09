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

public abstract class FnPredicate<A>
{
    public abstract boolean applyPredicate(A a);
    
    public final FnPredicate<A> invert()
    {
        return Predicates.not(this);
    }
    
    public final FnPredicate<A> and(final FnPredicate<? super A> predicate)
    {
        return Predicates.and(this, predicate);
    }

    public final FnPredicate<A> and(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB)
    {
        return Predicates.and(this, predicateA, predicateB);
    }

    public final FnPredicate<A> or(final FnPredicate<? super A> predicate)
    {
        return Predicates.or(this, predicate);
    }
    
    public final FnPredicate<A> or(final FnPredicate<? super A> predicateA, final FnPredicate<? super A> predicateB)
    {
        return Predicates.or(this, predicateA, predicateB);
    }
}
