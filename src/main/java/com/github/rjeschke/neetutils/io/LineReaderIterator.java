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

public class LineReaderIterator implements Iterable<String>
{
    final BufferedReader in;
    boolean closed = false;
    volatile boolean iteratorInUse = false;
    String current;
    
    public LineReaderIterator(BufferedReader in)
    {
        this.in = in;
        this.current = this.read();
    }
    
    String read()
    {
        if(this.closed)
            return this.current = null;
        try
        {
            this.current = this.in.readLine();
            
            if(this.current == null)
            {
                this.closed = true;
                this.in.close();
            }
        }
        catch(IOException e)
        {
            this.closed = true;
            this.current = null;
            try
            {
                this.in.close();
            }
            catch (IOException e1)
            {
                throw new WrappedCheckedException(e);
            }
        }
        return this.current;
    }
    
    @Override
    public synchronized Iterator<String> iterator()
    {
        if(this.iteratorInUse)
            throw new IllegalStateException("An iterator is already in use");
        return new StreamIterator(this);
    }

    private class StreamIterator implements Iterator<String>
    {
        final LineReaderIterator ist;
        
        public StreamIterator(LineReaderIterator ist)
        {
            this.ist = ist; 
        }

        @Override
        public boolean hasNext()
        {
            return this.ist.current != null;
        }

        @Override
        public String next()
        {
            if(!this.hasNext())
                throw new NoSuchElementException("Trying to read past end of stream");
            final String ret = this.ist.current;
            this.ist.read();
            return ret;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Can not remove characters from an InputStream");
        }
    }
}
