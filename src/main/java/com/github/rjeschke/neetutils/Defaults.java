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
import java.util.Map;

/**
 * Functions to deal with getting default values in various cases.
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class Defaults
{
    private Defaults()
    {
        // meh!
    }

    /**
     * @param obj
     *            Object reference to test
     * @param def
     *            Default value
     * @return {@code def} if {@code obj} is {@code null}
     */
    public final static <T> T ifNull(final T obj, final T def)
    {
        return obj == null ? def : obj;
    }

    /**
     * @param obj
     *            Object reference to test
     * @param def
     *            Default value
     * @return {@code def} if {@code obj} is {@code null} or empty.
     */
    public final static <T> T ifNullOrEmpty(final T obj, final T def)
    {
        if (obj == null) return def;

        if (obj instanceof Collection) return ((Collection<?>)obj).isEmpty() ? def : obj;
        if (obj instanceof Map) return ((Map<?, ?>)obj).isEmpty() ? def : obj;
        if (obj instanceof String) return ((String)obj).isEmpty() ? def : obj;

        return obj;
    }
}
