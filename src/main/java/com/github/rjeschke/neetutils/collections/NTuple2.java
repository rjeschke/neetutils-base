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

public class NTuple2<A, B> implements Serializable, Tuple2<A, B>
{
    private static final long serialVersionUID = -1883584188678434246L;
    private final A a;
    private final B b;

    public NTuple2(A a, B b)
    {
        this.a = a;
        this.b = b;
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
    public int hashCode()
    {
        return this.a.hashCode() * 31 + this.b.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof NTuple2<?, ?>))
        {
            return false;
        }
        NTuple2<?, ?> tup = (NTuple2<?, ?>)obj;
        return this.a.equals(tup.a) && this.b.equals(tup.b);
    }

    @Override
    public String toString()
    {
        return "{" + this.a.toString() + "," + this.b.toString() + "}";
    }
}
