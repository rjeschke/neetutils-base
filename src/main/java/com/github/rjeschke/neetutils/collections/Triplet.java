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

import com.github.rjeschke.neetutils.Objects;

public class Triplet<A, B, C>
{
    public final A a;
    public final B b;
    public final C c;

    public Triplet(final A a, final B b, final C c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public final static <A, B, C> Triplet<A, B, C> of(final A a, final B b, final C c)
    {
        return new Triplet<>(a, b, c);
    }

    @Override
    public int hashCode()
    {
        return ((this.a == null ? 0 : this.a.hashCode()) * 31 + (this.b == null ? 0 : this.b.hashCode())) * 31 + (this.c == null ? 0 : this.c.hashCode());
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;

        if (!(obj instanceof Triplet)) return false;

        final Triplet<?, ?, ?> p = (Triplet<?, ?, ?>)obj;

        return Objects.equals(this.a, p.a) && Objects.equals(this.b, p.b) && Objects.equals(this.c, p.c);
    }

    @Override
    public String toString()
    {
        return "(" + this.a.toString() + ", " + this.b.toString() + ", " + this.c.toString() + ")";
    }
}
