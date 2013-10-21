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
package com.github.rjeschke.neetutils.collections;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 * @param <B>
 */
public class Pair<A extends Comparable<A>, B extends Comparable<B>> extends Tuple<A, B> implements Comparable<Pair<A, B>>
{
    public Pair(final A a, final B b)
    {
        super(a, b);

        if (a == null) throw new IllegalArgumentException("First value of Pair is null");

        if (b == null) throw new IllegalArgumentException("Second value of Pair is null");
    }

    public final static <A extends Comparable<A>, B extends Comparable<B>> Pair<A, B> of(final A a, final B b)
    {
        return new Pair<>(a, b);
    }

    @Override
    public int hashCode()
    {
        return this.a.hashCode() * 31 + this.b.hashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;

        if (!(obj instanceof Pair)) return false;

        final Pair<?, ?> p = (Pair<?, ?>)obj;

        return this.a.equals(p.a) && this.b.equals(p.b);
    }

    @Override
    public int compareTo(final Pair<A, B> o)
    {
        final int t;

        if (this == o) return 0;

        if (o == null) return 1;

        return (t = this.a.compareTo(o.a)) != 0 ? t : this.b.compareTo(o.b);
    }
}
