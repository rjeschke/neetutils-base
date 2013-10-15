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

import java.nio.charset.Charset;

public final class Charsets
{
    private Charsets()
    {
        // meh!
    }

    public final static Charset UTF8         = Charset.forName("UTF-8");
    public final static Charset UTF16        = Charset.forName("UTF-16");
    public final static Charset UTF16BE      = Charset.forName("UTF-16BE");
    public final static Charset UTF16LE      = Charset.forName("UTF-16LE");
    public final static Charset UTF32        = Charset.forName("UTF-32");
    public final static Charset UTF32BE      = Charset.forName("UTF-32BE");
    public final static Charset UTF32LE      = Charset.forName("UTF-32LE");
    public final static Charset ISO8859_1    = Charset.forName("ISO-8859-1");
    public final static Charset ISO8859_2    = Charset.forName("ISO-8859-2");
    public final static Charset ISO8859_3    = Charset.forName("ISO-8859-3");
    public final static Charset ISO8859_4    = Charset.forName("ISO-8859-4");
    public final static Charset ISO8859_5    = Charset.forName("ISO-8859-5");
    public final static Charset ISO8859_6    = Charset.forName("ISO-8859-6");
    public final static Charset ISO8859_7    = Charset.forName("ISO-8859-7");
    public final static Charset ISO8859_8    = Charset.forName("ISO-8859-8");
    public final static Charset ISO8859_9    = Charset.forName("ISO-8859-9");
    public final static Charset ISO8859_13   = Charset.forName("ISO-8859-13");
    public final static Charset ISO8859_15   = Charset.forName("ISO-8859-15");
    public final static Charset ASCII        = Charset.forName("US-ASCII");
    public final static Charset WINDOWS_1250 = Charset.forName("windows-1250");
    public final static Charset WINDOWS_1251 = Charset.forName("windows-1251");
    public final static Charset WINDOWS_1252 = Charset.forName("windows-1252");
    public final static Charset WINDOWS_1253 = Charset.forName("windows-1253");
    public final static Charset WINDOWS_1254 = Charset.forName("windows-1254");
    public final static Charset WINDOWS_1255 = Charset.forName("windows-1255");
    public final static Charset WINDOWS_1256 = Charset.forName("windows-1256");
    public final static Charset WINDOWS_1257 = Charset.forName("windows-1257");
    public final static Charset WINDOWS_1258 = Charset.forName("windows-1258");
}
