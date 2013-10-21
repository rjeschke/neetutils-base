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

import java.util.List;

import com.github.rjeschke.neetutils.collections.Colls;
import com.github.rjeschke.neetutils.io.Files;

/**
 * Functions dealing with classes.
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public final class Classes
{
    private Classes()
    {
        //
    }

    /**
     * Lists all classes on the classpath.
     *
     * @return List of class names
     */
    public final static List<String> getClassesOnClasspath()
    {
        final List<String> ret = Colls.list();

        for (final String s : Files.getFilesOnClasspath())
        {
            if (s.endsWith(".class"))
            {
                ret.add(s.substring(1, s.length() - 6).replace('/', '.'));
            }
        }

        return ret;
    }

    /**
     * Tests whether {@code clazz} implements interface {@code interfce}
     *
     * @param clazz
     *            The class to test
     * @param interfce
     *            The interface to check for
     * @return {@code true} if the class implements the given interface
     */
    public final static boolean implementsInterface(final Class<?> clazz, final Class<?> interfce)
    {
        for (final Class<?> c : clazz.getInterfaces())
        {
            if (c.equals(interfce))
            {
                return true;
            }
        }

        return false;
    }

    public final static boolean implementsInterface(final Object o, final Class<?> interfce)
    {
        return implementsInterface(o.getClass(), interfce);
    }
}
