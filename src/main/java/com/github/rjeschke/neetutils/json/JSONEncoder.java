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
package com.github.rjeschke.neetutils.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JSON encoder.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 */
public final class JSONEncoder
{
    private JSONEncoder()
    {
        // meh
    }

    /**
     * Encodes the given object into a JSON string representation.
     * 
     * @param obj
     *            The object to encode.
     * @return The JSON string representation.
     */
    public final static String encode(final Object obj)
    {
        return encode(new StringBuilder(), obj).toString();
    }

    /**
     * Encodes the given object into a JSON string representation.
     * 
     * @param sb
     *            The {@link StringBuilder} to write the result into.
     * @param obj
     *            The object to encode.
     * @return The given StringBuilder.
     */
    public final static StringBuilder encode(final StringBuilder sb, final Object obj)
    {
        writeObject(sb, obj);
        return sb;
    }

    private final static void writeNumber(final StringBuilder sb, final long value)
    {
        sb.append(value);
    }

    private final static void writeNumber(final StringBuilder sb, final double value)
    {
        sb.append(value);
    }

    private final static void writeNumber(final StringBuilder sb, final Number value)
    {
        if (value instanceof Double || value instanceof Float)
        {
            writeNumber(sb, value.doubleValue());
        }
        else
        {
            writeNumber(sb, value.longValue());
        }
    }

    private final static void writeString(final StringBuilder sb, final String value)
    {
        sb.append('"');
        for (int i = 0; i < value.length(); i++)
        {
            final char ch = value.charAt(i);
            switch (ch)
            {
            case '"':
                sb.append("\\\"");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            default:
                if (ch < 32)
                {
                    sb.append(String.format("\\u%04x", (int)ch));
                }
                else
                {
                    sb.append(ch);
                }
            }
        }
        sb.append('"');
    }

    private final static void writeList(final StringBuilder sb, final Collection<?> list)
    {
        boolean second = false;
        sb.append('[');
        for (final Object o : list)
        {
            if (second)
            {
                sb.append(',');
            }
            else
            {
                second = true;
            }
            writeObject(sb, o);
        }
        sb.append(']');
    }

    private final static void writeMap(final StringBuilder sb, final Map<?, ?> map)
    {
        boolean second = false;
        sb.append('{');
        for (final Entry<?, ?> e : map.entrySet())
        {
            if (second)
            {
                sb.append(',');
            }
            else
            {
                second = true;
            }
            writeString(sb, e.getKey().toString());
            sb.append(':');
            writeObject(sb, e.getValue());
        }
        sb.append('}');
    }

    protected final static boolean isPublicVisibility(final Field f)
    {
        return (f.getModifiers() & Modifier.PUBLIC) != 0;
    }

    protected final static boolean isPrivateVisibility(final Field f)
    {
        return (f.getModifiers() & Modifier.PRIVATE) != 0;
    }

    protected final static boolean isDefaultVisibility(final Field f)
    {
        return (f.getModifiers() & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED)) == 0;
    }

    protected final static boolean isProtectedVisibility(final Field f)
    {
        return (f.getModifiers() & Modifier.PROTECTED) != 0;
    }

    protected final static boolean isFieldVisible(final Field f, final int vis)
    {
        if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0) return false;

        if ((vis & JSONObjectVisibility.PRIVATE) != 0 && isPrivateVisibility(f)) return true;
        if ((vis & JSONObjectVisibility.DEFAULT) != 0 && isDefaultVisibility(f)) return true;
        if ((vis & JSONObjectVisibility.PROTECTED) != 0 && isProtectedVisibility(f)) return true;
        if ((vis & JSONObjectVisibility.PUBLIC) != 0 && isPublicVisibility(f)) return true;

        return false;
    }

    private final static void writeMarshallable(final StringBuilder sb, final Object obj)
    {
        boolean second = false;
        sb.append('{');

        try
        {
            int vis = JSONObjectVisibility.PUBLIC;
            if (obj.getClass().isAnnotationPresent(JSONObject.class))
            {
                final JSONObject v = obj.getClass().getAnnotation(JSONObject.class);
                vis = v.visibility();
            }

            for (final Field f : obj.getClass().getDeclaredFields())
            {
                if (isFieldVisible(f, vis))
                {
                    if (second)
                    {
                        sb.append(',');
                    }
                    else
                    {
                        second = true;
                    }

                    boolean isAccessible = f.isAccessible();
                    f.setAccessible(true);
                    writeString(sb, f.getName());
                    sb.append(':');
                    writeObject(sb, f.get(obj));
                    f.setAccessible(isAccessible);
                }
            }
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to write marshallable of type: " + obj.getClass(), e);
        }

        sb.append('}');
    }

    private final static void writeObject(final StringBuilder sb, final Object obj)
    {
        if (obj == null)
        {
            sb.append("null");
        }
        else if (obj instanceof JSONMarshallable)
        {
            writeMarshallable(sb, obj);
        }
        else if (obj instanceof Map)
        {
            writeMap(sb, (Map<?, ?>)obj);
        }
        else if (obj instanceof Collection)
        {
            writeList(sb, (Collection<?>)obj);
        }
        else if (obj instanceof Number)
        {
            writeNumber(sb, (Number)obj);
        }
        else if (obj instanceof Boolean)
        {
            sb.append(((Boolean)obj).booleanValue() ? "true" : "false");
        }
        else
        {
            writeString(sb, obj.toString());
        }
    }
}
