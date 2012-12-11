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

public interface XIterable<A> extends Iterable<A>
{
    public XIterable<A> filter(FnPredicate<? super A> predicate);

    public <B> XIterable<B> map(FnMapping<? super A, B> mapping);

    public <B> B reduce(FnFoldStep<? super A, B> foldStep, B initialValue);

    public A reduce(FnFoldStep<? super A, A> foldStep);

    public <B> XIterable<B> reductions(FnFoldStep<? super A, B> foldStep, B initialValue);
    
    public XIterable<A> reductions(FnFoldStep<? super A, A> foldStep);
    
    public <B> XIterable<Tuple<A, B>> zip(Iterable<B> iterable);

    public <B, C> XIterable<C> zipWith(Iterable<B> iterable, FnCombine<? super A, ? super B, C> combine);

    public XIterable<A> take(int amount);

    public void consume();

    public XIterable<A> drop(int amount);

    public XIterable<A> tail();

    public XIterable<A> concat(Iterable<? extends A> iterable);
    
    public XIterable<A> concat(final Iterable<? extends A> ... iterables);
    
    public <B> XIterable<B> collect(Collector<? super A, B> collector);
    
    public List<A> asList();

    public List<A> asList(List<A> list);

    public Set<A> asSet();

    public Set<A> asSet(Set<A> set);

    public XIterable<A> interleave(Iterable<? extends A> iterable);
    
    public <B> Map<? super A, ? super B> asMapKV(Iterable<? extends B> iterable);

    public <B> Map<? super A, ? super B> asMapKV(Iterable<? extends B> iterable, Map<? super A, ? super B> list);

    public <B> Map<? super B, ? super A> asMapVK(Iterable<? extends B> iterable);

    public <B> Map<? super B, ? super A> asMapVK(Iterable<? extends B> iterable, Map<? super B, ? super A> list);
    
    public String asString();
}
