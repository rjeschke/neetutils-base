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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.TreeMap;
import java.util.Vector;

import com.github.rjeschke.neetutils.fn.FnCombine;
import com.github.rjeschke.neetutils.fn.FnEquals;
import com.github.rjeschke.neetutils.fn.FnFoldStep;
import com.github.rjeschke.neetutils.fn.FnMapping;
import com.github.rjeschke.neetutils.fn.FnPredicate;
import com.github.rjeschke.neetutils.fn.Fns;

/**
 * Some handy collection methods.
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class Colls
{
    private Colls()
    {
        //
    }

    /**
     * Creates a new instance of the default List type.
     *
     * @return A List instance.
     */
    public final static <A> List<A> list()
    {
        return new ArrayList<>();
    }

    /**
     * Creates a new instance of the default List type.
     *
     * @param initialSize
     *            Initial list size.
     * @return A List instance.
     */
    public final static <A> List<A> list(final int initialSize)
    {
        return new ArrayList<>(initialSize);
    }

    /**
     * Creates a new instance of the default List type.
     *
     * @param coll
     *            Collection of values to populate List with.
     * @return A List instance.
     */
    public final static <A> List<A> list(final Collection<A> coll)
    {
        return new ArrayList<>(coll);
    }

    /**
     * Creates a new instance of the default List type.
     *
     * @param coll
     *            Collection of values to populate List with.
     * @return A List instance.
     */
    public final static <A> List<A> list(final Iterable<A> coll)
    {
        final List<A> ret = list();
        for (final A a : coll)
            ret.add(a);
        return ret;
    }

    /**
     * Creates a new instance of the default List type.
     *
     * @param coll
     *            Array of values to populate List with.
     * @return A List instance.
     */
    @SafeVarargs
    public final static <A> List<A> list(final A... coll)
    {
        final List<A> ret = list(coll.length);
        for (int i = 0; i < coll.length; i++)
            ret.add(coll[i]);
        return ret;
    }

    /**
     * Creates a new instance of the default ImmutableList type.
     *
     * @param coll
     *            Collection of values to populate List with.
     * @return A List instance.
     */
    public final static <A> List<A> ilist(final Collection<A> coll)
    {
        return new ImmutableList<>(coll);
    }

    /**
     * Creates a new instance of the default ImmutableList type.
     *
     * @param coll
     *            Collection of values to populate List with.
     * @return A List instance.
     */
    public final static <A> List<A> ilist(final Iterable<A> coll)
    {
        return new ImmutableList<>(coll);
    }

    /**
     * Creates a new instance of the default ImmutableList type.
     *
     * @param coll
     *            Array of values to populate List with.
     * @return A List instance.
     */
    @SafeVarargs
    public final static <A> List<A> ilist(final A... coll)
    {
        return new ImmutableList<>(coll);
    }

    public final static <A> Iterable<Tuple<Integer, A>> idxIter(final Collection<A> coll)
    {
        return new Iterable<Tuple<Integer, A>>()
        {
            final Collection<A> collection = coll;

            @Override
            public Iterator<Tuple<Integer, A>> iterator()
            {
                return new IndexedIterator<>(this.collection.iterator());
            }
        };
    }

    public final static <A> Iterable<Tuple<Integer, A>> idxIter(final Iterable<A> iter)
    {
        return new Iterable<Tuple<Integer, A>>()
        {
            final Iterable<A> iterable = iter;

            @Override
            public Iterator<Tuple<Integer, A>> iterator()
            {
                return new IndexedIterator<>(this.iterable.iterator());
            }
        };
    }

    /**
     * Tries to call trimToSize() on the given list. If no suitable type is found, then this method is a no-op.
     *
     * @param list
     *            The list
     * @return The same list
     * @see Vector
     * @see ArrayList
     * @see SortedList
     */
    public final static <A> List<A> trimToSize(final List<A> list)
    {
        if (list instanceof Vector)
            ((Vector<?>)list).trimToSize();
        else if (list instanceof ArrayList)
            ((ArrayList<?>)list).trimToSize();
        else if (list instanceof SortedList) ((SortedList<?>)list).trimToSize();

        return list;
    }

    /**
     * Transforms a Map into a list of Tuples.
     *
     * @param map
     *            Map to transform.
     * @return List of Tuples.
     */
    public final static <A, B> List<Tuple<A, B>> asList(final Map<A, B> map)
    {
        final List<Tuple<A, B>> ret = list();
        for (final Map.Entry<A, B> e : map.entrySet())
            ret.add(Tuple.of(e.getKey(), e.getValue()));
        return ret;
    }

    /**
     * Puts the given key/value pairs into the given map.
     *
     * @param keys
     *            Collection of keys.
     * @param values
     *            Collection of values.
     * @param map
     *            Map to populate.
     * @return The supplied map argument.
     */
    public final static <A, B> Map<A, B> intoMap(final Iterable<A> keys, final Iterable<B> values, final Map<A, B> map)
    {
        final Iterator<A> a = keys.iterator();
        final Iterator<B> b = values.iterator();
        while (a.hasNext() && b.hasNext())
            map.put(a.next(), b.next());

        return map;
    }

    /**
     * Puts the given key/value pairs into the given map.
     *
     * @param keyValues
     *            Collection of keys.
     * @param map
     *            Map to populate.
     * @return The supplied map argument.
     */
    public final static <A, B> Map<A, B> intoMap(final Iterable<Tuple<A, B>> keyValues, final Map<A, B> map)
    {
        for (final Tuple<A, B> t : keyValues)
            map.put(t.a, t.b);
        return map;
    }

    /**
     * Puts the given key/value pairs into a HashMap.
     *
     * @param keys
     *            Collection of keys.
     * @param values
     *            Collection of values.
     * @return The HashMap.
     * @see HashMap
     */
    public final static <A, B> Map<A, B> toHashMap(final Iterable<A> keys, final Iterable<B> values)
    {
        return intoMap(keys, values, new HashMap<A, B>());
    }

    /**
     * Puts the given key/value pairs into a HashMap.
     *
     * @param keyValues
     *            Collection of keys.
     * @return The HashMap.
     * @see HashMap
     */
    public final static <A, B> Map<A, B> toHashMap(final Iterable<Tuple<A, B>> keyValues)
    {
        return intoMap(keyValues, new HashMap<A, B>());
    }

    /**
     * Puts the given key/value pairs into a TreeMap.
     *
     * @param keys
     *            Collection of keys.
     * @param values
     *            Collection of values.
     * @return The TreeMap.
     * @see TreeMap
     */
    public final static <A, B> Map<A, B> toTreeMap(final Iterable<A> keys, final Iterable<B> values)
    {
        return intoMap(keys, values, new TreeMap<A, B>());
    }

    /**
     * Puts the given key/value pairs into a TreeMap.
     *
     * @param keyValues
     *            Collection of keys.
     * @return The TreeMap.
     * @see TreeMap
     */
    public final static <A, B> Map<A, B> toTreeMap(final Iterable<Tuple<A, B>> keyValues)
    {
        return intoMap(keyValues, new TreeMap<A, B>());
    }

    /**
     * Puts the given key/value pairs into a LinkedHashMap.
     *
     * @param keys
     *            Collection of keys.
     * @param values
     *            Collection of values.
     * @return The LinkedHashMap.
     * @see LinkedHashMap
     */
    public final static <A, B> Map<A, B> toLinkedHashMap(final Iterable<A> keys, final Iterable<B> values)
    {
        return intoMap(keys, values, new LinkedHashMap<A, B>());
    }

    /**
     * Puts the given key/value pairs into a LinkedHashMap.
     *
     * @param keyValues
     *            Collection of keys.
     * @return The LinkedHashMap.
     * @see LinkedHashMap
     */
    public final static <A, B> Map<A, B> toLinkedHashMap(final Iterable<Tuple<A, B>> keyValues)
    {
        return intoMap(keyValues, new LinkedHashMap<A, B>());
    }

    /**
     * Sorts the given List.
     *
     * @param list
     *            The list to sort.
     * @return The sorted list.
     */
    public final static <A extends Comparable<A>> List<A> sort(final List<A> list)
    {
        Collections.sort(list);
        return list;
    }

    /**
     * Returns the first element of the given collection.
     *
     * @param coll
     *            The collection.
     * @return The first element.
     */
    public final static <A> A head(final Iterable<A> coll)
    {
        return coll.iterator().next();
    }

    /**
     * Returns the first element of the given collection.
     *
     * @param coll
     *            The collection.
     * @return The first element.
     */
    public final static <A> A head(final List<A> coll)
    {
        return coll.get(0);
    }

    /**
     * Returns the last element of the given List.
     *
     * @param coll
     *            The list.
     * @return The first element.
     */
    public final static <A> A last(final List<A> coll)
    {
        return coll.get(coll.size() - 1);
    }

    /**
     * Returns a new List containing all but the first element of 'coll'.
     *
     * @param coll
     *            The collection.
     * @return The list.
     */
    public final static <A> List<A> tail(final Collection<A> coll)
    {
        return drop(coll, 1);
    }

    /**
     * Returns a new List containing all but the first element of 'coll'.
     *
     * @param coll
     *            The collection.
     * @return The list.
     */
    public final static <A> List<A> tail(final Iterable<A> coll)
    {
        return drop(coll, 1);
    }

    /**
     * Returns a new list containing the first 'amount' elements of the given collection.
     *
     * @param coll
     *            The collection.
     * @param amount
     *            Elements to take.
     * @return The list.
     */
    public final static <A> List<A> take(final Collection<A> coll, final int amount)
    {
        if (amount >= coll.size()) return list(coll);

        final List<A> ret = list(amount);
        final Iterator<A> it = coll.iterator();
        for (int i = 0; i < amount; i++)
            ret.add(it.next());

        return ret;
    }

    /**
     * Returns a new list containing the first 'amount' elements of the given collection.
     *
     * @param coll
     *            The collection.
     * @param amount
     *            Elements to take.
     * @return The list.
     */
    public final static <A> List<A> take(final Iterable<A> coll, final int amount)
    {
        final List<A> ret = list();
        final Iterator<A> it = coll.iterator();
        int i = 0;
        while (i++ < amount && it.hasNext())
            ret.add(it.next());

        return ret;
    }

    /**
     * Returns a new List containing all but the first 'amount' elements of 'coll'.
     *
     * @param coll
     *            The collection.
     * @param amount
     *            Number of element to leave out.
     * @return The list.
     */
    public final static <A> List<A> drop(final Collection<A> coll, final int amount)
    {
        if (amount >= coll.size()) return list();

        final int toTake = coll.size() - amount;
        final List<A> ret = list(toTake);
        final Iterator<A> it = coll.iterator();
        for (int i = 0; i < amount; i++)
            it.next();
        for (int i = 0; i < toTake; i++)
            ret.add(it.next());

        return ret;
    }

    /**
     * Returns a new List containing all but the first 'amount' elements of 'coll'.
     *
     * @param coll
     *            The collection.
     * @param amount
     *            Number of element to leave out.
     * @return The list.
     */
    public final static <A> List<A> drop(final Iterable<A> coll, final int amount)
    {
        final List<A> ret = list();
        final Iterator<A> it = coll.iterator();
        int i = 0;
        while (i++ < amount && it.hasNext())
            it.next();
        while (it.hasNext())
            ret.add(it.next());

        return ret;
    }

    public final static <A, B> List<B> map(final Collection<A> coll, final FnMapping<A, B> fn)
    {
        final List<B> l = list(coll.size());
        for (final A a : coll)
            l.add(fn.applyMapping(a));
        return l;
    }

    public final static <A, B> List<B> map(final Iterable<A> coll, final FnMapping<A, B> fn)
    {
        final List<B> l = list();
        for (final A a : coll)
            l.add(fn.applyMapping(a));
        return l;
    }

    public final static <A> List<A> filter(final Iterable<A> coll, final FnPredicate<A> fn)
    {
        final List<A> l = list();
        for (final A a : coll)
        {
            if (fn.applyPredicate(a)) l.add(a);
        }
        return l;
    }

    public final static <A, B> List<B> filterMap(final Iterable<A> coll, final FnPredicate<A> fnPredicate, final FnMapping<A, B> fnMap)
    {
        final List<B> l = list();
        for (final A a : coll)
        {
            if (fnPredicate.applyPredicate(a)) l.add(fnMap.applyMapping(a));
        }
        return l;
    }

    public final static <A, B> List<B> mapFilter(final Iterable<A> coll, final FnMapping<A, B> fnMap, final FnPredicate<B> fnPredicate)
    {
        final List<B> l = list();
        for (final A a : coll)
        {
            final B b = fnMap.applyMapping(a);
            if (fnPredicate.applyPredicate(b)) l.add(b);
        }
        return l;
    }

    public final static <A, B, C> C mapReduce(final Iterable<A> coll, final FnMapping<A, B> fnMap, final FnFoldStep<B, C> fnReduce, final C initial)
    {
        C c = initial;
        for (final A a : coll)
            c = fnReduce.applyFoldStep(fnMap.applyMapping(a), c);
        return c;
    }

    public final static <A, B, C> C filterMapReduce(final Iterable<A> coll, final FnPredicate<A> fnPredicate, final FnMapping<A, B> fnMap,
            final FnFoldStep<B, C> fnReduce, final C initial)
    {
        C c = initial;
        for (final A a : coll)
        {
            if (fnPredicate.applyPredicate(a)) c = fnReduce.applyFoldStep(fnMap.applyMapping(a), c);
        }
        return c;
    }

    public final static <A, B, C> C mapFilterReduce(final Iterable<A> coll, final FnMapping<A, B> fnMap, final FnPredicate<B> fnPredicate,
            final FnFoldStep<B, C> fnReduce, final C initial)
    {
        C c = initial;
        for (final A a : coll)
        {
            final B b = fnMap.applyMapping(a);
            if (fnPredicate.applyPredicate(b)) c = fnReduce.applyFoldStep(b, c);
        }
        return c;
    }

    public final static <A, B> B reduce(final List<A> coll, final FnFoldStep<A, B> fn, final B initial)
    {
        if (coll instanceof RandomAccess)
        {
            final int sz = coll.size();
            B b = initial;
            for (int i = 0; i < sz; i++)
                b = fn.applyFoldStep(coll.get(i), b);
            return b;
        }
        return reduce((Iterable<A>)coll, fn, initial);
    }

    public final static <A, B> B reduce(final Iterable<A> coll, final FnFoldStep<A, B> fn, final B initial)
    {
        B b = initial;
        for (final A a : coll)
            b = fn.applyFoldStep(a, b);
        return b;
    }

    public final static <A, B> B filterReduce(final Iterable<A> coll, final FnPredicate<A> fnPredicate, final FnFoldStep<A, B> fnReduce, final B initial)
    {
        B b = initial;
        for (final A a : coll)
        {
            if (fnPredicate.applyPredicate(a)) b = fnReduce.applyFoldStep(a, b);
        }
        return b;
    }

    /**
     * Zips the two given collections into a list of tuples.
     *
     * @param collA
     *            First collection.
     * @param collB
     *            Second collection.
     * @return The list of tuples.
     * @see Tuple
     */
    public final static <A, B> List<Tuple<A, B>> zip(final Collection<A> collA, final Collection<B> collB)
    {
        final int todo = Math.min(collA.size(), collB.size());
        final List<Tuple<A, B>> ret = list(todo);
        final Iterator<A> a = collA.iterator();
        final Iterator<B> b = collB.iterator();
        for (int i = 0; i < todo; i++)
            ret.add(Tuple.of(a.next(), b.next()));
        return ret;
    }

    /**
     * Zips the two given collections into a list of tuples.
     *
     * @param collA
     *            First collection.
     * @param collB
     *            Second collection.
     * @return The list of tuples.
     * @see Tuple
     */
    public final static <A, B> List<Tuple<A, B>> zip(final Iterable<A> collA, final Iterable<B> collB)
    {
        final List<Tuple<A, B>> ret = list();
        final Iterator<A> a = collA.iterator();
        final Iterator<B> b = collB.iterator();
        while (a.hasNext() && b.hasNext())
            ret.add(Tuple.of(a.next(), b.next()));
        return ret;
    }

    public final static <A, B, C> List<C> zip(final Collection<A> collA, final Collection<B> collB, final FnCombine<A, B, C> fn)
    {
        final int todo = Math.min(collA.size(), collB.size());
        final List<C> ret = list(todo);
        final Iterator<A> a = collA.iterator();
        final Iterator<B> b = collB.iterator();
        for (int i = 0; i < todo; i++)
            ret.add(fn.applyCombine(a.next(), b.next()));
        return ret;
    }

    public final static <A, B, C> List<C> zip(final Iterable<A> collA, final Iterable<B> collB, final FnCombine<A, B, C> fn)
    {
        final List<C> ret = list();
        final Iterator<A> a = collA.iterator();
        final Iterator<B> b = collB.iterator();
        while (a.hasNext() && b.hasNext())
            ret.add(fn.applyCombine(a.next(), b.next()));
        return ret;
    }

    /**
     * Unzips the given collection of tuples into two lists.
     *
     * @param coll
     *            The collection
     * @return A tuple of two lists
     * @see Tuple
     */
    public final static <A, B> Tuple<List<A>, List<B>> unzip(final Collection<Tuple<A, B>> coll)
    {
        final List<A> listA = list(coll.size());
        final List<B> listB = list(coll.size());
        for (final Tuple<A, B> t : coll)
        {
            listA.add(t.a);
            listB.add(t.b);
        }
        return Tuple.of(listA, listB);
    }

    /**
     * Unzips the given collection of tuples into two lists.
     *
     * @param coll
     *            The collection
     * @return A tuple of two lists
     * @see Tuple
     */
    public final static <A, B> Tuple<List<A>, List<B>> unzip(final Iterable<Tuple<A, B>> coll)
    {
        final List<A> listA = list();
        final List<B> listB = list();
        for (final Tuple<A, B> t : coll)
        {
            listA.add(t.a);
            listB.add(t.b);
        }
        return Tuple.of(listA, listB);
    }

    public final static <A> List<List<A>> partition(final Iterable<A> coll, final int size)
    {
        final List<List<A>> ret = list();
        if (size < 1) throw new IllegalArgumentException("Partition size must be > 0");

        List<A> part = list();
        for (final A a : coll)
        {
            part.add(a);
            if (part.size() == size)
            {
                ret.add(part);
                part = list();
            }
        }
        if (part.size() > 0) ret.add(part);
        return ret;
    }

    public final static <A> Tuple<List<A>, List<A>> partition(final Iterable<A> coll, final FnPredicate<A> fn)
    {
        final List<A> l0 = list();
        final List<A> l1 = list();
        for (final A a : coll)
        {
            if (fn.applyPredicate(a))
                l0.add(a);
            else
                l1.add(a);
        }
        return Tuple.of(l0, l1);
    }

    public final static <A> List<List<A>> group(final Iterable<A> coll)
    {
        return group(coll, Fns.<A> examineEquals());
    }

    public final static <A> List<List<A>> group(final Iterable<A> coll, final FnEquals<A> fn)
    {
        final List<List<A>> ret = list();

        List<A> part = list();
        for (final A a : coll)
        {
            if (part.size() == 0)
                part.add(a);
            else
            {
                if (!fn.applyEquals(last(part), a))
                {
                    ret.add(part);
                    part = list();
                }
                part.add(a);
            }
        }
        if (part.size() > 0) ret.add(part);
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of bytes.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static byte[] asByteArray(final Collection<? extends Number> coll)
    {
        final byte[] ret = new byte[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.byteValue();
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of shorts.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static short[] asShortArray(final Collection<? extends Number> coll)
    {
        final short[] ret = new short[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.shortValue();
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of ints.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static int[] asIntArray(final Collection<? extends Number> coll)
    {
        final int[] ret = new int[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.intValue();
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of longs.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static long[] asLongArray(final Collection<? extends Number> coll)
    {
        final long[] ret = new long[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.longValue();
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of floats.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static float[] asFloatArray(final Collection<? extends Number> coll)
    {
        final float[] ret = new float[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.floatValue();
        return ret;
    }

    /**
     * Returns the given collection of numbers as an array of doubles.
     *
     * @param coll
     *            The collection
     * @return The array
     */
    public final static double[] asDoubleArray(final Collection<? extends Number> coll)
    {
        final double[] ret = new double[coll.size()];
        int i = 0;
        for (final Number n : coll)
            ret[i++] = n.doubleValue();
        return ret;
    }

    @SafeVarargs
    public static <T> T[] objArray(final T... ts)
    {
        return ts;
    }

    public static byte[] array(final byte... bytes)
    {
        return bytes;
    }

    public static short[] array(final short... shorts)
    {
        return shorts;
    }

    public static char[] array(final char... chars)
    {
        return chars;
    }

    public static int[] array(final int... ints)
    {
        return ints;
    }

    public static long[] array(final long... longs)
    {
        return longs;
    }

    public static float[] array(final float... floats)
    {
        return floats;
    }

    public static double[] array(final double... doubles)
    {
        return doubles;
    }

    @SafeVarargs
    public static <T> void addAll(final Collection<T> coll, final T... ts)
    {
        for (final T t : ts)
        {
            coll.add(t);
        }
    }

    public static <T> void addAll(final Collection<T> coll, final Iterable<T> iterable)
    {
        for (final T t : iterable)
        {
            coll.add(t);
        }
    }
}
