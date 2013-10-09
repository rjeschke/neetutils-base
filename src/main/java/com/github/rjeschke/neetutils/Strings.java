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

/**
 * String utility methods.
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class Strings
{
    /**
     * Produces a in Java usable string representation of the given String, wrapping it in quotes and escaping all relevant characters.
     *
     * @param str
     *            The String to escape.
     * @return Escape String or &quote;null&quote; if str is <code>null</code>
     */
    public final static String escapeString(final String str)
    {
        if (str == null) return "null";

        final StringBuilder sb = new StringBuilder();

        sb.append('"');
        for (int i = 0; i < str.length(); i++)
        {
            final char c = str.charAt(i);
            switch (c)
            {
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '"':
                sb.append("\\\"");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        sb.append('"');

        return sb.toString();
    }

    public final static List<String> split(final String str, final char ch)
    {
        final List<String> ret = Colls.list();

        if (str != null)
        {
            int s = 0, e = 0;
            while (e < str.length())
            {
                if (str.charAt(e) == ch)
                {
                    ret.add(str.substring(s, e));
                    s = e + 1;
                }
                e++;
            }
            ret.add(str.substring(s, e));
        }

        return ret;
    }

    public final static List<String> trim(final List<String> strings)
    {
        for (int i = 0; i < strings.size(); i++)
        {
            final String s = strings.get(i);
            if (s != null) strings.set(i, s.trim());
        }
        return strings;
    }

    public final static boolean isEmpty(final String str)
    {
        return str == null || str.isEmpty();
    }

    public final static String collapseWhitespace(final String str)
    {
        final StringBuilder sb = new StringBuilder();
        boolean wasWs = false;
        for (int i = 0; i < str.length(); i++)
        {
            final char c = str.charAt(i);
            if (Character.isWhitespace(c) || Character.isSpaceChar(c))
            {
                if (!wasWs)
                {
                    sb.append(' ');
                    wasWs = true;
                }
            }
            else
            {
                wasWs = false;
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public final static String join(final Iterable<String> iterable, final String glue)
    {
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (final String s : iterable)
        {
            if (index > 0)
            {
                sb.append(glue);
            }
            sb.append(s);
            ++index;
        }

        return sb.toString();
    }

    public final static String join(final Iterable<String> iterable)
    {
        final StringBuilder sb = new StringBuilder();

        for (final String s : iterable)
        {
            sb.append(s);
        }

        return sb.toString();
    }

    public final static List<String> splitLength(final String str, final int len)
    {
        final List<String> ret = Colls.list();
        int todo = str.length();
        int pos = 0;
        while (todo > 0)
        {
            final int n = Math.min(todo, len);
            ret.add(str.substring(pos, pos + n));
            pos += n;
            todo -= n;
        }
        return ret;
    }
}
