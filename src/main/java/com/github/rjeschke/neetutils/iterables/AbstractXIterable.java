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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 */
public abstract class AbstractXIterable<A> implements XIterable<A>
{
    @Override
    public XIterable<A> filter(final FnPredicate<? super A> predicate)
    {
        return Iterables.filter(this, predicate);
    }

    @Override
    public <B> XIterable<B> map(final FnMapping<? super A, B> mapping)
    {
        return Iterables.map(this, mapping);
    }

    @Override
    public <B> B reduce(final FnFoldStep<? super A, B> foldStep, final B initialValue)
    {
        return Iterables.reduce(this, foldStep, initialValue);
    }

    @Override
    public A reduce(final FnFoldStep<? super A, A> foldStep)
    {
        return Iterables.reduce(this, foldStep);
    }

    @Override
    public XIterable<A> reductions(final FnFoldStep<? super A, A> foldStep)
    {
        return Iterables.reductions(this, foldStep);
    }

    @Override
    public <B> XIterable<B> reductions(final FnFoldStep<? super A, B> foldStep, final B initialValue)
    {
        return Iterables.reductions(this, foldStep, initialValue);
    }

    @Override
    public <B> XIterable<Tuple<A, B>> zip(final Iterable<B> iterable)
    {
        return Iterables.zip(this, iterable);
    }

    @Override
    public <B, C> XIterable<C> zipWith(final Iterable<B> iterable, final FnCombine<? super A, ? super B, C> combine)
    {
        return Iterables.zipWith(this, iterable, combine);
    }

    @Override
    public <B> XIterable<B> collect(final Collector<? super A, B> collector)
    {
        return Iterables.collect(this, collector);
    }

    @Override
    public XIterable<A> take(final int amount)
    {
        return Iterables.take(this, amount);
    }

    @Override
    public void consume()
    {
        Iterables.consume(this);
    }

    @Override
    public XIterable<A> drop(final int amount)
    {
        return Iterables.drop(this, amount);
    }

    @Override
    public XIterable<A> tail()
    {
        return Iterables.drop(this, 1);
    }

    @Override
    public XIterable<A> concat(final Iterable<? extends A> iterable)
    {
        return Iterables.concat(this, iterable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public XIterable<A> concat(final Iterable<? extends A>... iterables)
    {
        return Iterables.concat(this, iterables);
    }

    @Override
    public XIterable<A> interleave(final Iterable<? extends A> iterable)
    {
        return Iterables.interleave(this, iterable);
    }

    @Override
    public List<A> asList()
    {
        return Iterables.asList(this);
    }

    @Override
    public List<A> asList(final List<A> list)
    {
        return Iterables.asList(this, list);
    }

    @Override
    public Set<A> asSet()
    {
        return Iterables.asSet(this);
    }

    @Override
    public Set<A> asSet(final Set<A> set)
    {
        return Iterables.asSet(this, set);
    }

    @Override
    public <B> Map<? super A, ? super B> asMapKV(final Iterable<? extends B> iterable)
    {
        return Iterables.asMap(this, iterable);
    }

    @Override
    public <B> Map<? super A, ? super B> asMapKV(final Iterable<? extends B> iterable, final Map<? super A, ? super B> map)
    {
        return Iterables.asMap(this, iterable, map);
    }

    @Override
    public <B> Map<? super B, ? super A> asMapVK(final Iterable<? extends B> iterable)
    {
        return Iterables.asMap(iterable, this);
    }

    @Override
    public <B> Map<? super B, ? super A> asMapVK(final Iterable<? extends B> iterable, final Map<? super B, ? super A> map)
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
