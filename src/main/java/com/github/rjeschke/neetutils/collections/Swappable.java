package com.github.rjeschke.neetutils.collections;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 * @param <T>
 */
public class Swappable<T>
{
    private final T first;
    private final T second;
    private boolean swapped = false;

    public Swappable(final T first, final T second)
    {
        this.first = first;
        this.second = second;
    }

    public static <T> Swappable<T> of(final T first, final T second)
    {
        return new Swappable<>(first, second);
    }

    public T first()
    {
        return this.swapped ? this.second : this.first;
    }

    public T second()
    {
        return this.swapped ? this.first : this.second;
    }

    public void swap()
    {
        this.swapped = !this.swapped;
    }

    public void reset()
    {
        this.swapped = false;
    }
}
