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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.rjeschke.neetutils.collections.Colls;
import com.github.rjeschke.neetutils.json.JSONTokenizer.Token;

/**
 * JSON encoder, decoder and utilities.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 */
public final class JSON
{
    private JSON()
    {
        // meh!
    }

    /**
     * Decodes the given JSON string into an object.
     * 
     * @param string
     *            The string to decode.
     * @return The decoded object.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static Object decode(final String string) throws IOException
    {
        try (final StringReader reader = new StringReader(string))
        {
            return decode(reader);
        }
    }

    /**
     * Decodes a JSON string read from the given {@link Reader} into an object.
     * 
     * @param reader
     *            The reader to read from.
     * @return The decoded object.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static Object decode(final Reader reader) throws IOException
    {
        final JSONTokenizer tokenizer = new JSONTokenizer(reader);

        tokenizer.next();

        final Object ret = readObject(tokenizer);

        if (tokenizer.getCurrentToken() != Token.EOF) throw new IOException("Multiple JSON values in string" + tokenizer.getPosition());

        return ret;
    }

    /**
     * Decodes a JSON string containing a single object into the given
     * {@link JSONMarshallable}.
     * 
     * @param string
     *            The string to decode.
     * @param object
     *            The JSONMarshallable.
     * @return The decoded {@code object}.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static <T extends JSONMarshallable> T decodeInto(final String string, final T object) throws IOException
    {
        try (final StringReader reader = new StringReader(string))
        {
            return decodeInto(reader, object);
        }
    }

    /**
     * Decodes a JSON string read from the given {@link Reader} containing a
     * single object into the given {@link JSONMarshallable}.
     * 
     * @param reader
     *            The reader to read from.
     * @param object
     *            The JSONMarshallable.
     * @return The decoded {@code object}.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static <T extends JSONMarshallable> T decodeInto(final Reader reader, final T object) throws IOException
    {
        final Object obj = decode(reader);

        if (!isMap(obj)) throw new IOException("JSON value ist not of type 'object'.");

        return decodeInto(asMap(obj), object);
    }

    /**
     * Fills a {@link JSONMarshallable} using the given map.
     * 
     * @param jsonObject
     *            The map.
     * @param object
     *            The JSONMarshallable.
     * @return The decoded {@code object}.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static <T extends JSONMarshallable> T decodeInto(final Map<String, Object> jsonObject, final T object) throws IOException
    {
        Field catchAll = null;
        final Map<String, Object> rest = new HashMap<>();

        final Class<?> toClass = object.getClass();

        int vis = JSONObjectVisibility.PUBLIC;
        if (toClass.isAnnotationPresent(JSONObject.class))
        {
            final JSONObject v = toClass.getAnnotation(JSONObject.class);
            vis = v.visibility();
        }

        for (final Entry<String, Object> e : jsonObject.entrySet())
        {
            try
            {
                final Field f = toClass.getDeclaredField(e.getKey());

                if (f.isAnnotationPresent(JSONCatchAllField.class))
                {
                    catchAll = f;
                    continue;
                }

                if (isFieldVisible(f, vis, false))
                {
                    boolean isAccessible = f.isAccessible();
                    f.setAccessible(true);
                    if (f.getType().isEnum())
                    {
                        Method m;
                        try
                        {
                            m = f.getType().getMethod("fromJSONString", String.class);
                        }
                        catch (NoSuchMethodException ex)
                        {
                            m = f.getType().getMethod("valueOf", String.class);
                        }
                        f.set(object, m.invoke(null, e.getValue().toString()));
                    }
                    else
                    {
                        f.set(object, e.getValue());
                    }
                    f.setAccessible(isAccessible);
                }
            }
            catch (NoSuchFieldException ex)
            {
                rest.put(e.getKey(), e.getValue());
            }
            catch (IllegalArgumentException | IllegalAccessException | ClassCastException | InvocationTargetException | SecurityException
                    | NoSuchMethodException ex)
            {
                throw new IOException("Marshalling for type " + toClass + " failed for '" + e.getKey() + "'", ex);
            }
        }

        if (catchAll != null)
        {
            try
            {
                catchAll.set(object, rest);
            }
            catch (IllegalArgumentException | IllegalAccessException ex)
            {
                throw new IOException("Marshalling for type " + toClass + " failed for '" + catchAll.getName() + "'", ex);
            }
        }

        return object;
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

    public final static boolean isMap(final Object obj)
    {
        return obj instanceof Map;
    }

    @SuppressWarnings("unchecked")
    public final static Map<String, Object> asMap(final Object obj)
    {
        return (Map<String, Object>)obj;
    }

    public final static boolean isArray(final Object obj)
    {
        return obj instanceof List;
    }

    @SuppressWarnings("unchecked")
    public final static List<Object> asArray(final Object obj)
    {
        return (List<Object>)obj;
    }

    public final static boolean isNumber(final Object obj)
    {
        return obj instanceof Number;
    }

    public final static Number asNumber(final Object obj)
    {
        return (Number)obj;
    }

    public final static boolean isBoolean(final Object obj)
    {
        return obj instanceof Boolean;
    }

    public final static Boolean asBoolean(final Object obj)
    {
        return (Boolean)obj;
    }

    private final static Object readObject(final JSONTokenizer tokenizer) throws IOException
    {
        switch (tokenizer.getCurrentToken())
        {
        case OBJECT_OPEN:
            tokenizer.next();
            return readMap(tokenizer);
        case ARRAY_OPEN:
            tokenizer.next();
            return readArray(tokenizer);
        case NULL:
            tokenizer.next();
            return null;
        case TRUE:
            tokenizer.next();
            return Boolean.TRUE;
        case FALSE:
            tokenizer.next();
            return Boolean.FALSE;
        case STRING:
            tokenizer.next();
            return tokenizer.getStringValue();
        case INTEGER:
            tokenizer.next();
            return tokenizer.getIntegerValue();
        case DOUBLE:
            tokenizer.next();
            return tokenizer.getDoubleValue();
        default:
            throw new IOException("Unexpected token: " + tokenizer.getCurrentToken() + "," + tokenizer.getPosition());
        }
    }

    private final static List<Object> readArray(final JSONTokenizer tokenizer) throws IOException
    {
        final List<Object> list = Colls.list();

        for (;;)
        {
            Token t = tokenizer.getCurrentToken();
            if (t == Token.ARRAY_CLOSE)
            {
                tokenizer.next();
                break;
            }

            if (t == Token.EOF) throw new IOException("Unexpected end of string for array" + tokenizer.getPosition());

            list.add(readObject(tokenizer));

            if (tokenizer.getCurrentToken() == Token.COMMA) tokenizer.next();
        }

        return list;
    }

    private final static Map<String, Object> readMap(final JSONTokenizer tokenizer) throws IOException
    {
        final Map<String, Object> map = new HashMap<>();
        for (;;)
        {
            Token t = tokenizer.getCurrentToken();
            if (t == Token.OBJECT_CLOSE)
            {
                tokenizer.next();
                break;
            }

            if (t != Token.STRING) throw new IOException("Object key expected" + tokenizer.getPosition());
            final String key = tokenizer.getStringValue();
            if (Token.COLON != tokenizer.next()) throw new IOException("':' expected" + tokenizer.getPosition());
            tokenizer.next();
            map.put(key, readObject(tokenizer));
            if (tokenizer.getCurrentToken() == Token.COMMA) tokenizer.next();
        }
        return map;
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

    protected final static boolean isFieldVisible(final Field f, final int vis, final boolean read)
    {
        if (f.isAnnotationPresent(JSONIgnoreField.class)) return false;
        if (f.isAnnotationPresent(JSONForceField.class)) return true;
        if (read && f.isAnnotationPresent(JSONReadOnlyField.class)) return true;

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
                if (isFieldVisible(f, vis, true))
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
