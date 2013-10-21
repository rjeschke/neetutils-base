package com.github.rjeschke.neetutils.collections;

import java.util.Iterator;

import com.github.rjeschke.neetutils.math.Numbers;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <A>
 */
class IndexedIterator<A> implements Iterator<Tuple<Integer, A>>
{
    private final Iterator<A> iterator;
    private int               index = 0;

    public IndexedIterator(final Iterator<A> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    @Override
    public Tuple<Integer, A> next()
    {
        return Tuple.of(Numbers.integerOf(this.index++), this.iterator.next());
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
