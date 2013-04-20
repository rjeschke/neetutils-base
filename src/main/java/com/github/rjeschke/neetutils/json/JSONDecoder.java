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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.rjeschke.neetutils.collections.Colls;
import com.github.rjeschke.neetutils.json.JSONTokenizer.Token;

/**
 * JSON decoder.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 */
public final class JSONDecoder
{
    private JSONDecoder()
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
     * @return A map containing all unmapped values.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static Map<String, Object> decodeInto(final String string, final JSONMarshallable object) throws IOException
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
     * @return A map containing all unmapped values.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    @SuppressWarnings("unchecked")
    public final static Map<String, Object> decodeInto(final Reader reader, final JSONMarshallable object) throws IOException
    {
        final Object obj = decode(reader);

        if (!(obj instanceof Map)) throw new IOException("JSON value ist not of type 'object'.");

        return decodeInto((Map<String, Object>)obj, object);
    }

    /**
     * Fills a {@link JSONMarshallable} using the given map.
     * 
     * @param jsonObject
     *            The map.
     * @param object
     *            The JSONMarshallable.
     * @return A map containing all unmapped values.
     * @throws IOException
     *             if an IO or processing error occurred.
     */
    public final static Map<String, Object> decodeInto(final Map<String, Object> jsonObject, final JSONMarshallable object)
            throws IOException
    {
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

                if (JSONEncoder.isFieldVisible(f, vis))
                {
                    boolean isAccessible = f.isAccessible();
                    f.setAccessible(true);
                    if (f.getType().isEnum())
                    {
                        try
                        {
                            final Method m = f.getType().getMethod("fromJSONString", String.class);
                            f.set(object, m.invoke(null, e.getValue().toString()));
                        }
                        catch (NoSuchMethodException | SecurityException e1)
                        {
                            f.set(object, e.getValue());
                        }
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
            catch (IllegalArgumentException | IllegalAccessException | ClassCastException | InvocationTargetException ex)
            {
                throw new IOException("Marshalling for type " + toClass + " failed for '" + e.getKey() + "'", ex);
            }
        }

        return rest;
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

}
