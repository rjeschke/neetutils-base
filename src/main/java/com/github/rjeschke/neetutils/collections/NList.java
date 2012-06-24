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

import java.util.Comparator;
import java.util.List;

public interface NList<E> extends List<E>
{
    public NList<E> sort();

    public NList<E> sort(Comparator<? super E> c);

    public NList<E> modify(ListModifier<E> modifier);

    public NList<E> operate(ListOperator<E> operator);

    public NList<E> filter(ListFilter<E> filter);

    public NList<NList<E>> split(ListFilter<E> filter);

    public E getFirst();

    public E getLast();

    public E removeFirst();

    public E removeLast();
}
