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
        if(iterable instanceof XIterable)
        {
            return (XIterable<A>)iterable;
        }
        return new XIterableGeneric<A>(iterable);
    }

    public final static <A> String asString(final Iterable<A> iterable)
    {
        final StringBuilder sb = new StringBuilder();
        for(final A a : iterable)
        {
            sb.append(a);
        }
        return sb.toString();
    }

    public final static <A> List<A> asList(final Iterable<A> iterable)
    {
        return asList(iterable, new ArrayList<A>());
    }

    public final static <A> List<A> asList(final Iterable<A> iterable, final List<A> list)
    {
        for(final A a : iterable)
        {
            list.add(a);
        }
        return list;
    }

    public final static <A> Set<A> asSet(final Iterable<A> iterable)
    {
        return asSet(iterable, new HashSet<A>());
    }

    public final static <A> Set<A> asSet(final Iterable<A> iterable, final Set<A> set)
    {
        for(final A a : iterable)
        {
            set.add(a);
        }
        return set;
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<Tuple<A, B>> iterable)
    {
        return asMap(iterable, new HashMap<A, B>());
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<Tuple<A, B>> iterable, final Map<A, B> map)
    {
        for(final Tuple<A, B> t : iterable)
        {
            map.put(t.a, t.b);
        }
        return map;
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<A> iterableA, final Iterable<B> iterableB)
    {
        return asMap(iterableA, iterableB, new HashMap<A, B>());
    }

    public final static <A, B> Map<A, B> asMap(final Iterable<A> iterableA, final Iterable<B> iterableB,
            final Map<A, B> map)
    {
        final Iterator<A> ai = iterableA.iterator();
        final Iterator<B> bi = iterableB.iterator();
        while(ai.hasNext() && bi.hasNext())
        {
            map.put(ai.next(), bi.next());
        }
        return map;
    }

    public final static <A, B> XIterable<B> collect(final Iterable<A> iterable, final Collector<A, B> collector)
    {
        return new XIterableCollect<A, B>(iterable, collector);
    }
    
    public final static <A> XIterable<A> take(final Iterable<A> iterable, final int amount)
    {
        return new XIterableTake<A>(iterable, amount);
    }

    public final static <A> void consume(final Iterable<A> iterable)
    {
        final Iterator<A> i = iterable.iterator();
        while(i.hasNext())
        {
            i.next();
        }
    }
    
    public final static <A> XIterable<A> drop(final Iterable<A> iterable, final int amount)
    {
        return new XIterableDrop<A>(iterable, amount);
    }

    public final static <A> XIterable<A> concat(final Iterable<? extends A> iterableA, final Iterable<? extends A> iterableB)
    {
        return new XIterableConcat<A>(iterableA, iterableB);
    }

    public final static <A> XIterable<A> concat(final Iterable<? extends A> ... iterables)
    {
        return new XIterableConcat2<A>(ArrayIterator.unsafeOf(iterables));
    }
    
    public final static <A> XIterable<A> concat(final Iterable<? extends A> iterable, final Iterable<? extends A> ... iterables)
    {
        return new XIterableConcat<A>(iterable, new XIterableConcat2<A>(ArrayIterator.unsafeOf(iterables)));
    }
    
    public final static <A> XIterable<A> concat(final Iterable<? extends Iterable<? extends A>> iterable)
    {
        return new XIterableConcat2<A>(iterable);
    }
    
    public final static <A> XIterable<A> filter(final Iterable<A> iterable, final FnPredicate<A> predicate)
    {
        return new XIterableFilter<A>(iterable, predicate);
    }

    public final static <A, B> XIterable<B> map(final Iterable<A> iterable, final FnMapping<A, B> mapping)
    {
        return new XIterableMap<A, B>(iterable, mapping);
    }

    public final static <A, B> B reduce(final Iterable<A> iterable, final FnFoldStep<A, B> foldStep,
            final B initialValue)
    {
        B b = initialValue;
        for(final A a : iterable)
        {
            b = foldStep.applyFoldStep(a, b);
        }
        return b;
    }

    public final static <A> XIterable<A> interleave(final Iterable<A> iterableA, final Iterable<A> iterableB)
    {
        return new XIterableInterleave<A>(iterableA, iterableB);
    }

    public final static <A, B> XIterable<Tuple<A, B>> zip(final Iterable<A> iterableA, final Iterable<B> iterableB)
    {
        return new XIterableZip<A, B>(iterableA, iterableB);
    }

    public final static <A, B, C> XIterable<C> zipWith(final Iterable<A> iterableA, final Iterable<B> iterableB,
            final FnCombine<A, B, C> combine)
    {
        return new XIterableZipWith<A, B, C>(iterableA, iterableB, combine);
    }

    public final static <A> String iterableToString(final Iterable<A> iterable)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(final A a : iterable)
        {
            if(sb.length() > 1)
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
