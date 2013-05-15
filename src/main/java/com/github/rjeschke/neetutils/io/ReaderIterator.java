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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.rjeschke.neetutils.WrappedCheckedException;
import com.github.rjeschke.neetutils.math.Numbers;

public class ReaderIterator implements Iterable<Character>, Closeable
{
    final Reader     in;
    boolean          closed        = false;
    volatile boolean iteratorInUse = false;
    int              current;

    public ReaderIterator(final Reader in)
    {
        this.in = in;
        this.current = this.read();
    }

    int read()
    {
        if (this.closed) return this.current = -1;
        try
        {
            this.current = this.in.read();
            if (this.current == -1)
            {
                this.in.close();
                this.closed = true;
            }
        }
        catch (final IOException e)
        {
            this.current = -1;
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
    public synchronized Iterator<Character> iterator()
    {
        if (this.iteratorInUse) throw new IllegalStateException("An iterator is already in use");
        return new StreamIterator(this);
    }

    private class StreamIterator implements Iterator<Character>
    {
        final ReaderIterator ri;

        public StreamIterator(final ReaderIterator ri)
        {
            this.ri = ri;
        }

        @Override
        public boolean hasNext()
        {
            return this.ri.current != -1;
        }

        @Override
        public Character next()
        {
            if (!this.hasNext()) throw new NoSuchElementException("Trying to read past end of stream");
            final Character ret = Numbers.characterOf((char)this.ri.current);
            this.ri.read();
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
        this.current = -1;
    }
}
