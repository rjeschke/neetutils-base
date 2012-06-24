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

public class NTuple4<A, B, C, D> implements Tuple4<A, B, C, D>, Serializable
{
    private static final long serialVersionUID = 2969445247487288094L;
    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public NTuple4(A a, B b, C c, D d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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
    public D d()
    {
        return this.d;
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
    public D _4()
    {
        return this.d;
    }

    @Override
    public int hashCode()
    {
        return ((this.a.hashCode() * 31 + this.b.hashCode()) * 31 + this.c.hashCode()) * 31 + this.d.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof NTuple4<?, ?, ?, ?>))
        {
            return false;
        }
        NTuple4<?, ?, ?, ?> tup = (NTuple4<?, ?, ?, ?>)obj;
        return this.a.equals(tup.a) && this.b.equals(tup.b) && this.c.equals(tup.c) && this.d.equals(tup.d);
    }

    @Override
    public String toString()
    {
        return "{" + this.a.toString() + "," + this.b.toString() + "," + this.c.toString() + "," + this.d.toString()
                + "}";
    }
}
