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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.rjeschke.neetutils.WrappedCheckedException;
import com.github.rjeschke.neetutils.iterables.AbstractXIterable;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class LineReaderIterator extends AbstractXIterable<String> implements AutoCloseable
{
    final BufferedReader in;
    boolean              closed        = false;
    volatile boolean     iteratorInUse = false;
    String               current;

    public LineReaderIterator(final BufferedReader in)
    {
        this.in = in;
        this.current = this.read();
    }

    String read()
    {
        if (this.closed) return this.current = null;
        try
        {
            this.current = this.in.readLine();

            if (this.current == null)
            {
                this.in.close();
                this.closed = true;
            }
        }
        catch (final IOException e)
        {
            this.current = null;
            try
            {
                this.in.close();
                this.closed = true;
            }
            catch (final IOException e1)
            {
                throw new WrappedCheckedException(e);
            }
        }
        return this.current;
    }

    @Override
    public synchronized Iterator<String> iterator()
    {
        if (this.iteratorInUse) throw new IllegalStateException("An iterator is already in use");
        return new StreamIterator(this);
    }

    private static class StreamIterator implements Iterator<String>
    {
        final LineReaderIterator lri;

        public StreamIterator(final LineReaderIterator lri)
        {
            this.lri = lri;
        }

        @Override
        public boolean hasNext()
        {
            return this.lri.current != null;
        }

        @Override
        public String next()
        {
            if (!this.hasNext()) throw new NoSuchElementException("Trying to read past end of stream");
            final String ret = this.lri.current;
            this.lri.read();
            return ret;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Can not remove characters from an InputStream");
        }
    }

    @Override
    public void close() throws IOException
    {
        this.in.close();
        this.closed = true;
        this.current = null;
    }
}
