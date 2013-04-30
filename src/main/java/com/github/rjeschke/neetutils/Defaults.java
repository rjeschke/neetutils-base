package com.github.rjeschke.neetutils;

import java.util.Collection;
import java.util.Map;

public final class Defaults
{
    private Defaults()
    {
        // meh!
    }

    public final static <T> T ifNull(final T obj, final T def)
    {
        return obj == null ? def : obj;
    }

    public final static <T> T ifNullOrEmpty(final T obj, final T def)
    {
        if (obj == null) return def;

        if (obj instanceof Collection) return ((Collection<?>)obj).isEmpty() ? def : obj;
        if (obj instanceof Map) return ((Map<?, ?>)obj).isEmpty() ? def : obj;
        if (obj instanceof String) return ((String)obj).isEmpty() ? def : obj;

        return obj;
    }
}
