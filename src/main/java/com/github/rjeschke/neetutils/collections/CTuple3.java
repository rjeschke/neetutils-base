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

import java.io.Serializable;

public class CTuple3<A extends Comparable<? super A>, B extends Comparable<? super B>, C extends Comparable<? super C>>
        implements Tuple3<A, B, C>, Comparable<CTuple3<A, B, C>>, Serializable
{
    private static final long serialVersionUID = -376103763132616543L;
    private final A a;
    private final B b;
    private final C c;

    public CTuple3(A a, B b, C c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public A a()
    {
        return this.a;
    }

    @Override
    public B b()
    {
        return this.b;
    }

    @Override
    public C c()
    {
        return this.c;
    }

    @Override
    public A _1()
    {
        return this.a;
    }

    @Override
    public B _2()
    {
        return this.b;
    }

    @Override
    public C _3()
    {
        return this.c;
    }

    @Override
    public int hashCode()
    {
        return (this.a.hashCode() * 31 + this.b.hashCode()) * 31 + this.c.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof CTuple3<?, ?, ?>))
        {
            return false;
        }
        CTuple3<?, ?, ?> tup = (CTuple3<?, ?, ?>)obj;
        return this.a.equals(tup.a) && this.b.equals(tup.b) && this.c.equals(tup.c);
    }

    @Override
    public int compareTo(CTuple3<A, B, C> o)
    {
        int cmp = this.a.compareTo(o.a);
        if(cmp != 0)
            return cmp;
        cmp = this.b.compareTo(o.b);
        if(cmp != 0)
            return cmp;
        return this.c.compareTo(o.c);
    }

    @Override
    public String toString()
    {
        return "{" + this.a.toString() + "," + this.b.toString() + "," + this.c.toString() + "}";
    }
}
