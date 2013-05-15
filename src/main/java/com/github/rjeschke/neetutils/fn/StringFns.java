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
package com.github.rjeschke.neetutils.fn;

import java.util.Arrays;
import java.util.Iterator;

import com.github.rjeschke.neetutils.iterables.Collector;

public final class StringFns
{
    private StringFns()
    {
        // 1up
    }

    public final static FnPredicate<String> notEmpty()
    {
        return new FnPredicate<String>()
        {
            @Override
            public boolean applyPredicate(final String a)
            {
                return a != null && a.length() > 0;
            }
        };
    }

    public final static Collector<Character, String> splitCollector(final char splitCharacter)
    {
        return new Collector<Character, String>()
        {
            @Override
            public void collect(final Iterator<? extends Character> iterator)
            {
                if (iterator.hasNext())
                {
                    final StringBuilder sb = new StringBuilder();
                    while (iterator.hasNext())
                    {
                        final char c = iterator.next();
                        if (c == splitCharacter)
                        {
                            break;
                        }
                        sb.append(c);
                    }
                    this.emit(sb.toString());
                }
            }
        };
    }

    public final static FnMapping<String, String> toUpperCase()
    {
        return new FnMapping<String, String>()
        {
            @Override
            public String applyMapping(final String value)
            {
                return value.toUpperCase();
            }
        };
    }

    public final static FnMapping<String, String> trim()
    {
        return new FnMapping<String, String>()
        {
            @Override
            public String applyMapping(final String value)
            {
                return value.trim();
            }
        };
    }

    public final static <A> FnMapping<A, String> asString()
    {
        return new FnMapping<A, String>()
        {
            @Override
            public String applyMapping(final A value)
            {
                return value.toString();
            }
        };
    }

    public final static FnMapping<String, String> toLowerCase()
    {
        return new FnMapping<String, String>()
        {
            @Override
            public String applyMapping(final String value)
            {
                return value.toLowerCase();
            }
        };
    }

    public final static FnPredicate<String> endsWith(final String... strings)
    {
        final String[] needles = Arrays.copyOf(strings, strings.length);

        return new FnPredicate<String>()
        {
            @Override
            public boolean applyPredicate(final String a)
            {
                if (needles.length == 0) return true;

                for (int i = 0; i < needles.length; i++)
                {
                    if (a.endsWith(needles[i]))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public final static FnPredicate<String> startsWith(final String... strings)
    {
        final String[] needles = Arrays.copyOf(strings, strings.length);

        return new FnPredicate<String>()
        {
            @Override
            public boolean applyPredicate(final String a)
            {
                if (needles.length == 0) return true;

                for (int i = 0; i < needles.length; i++)
                {
                    if (a.startsWith(needles[i]))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public final static FnPredicate<String> endsWithIgnoreCase(final String... strings)
    {
        final String[] needles = Arrays.copyOf(strings, strings.length);
        for (int i = 0; i < needles.length; i++)
        {
            needles[i] = needles[i].toLowerCase();
        }

        return new FnPredicate<String>()
        {
            @Override
            public boolean applyPredicate(final String a)
            {
                if (needles.length == 0) return true;

                for (int i = 0; i < needles.length; i++)
                {
                    if (a.toLowerCase().endsWith(needles[i]))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public final static FnPredicate<String> startsWithIgnoreCase(final String... strings)
    {
        final String[] needles = Arrays.copyOf(strings, strings.length);
        for (int i = 0; i < needles.length; i++)
        {
            needles[i] = needles[i].toLowerCase();
        }

        return new FnPredicate<String>()
        {
            @Override
            public boolean applyPredicate(final String a)
            {
                if (needles.length == 0) return true;

                for (int i = 0; i < needles.length; i++)
                {
                    if (a.toLowerCase().startsWith(needles[i]))
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public final static <A> FnFoldStep<A, StringBuilder> stringConcat()
    {
        return new FnFoldStep<A, StringBuilder>()
        {
            @Override
            public StringBuilder applyFoldStep(final A a, final StringBuilder b)
            {
                b.append(a);

                return b;
            }
        };
    }
}
