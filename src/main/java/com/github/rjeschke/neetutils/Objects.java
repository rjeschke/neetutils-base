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
package com.github.rjeschke.neetutils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class Objects
{
    private Objects()
    {
        //
    }

    @Deprecated
    public final static boolean implementsInterface(final Object o, final Class<?> interfaceClass)
    {
        return implementsInterface(o.getClass(), interfaceClass);
    }

    @Deprecated
    public final static boolean implementsInterface(final Class<?> clazz, final Class<?> interfaceClass)
    {
        if (clazz.equals(interfaceClass)) return true;
        final Class<?>[] is = clazz.getInterfaces();
        for (int i = 0; i < is.length; i++)
        {
            if (is[i].equals(interfaceClass)) return true;
        }
        return false;
    }

    public final static boolean isString(final Object o)
    {
        return o instanceof String;
    }

    public final static boolean isBoolean(final Object o)
    {
        return o instanceof Boolean;
    }

    public final static boolean isNumber(final Object o)
    {
        return o instanceof Number;
    }

    public final static boolean isMap(final Object o)
    {
        return o instanceof Map;
    }

    public final static boolean isList(final Object o)
    {
        return o instanceof List;
    }

    public final static boolean isCollection(final Object o)
    {
        return o instanceof Collection;
    }

    public final static boolean equals(final Object a, final Object b)
    {
        if (a == b) return true;

        if (a == null) return b == null;

        if (a.getClass() != b.getClass()) return false;

        return a.equals(b);
    }

    public final static boolean isNullOrEmpty(final Object obj)
    {
        if (obj == null) return true;

        if (obj instanceof Collection) return ((Collection<?>)obj).isEmpty();
        if (obj instanceof Map) return ((Map<?, ?>)obj).isEmpty();
        if (obj instanceof String) return ((String)obj).isEmpty();

        return false;
    }

    @SuppressWarnings("unchecked")
    public final static <A> A uncheckedCast(final Object a)
    {
        return (A)a;
    }
}
