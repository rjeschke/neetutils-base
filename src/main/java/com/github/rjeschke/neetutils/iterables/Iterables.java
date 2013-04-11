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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.rjeschke.neetutils.collections.Tuple;
import com.github.rjeschke.neetutils.fn.FnCombine;
import com.github.rjeschke.neetutils.fn.FnFoldStep;
import com.github.rjeschke.neetutils.fn.FnMapping;
import com.github.rjeschke.neetutils.fn.FnPredicate;

public final class Iterables
{
    private Iterables()
    {
        // 1up
    }

    public final static <A> XIterable<A> asXIterable(final Iterable<A> iterable)
    {
        if (iterable instanceof XIterable)
        {
            return (XIterable<A>)iterable;
        }
        return new XIterableGeneric<>(iterable);
    }

    public final static <A> String asString(final Iterable<A> iterable)
    {
        final StringBuilder sb = new StringBuilder();
        for (final A a : iterable)
        {
            sb.append(a);
        }
        return sb.toString();
    }

    public final static <A> List<A> asList(final Iterable<? extends A> iterable)
    {
        return asList(iterable, new ArrayList<A>());
    }

    public final static <A> List<A> asList(final Iterable<? extends A> iterable, final List<A> list)
    {
        for (final A a : iterable)
        {
            list.add(a);
        }
        return list;
    }

    public final static <A> Set<A> asSet(final Iterable<? extends A> iterable)
    {
        return asSet(iterable, new HashSet<A>());
    }

    public final static <A> Set<A> asSet(final Iterable<? extends A> iterable, final Set<A> set)
    {
        for (final A a : iterable)
        {
            set.add(a);
        }
        return set;
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<? extends Tuple<? extends A, ? extends B>> iterable)
    {
        return asMap(iterable, new HashMap<A, B>());
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<? extends Tuple<? extends A, ? extends B>> iterable,
            final Map<A, B> map)
    {
        for (final Tuple<? extends A, ? extends B> t : iterable)
        {
            map.put(t.a, t.b);
        }
        return map;
    }

    public final static <A, B> Map<? super A, ? super B> asMap(final Iterable<? extends A> iterableA,
            final Iterable<? extends B> iterableB)
    {
        return asMap(iterableA, iterableB, new HashMap<A, B>());
    }

    public final static <A, B> Map<? super A, ? super B> asMap(final Iterable<? extends A> iterableA,
            final Iterable<? extends B> iterableB, final Map<? super A, ? super B> map)
    {
        final Iterator<? extends A> ai = iterableA.iterator();
        final Iterator<? extends B> bi = iterableB.iterator();
        while (ai.hasNext() && bi.hasNext())
        {
            map.put(ai.next(), bi.next());
        }
        return map;
    }

    public final static <A, B> XIterable<B> collect(final Iterable<A> iterable, final Collector<? super A, B> collector)
    {
        return new XIterableCollect<>(iterable, collector);
    }

    public final static <A> XIterable<A> take(final Iterable<A> iterable, final int amount)
    {
        return new XIterableTake<>(iterable, amount);
    }

    public final static <A> void consume(final Iterable<A> iterable)
    {
        final Iterator<A> i = iterable.iterator();
        while (i.hasNext())
        {
            i.next();
        }
    }

    public final static <A> XIterable<A> drop(final Iterable<A> iterable, final int amount)
    {
        return new XIterableDrop<>(iterable, amount);
    }

    public final static <A> XIterable<A> concat(final Iterable<? extends A> iterableA, final Iterable<? extends A> iterableB)
    {
        return new XIterableConcat<>(iterableA, iterableB);
    }

    @SafeVarargs
    public final static <A> XIterable<A> concat(final Iterable<? extends A>... iterables)
    {
        return new XIterableConcat2<>(ArrayIterator.unsafeOf(iterables));
    }

    @SafeVarargs
    public final static <A> XIterable<A> concat(final Iterable<? extends A> iterable, final Iterable<? extends A>... iterables)
    {
        return new XIterableConcat<>(iterable, new XIterableConcat2<>(ArrayIterator.unsafeOf(iterables)));
    }

    public final static <A> XIterable<A> concat(final Iterable<? extends Iterable<? extends A>> iterable)
    {
        return new XIterableConcat2<>(iterable);
    }

    public final static <A> XIterable<A> filter(final Iterable<? extends A> iterable, final FnPredicate<? super A> predicate)
    {
        return new XIterableFilter<>(iterable, predicate);
    }

    public final static <A, B> XIterable<B> map(final Iterable<? extends A> iterable, final FnMapping<A, B> mapping)
    {
        return new XIterableMap<>(iterable, mapping);
    }

    public final static <A, B> B reduce(final Iterable<A> iterable, final FnFoldStep<? super A, B> foldStep, final B initialValue)
    {
        B b = initialValue;
        for (final A a : iterable)
        {
            b = foldStep.applyFoldStep(a, b);
        }
        return b;
    }

    public final static <A, B> XIterable<B> reductions(final Iterable<A> iterable, final FnFoldStep<? super A, B> foldStep,
            final B initialValue)
    {
        return new XIterableReductions<>(iterable, foldStep, initialValue);
    }

    public final static <A> A reduce(final Iterable<A> iterable, final FnFoldStep<? super A, A> foldStep)
    {
        A a = null;
        final Iterator<A> iterator = iterable.iterator();
        if (iterator.hasNext())
        {
            a = iterator.next();
            while (iterator.hasNext())
            {
                a = foldStep.applyFoldStep(iterator.next(), a);
            }
        }
        return a;
    }

    public final static <A> XIterable<A> reductions(final Iterable<? extends A> iterable, final FnFoldStep<? super A, A> foldStep)
    {
        return new XIterableReductions2<>(iterable, foldStep);
    }

    public final static <A> XIterable<A> interleave(final Iterable<? extends A> iterableA, final Iterable<? extends A> iterableB)
    {
        return new XIterableInterleave<>(iterableA, iterableB);
    }

    public final static <A, B> XIterable<Tuple<A, B>> zip(final Iterable<A> iterableA, final Iterable<B> iterableB)
    {
        return new XIterableZip<>(iterableA, iterableB);
    }

    public final static <A, B, C> XIterable<C> zipWith(final Iterable<A> iterableA, final Iterable<B> iterableB,
            final FnCombine<? super A, ? super B, C> combine)
    {
        return new XIterableZipWith<>(iterableA, iterableB, combine);
    }

    public final static <A> String iterableToString(final Iterable<A> iterable)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (final A a : iterable)
        {
            if (sb.length() > 1)
            {
                sb.append(", ");
            }
            sb.append(a.toString());
        }
        sb.append(']');
        return sb.toString();
    }

    public final static <A> Iterable<A> constant(final A value)
    {
        return new Iterable<A>()
        {
            @Override
            public Iterator<A> iterator()
            {
                return new Iterator<A>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return true;
                    }

                    @Override
                    public A next()
                    {
                        return value;
                    }

                    @Override
                    public void remove()
                    {
                        throw new IllegalStateException("Constant iterators are read-only.");
                    }
                };
            }
        };
    }
}
