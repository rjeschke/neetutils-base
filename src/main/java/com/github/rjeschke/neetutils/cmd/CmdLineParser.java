/*
 * Copyright (C) 2013 René Jeschke <rene_jeschke@yahoo.de>
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
package com.github.rjeschke.neetutils.cmd;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.github.rjeschke.neetutils.Strings;
import com.github.rjeschke.neetutils.collections.Colls;

public final class CmdLineParser
{
    private CmdLineParser()
    {
        // meh!
    }

    private static void parseArgs(final Object[] objs, final List<Arg> allArgs, final HashMap<String, Arg> shortArgs, final HashMap<String, Arg> longArgs)
            throws IOException
    {
        for (final Object obj : objs)
        {
            final Class<?> cl = obj.getClass();
            final Field[] fields = cl.getDeclaredFields();

            for (final Field f : fields)
            {
                if (f.isAnnotationPresent(CmdArgument.class))
                {
                    final Arg arg = new Arg(f.getAnnotation(CmdArgument.class), obj, f);

                    if (Strings.isEmpty(arg.s) && Strings.isEmpty(arg.l))
                    {
                        throw new IOException("Missing parameter name");
                    }

                    if (!Strings.isEmpty(arg.s))
                    {
                        if (shortArgs.containsKey(arg.s))
                        {
                            throw new IOException("Duplicate short argument: -" + arg.s);
                        }
                        shortArgs.put(arg.s, arg);
                    }

                    if (!Strings.isEmpty(arg.l))
                    {
                        if (longArgs.containsKey(arg.l))
                        {
                            throw new IOException("Duplicate long argument: --" + arg.l);
                        }
                        longArgs.put(arg.l, arg);
                    }

                    allArgs.add(arg);
                }
            }
        }
    }

    public static String generateHelp(final int columnWidth, final boolean sort, final Object... objs) throws IOException
    {
        final List<Arg> allArgs = Colls.list();
        final HashMap<String, Arg> shortArgs = new HashMap<>();
        final HashMap<String, Arg> longArgs = new HashMap<>();

        parseArgs(objs, allArgs, shortArgs, longArgs);

        int minArgLen = 0;

        for (final Arg a : allArgs)
        {
            int len = a.toString().length();
            if (!a.isSwitch)
            {
                len += 4;
            }
            minArgLen = Math.max(minArgLen, len);
        }
        minArgLen += 3;
        if (sort)
        {
            Collections.sort(allArgs);
        }

        final StringBuilder sb = new StringBuilder();

        for (final Arg a : allArgs)
        {
            final StringBuilder line = new StringBuilder();
            line.append("  ");
            line.append(a);
            if (!a.isSwitch)
            {
                if (a.isCatchAll())
                {
                    line.append(" ...");
                }
                else
                {
                    line.append(" arg");
                }
            }
            while (line.length() < minArgLen)
            {
                line.append(' ');
            }

            line.append(':');

            final List<String> toks = Strings.split(a.desc, ' ');

            for (final String s : toks)
            {
                if (line.length() + s.length() + 1 > columnWidth)
                {
                    sb.append(line);
                    sb.append('\n');
                    line.setLength(0);
                    while (line.length() <= minArgLen)
                    {
                        line.append(' ');
                    }
                    line.append(' ');
                }
                line.append(' ');
                line.append(s);
            }

            if (line.length() > minArgLen)
            {
                sb.append(line);
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    public static List<String> parse(final String[] args, final Object... objs) throws IOException
    {
        final List<String> ret = Colls.list();

        final List<Arg> allArgs = Colls.list();
        final HashMap<String, Arg> shortArgs = new HashMap<>();
        final HashMap<String, Arg> longArgs = new HashMap<>();

        parseArgs(objs, allArgs, shortArgs, longArgs);

        for (int i = 0; i < args.length; i++)
        {
            final String s = args[i];

            final Arg a;

            if (s.startsWith("--"))
            {
                a = longArgs.get(s.substring(2));
                if (a == null)
                {
                    throw new IOException("Unknown switch: " + s);
                }
            }
            else if (s.startsWith("-"))
            {
                a = shortArgs.get(s.substring(1));
                if (a == null)
                {
                    throw new IOException("Unknown switch: " + s);
                }
            }
            else
            {
                a = null;
                ret.add(s);
            }

            if (a != null)
            {
                if (a.isSwitch)
                {
                    a.setField("true");
                }
                else
                {
                    if (i + 1 >= args.length)
                    {
                        System.out.println("Missing parameter for: " + s);
                    }
                    if (a.isCatchAll())
                    {
                        final List<String> ca = Colls.list();
                        for (++i; i < args.length; ++i)
                        {
                            ca.add(args[i]);
                        }
                        a.setListField(ca);
                    }
                    else
                    {
                        ++i;
                        a.setField(args[i]);
                    }
                }
                a.setPresent();
            }
        }

        for (final Arg a : allArgs)
        {
            if (!a.isOk())
            {
                throw new IOException("Missing mandatory argument: " + a);
            }
        }

        return ret;
    }

    static class Arg implements Comparable<Arg>
    {
        public final String  s;
        public final String  l;
        public final String  id;
        public final String  desc;
        public final char    itemSep;
        public final boolean isSwitch;
        public final boolean required;
        public final boolean catchAll;
        private boolean      present = false;
        private final Object object;
        private final Field  field;

        public Arg(final CmdArgument arg, final Object obj, final Field field)
        {
            this.s = arg.s() == 0 ? "" : Character.toString(arg.s());
            this.l = arg.l();
            this.desc = arg.desc();
            this.isSwitch = arg.isSwitch();
            this.required = arg.required();
            this.catchAll = arg.catchAll();
            this.itemSep = arg.itemSep();
            this.id = this.s + "/" + this.l;

            this.object = obj;
            this.field = field;
        }

        public boolean isCatchAll()
        {
            return this.field.getType().equals(List.class);
        }

        public void setListField(final List<String> list) throws IOException
        {
            try
            {
                this.field.set(this.object, list);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                throw new IOException("Failed to write value", e);
            }
        }

        public void setField(final String value) throws IOException
        {
            try
            {
                if (this.field.getType().equals(String.class))
                {
                    this.field.set(this.object, value);
                }
                else if (this.field.getType().equals(byte.class))
                {
                    this.field.set(this.object, Byte.parseByte(value));
                }
                else if (this.field.getType().equals(short.class))
                {
                    this.field.set(this.object, Short.parseShort(value));
                }
                else if (this.field.getType().equals(int.class))
                {
                    this.field.set(this.object, Integer.parseInt(value));
                }
                else if (this.field.getType().equals(long.class))
                {
                    this.field.set(this.object, Long.parseLong(value));
                }
                else if (this.field.getType().equals(float.class))
                {
                    this.field.set(this.object, Float.parseFloat(value));
                }
                else if (this.field.getType().equals(double.class))
                {
                    this.field.set(this.object, Double.parseDouble(value));
                }
                else if (this.field.getType().equals(boolean.class))
                {
                    final String v = value.toLowerCase();
                    this.field.set(this.object, v.equals("true") || v.equals("on") || v.equals("yes"));
                }
                else
                {
                    throw new IOException("Unsupported field type: " + this.field.getType());
                }
            }
            catch (final Exception e)
            {
                throw new IOException("Failed to write value", e);
            }
        }

        public void setPresent()
        {
            this.present = true;
        }

        public boolean isOk()
        {
            return !this.required || this.present;
        }

        @Override
        public int hashCode()
        {
            return this.id.hashCode();
        }

        @Override
        public boolean equals(final Object obj)
        {
            if (obj instanceof Arg)
            {
                return this.id.equals(((Arg)obj).id);
            }
            return false;
        }

        @Override
        public String toString()
        {
            if (Strings.isEmpty(this.s))
            {
                return "    --" + this.l;
            }
            if (Strings.isEmpty(this.l))
            {
                return "-" + this.s;
            }
            return "-" + this.s + ", --" + this.l;
        }

        @Override
        public int compareTo(final Arg o)
        {
            final String a = Strings.isEmpty(this.s) ? this.l : this.s;
            final String b = Strings.isEmpty(o.s) ? o.l : o.s;
            return a.compareTo(b);
        }
    }
}
