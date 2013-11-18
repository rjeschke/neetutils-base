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

    public static List<String> parse(final String[] args, final Object... objs) throws IOException
    {
        final List<String> ret = Colls.list();

        final List<Arg> allArgs = Colls.list();
        final HashMap<String, Arg> shortArgs = new HashMap<>();
        final HashMap<String, Arg> longArgs = new HashMap<>();

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

    static class Arg
    {
        public final String  s;
        public final String  l;
        public final String  id;
        public final String  desc;
        public final boolean isSwitch;
        public final boolean required;
        private boolean      present = false;
        private final Object object;
        private final Field  field;

        public Arg(final CmdArgument arg, final Object obj, final Field field)
        {
            this.s = arg.s();
            this.l = arg.l();
            this.desc = arg.desc();
            this.isSwitch = arg.isSwitch();
            this.required = arg.required();
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
                return "--" + this.l;
            }
            return "-" + this.s;
        }
    }
}
