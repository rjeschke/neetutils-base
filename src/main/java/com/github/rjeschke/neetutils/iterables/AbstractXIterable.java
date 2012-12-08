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
package com.github.rjeschke.neetutils.iterables;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rjeschke.neetutils.collections.Tuple;
import com.github.rjeschke.neetutils.fn.FnCombine;
import com.github.rjeschke.neetutils.fn.FnFoldStep;
import com.github.rjeschke.neetutils.fn.FnMapping;
import com.github.rjeschke.neetutils.fn.FnPredicate;

public abstract class AbstractXIterable<A> implements XIterable<A>
{
    @Override
    public XIterable<A> filter(FnPredicate<A> predicate)
    {
        return Iterables.filter(this, predicate);
    }

    @Override
    public <B> XIterable<B> map(FnMapping<A, B> mapping)
    {
        return Iterables.map(this, mapping);
    }

    @Override
    public <B> B reduce(FnFoldStep<A, B> foldStep, B initialValue)
    {
        return Iterables.reduce(this, foldStep, initialValue);
    }

    @Override
    public <B> XIterable<Tuple<A, B>> zip(Iterable<B> iterable)
    {
        return Iterables.zip(this, iterable);
    }

    @Override
    public <B, C> XIterable<C> zipWith(Iterable<B> iterable, FnCombine<A, B, C> combine)
    {
        return Iterables.zipWith(this, iterable, combine);
    }

    @Override
    public XIterable<A> take(int amount)
    {
        return Iterables.take(this, amount);
    }

    @Override
    public XIterable<A> drop(int amount)
    {
        return Iterables.drop(this, amount);
    }

    @Override
    public XIterable<A> concat(Iterable<A> iterable)
    {
        return Iterables.concat(this, iterable);
    }
    
    @Override
    public XIterable<A> interleave(Iterable<A> iterable)
    {
        return Iterables.interleave(this, iterable);
    }
    
    @Override
    public List<A> asList()
    {
        return Iterables.asList(this);
    }

    @Override
    public List<A> asList(List<A> list)
    {
        return Iterables.asList(this, list);
    }

    @Override
    public Set<A> asSet()
    {
        return Iterables.asSet(this);
    }

    @Override
    public Set<A> asSet(Set<A> set)
    {
        return Iterables.asSet(this, set);
    }

    @Override
    public <B> Map<A, B> asMapKV(Iterable<B> iterable)
    {
        return Iterables.asMap(this, iterable);
    }

    @Override
    public <B> Map<A, B> asMapKV(Iterable<B> iterable, Map<A, B> map)
    {
        return Iterables.asMap(this, iterable, map);
    }

    @Override
    public <B> Map<B, A> asMapVK(Iterable<B> iterable)
    {
        return Iterables.asMap(iterable, this);
    }

    @Override
    public <B> Map<B, A> asMapVK(Iterable<B> iterable, Map<B, A> map)
    {
        return Iterables.asMap(iterable, this, map);
    }

    @Override
    public String asString()
    {
        return Iterables.asString(this);
    }
    
    @Override
    public String toString()
    {
        return Iterables.iterableToString(this);
    }
}
