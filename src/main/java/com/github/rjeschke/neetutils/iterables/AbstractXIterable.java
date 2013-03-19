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
    public XIterable<A> filter(FnPredicate<? super A> predicate)
    {
        return Iterables.filter(this, predicate);
    }

    @Override
    public <B> XIterable<B> map(FnMapping<? super A, B> mapping)
    {
        return Iterables.map(this, mapping);
    }

    @Override
    public <B> B reduce(FnFoldStep<? super A, B> foldStep, B initialValue)
    {
        return Iterables.reduce(this, foldStep, initialValue);
    }

    @Override
    public A reduce(FnFoldStep<? super A, A> foldStep)
    {
        return Iterables.reduce(this, foldStep);
    }

    @Override
    public XIterable<A> reductions(FnFoldStep<? super A, A> foldStep)
    {
        return Iterables.reductions(this, foldStep);
    }

    @Override
    public <B> XIterable<B> reductions(FnFoldStep<? super A, B> foldStep, B initialValue)
    {
        return Iterables.reductions(this, foldStep, initialValue);
    }

    @Override
    public <B> XIterable<Tuple<A, B>> zip(Iterable<B> iterable)
    {
        return Iterables.zip(this, iterable);
    }

    @Override
    public <B, C> XIterable<C> zipWith(Iterable<B> iterable, FnCombine<? super A, ? super B, C> combine)
    {
        return Iterables.zipWith(this, iterable, combine);
    }

    @Override
    public <B> XIterable<B> collect(Collector<? super A, B> collector)
    {
        return Iterables.collect(this, collector);
    }

    @Override
    public XIterable<A> take(int amount)
    {
        return Iterables.take(this, amount);
    }

    @Override
    public void consume()
    {
        Iterables.consume(this);
    }

    @Override
    public XIterable<A> drop(int amount)
    {
        return Iterables.drop(this, amount);
    }

    @Override
    public XIterable<A> tail()
    {
        return Iterables.drop(this, 1);
    }

    @Override
    public XIterable<A> concat(Iterable<? extends A> iterable)
    {
        return Iterables.concat(this, iterable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public XIterable<A> concat(Iterable<? extends A>... iterables)
    {
        return Iterables.concat(this, iterables);
    }

    @Override
    public XIterable<A> interleave(Iterable<? extends A> iterable)
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
    public <B> Map<? super A, ? super B> asMapKV(Iterable<? extends B> iterable)
    {
        return Iterables.asMap(this, iterable);
    }

    @Override
    public <B> Map<? super A, ? super B> asMapKV(Iterable<? extends B> iterable, Map<? super A, ? super B> map)
    {
        return Iterables.asMap(this, iterable, map);
    }

    @Override
    public <B> Map<? super B, ? super A> asMapVK(Iterable<? extends B> iterable)
    {
        return Iterables.asMap(iterable, this);
    }

    @Override
    public <B> Map<? super B, ? super A> asMapVK(Iterable<? extends B> iterable, Map<? super B, ? super A> map)
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
