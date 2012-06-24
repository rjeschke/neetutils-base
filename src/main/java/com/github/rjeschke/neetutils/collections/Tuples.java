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

public class Tuples
{
    public static <A, B> NTuple2<A, B> n(A a, B b)
    {
        return new NTuple2<A, B>(a, b);
    }

    public static <A, B, C> NTuple3<A, B, C> n(A a, B b, C c)
    {
        return new NTuple3<A, B, C>(a, b, c);
    }

    public static <A, B, C, D> NTuple4<A, B, C, D> n(A a, B b, C c, D d)
    {
        return new NTuple4<A, B, C, D>(a, b, c, d);
    }

    public static <A extends Comparable<? super A>, B extends Comparable<? super B>> CTuple2<A, B> c(A a, B b)
    {
        return new CTuple2<A, B>(a, b);
    }

    public static <A extends Comparable<? super A>, B extends Comparable<? super B>, C extends Comparable<? super C>> CTuple3<A, B, C> c(
            A a, B b, C c)
    {
        return new CTuple3<A, B, C>(a, b, c);
    }

    public static <A extends Comparable<? super A>, B extends Comparable<? super B>, C extends Comparable<? super C>, D extends Comparable<? super D>> CTuple4<A, B, C, D> c(
            A a, B b, C c, D d)
    {
        return new CTuple4<A, B, C, D>(a, b, c, d);
    }
}
