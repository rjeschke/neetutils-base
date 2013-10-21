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
package com.github.rjeschke.neetutils.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public final class Streams
{
    private Streams()
    {
        //
    }

    public final static String UTF8 = "UTF-8";

    public final static BufferedReader newBufferedReader(final InputStream in, final String charsetName) throws IOException
    {
        return new BufferedReader(new InputStreamReader(in, charsetName));
    }

    public final static BufferedReader newBufferedReader(final InputStream in) throws IOException
    {
        return newBufferedReader(in, UTF8);
    }

    public final static BufferedReader newBufferedReader(final String filename, final String charsetName) throws IOException
    {
        return new BufferedReader(new InputStreamReader(new FileInputStream(filename), charsetName));
    }

    public final static BufferedReader newBufferedReader(final String filename) throws IOException
    {
        return newBufferedReader(filename, UTF8);
    }

    public final static BufferedReader newBufferedReader(final File file, final String charsetName) throws IOException
    {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
    }

    public final static BufferedReader newBufferedReader(final File file) throws IOException
    {
        return newBufferedReader(file, UTF8);
    }

    public final static LineReaderIterator newLineReaderIterator(final InputStream in, final String charsetName) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(in, charsetName));
    }

    public final static LineReaderIterator newLineReaderIterator(final InputStream in) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(in));
    }

    public final static LineReaderIterator newLineReaderIterator(final File file, final String charsetName) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(file, charsetName));
    }

    public final static LineReaderIterator newLineReaderIterator(final File file) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(file));
    }

    public final static LineReaderIterator newLineReaderIterator(final String filename, final String charsetName) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(filename, charsetName));
    }

    public final static LineReaderIterator newLineReaderIterator(final String filename) throws IOException
    {
        return new LineReaderIterator(newBufferedReader(filename));
    }

    public final static BufferedInputStream newBufferedInputStream(final String filename) throws IOException
    {
        return new BufferedInputStream(new FileInputStream(filename));
    }

    public final static BufferedInputStream newBufferedInputStream(final File file) throws IOException
    {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public final static BufferedOutputStream newBufferedOutputStream(final String filename) throws IOException
    {
        return new BufferedOutputStream(new FileOutputStream(filename));
    }

    public final static BufferedOutputStream newBufferedOutputStream(final File file) throws IOException
    {
        return new BufferedOutputStream(new FileOutputStream(file));
    }
}
