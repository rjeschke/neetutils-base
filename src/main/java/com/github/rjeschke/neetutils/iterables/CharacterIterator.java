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
package com.github.rjeschke.neetutils.iterables;

import java.util.Iterator;

import com.github.rjeschke.neetutils.math.Numbers;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public class CharacterIterator extends AbstractXIterable<Character>
{
    private final String value;

    public CharacterIterator(final String value)
    {
        this.value = value;
    }

    public final static CharacterIterator of(final String value)
    {
        return new CharacterIterator(value);
    }

    @Override
    public Iterator<Character> iterator()
    {
        return new XIterator(this.value);
    }

    private final static class XIterator implements Iterator<Character>
    {
        private final String value;
        private int          position = 0;

        public XIterator(final String value)
        {
            this.value = value;
        }

        @Override
        public boolean hasNext()
        {
            return this.position < this.value.length();
        }

        @Override
        public Character next()
        {
            return Numbers.characterOf(this.value.charAt(this.position++));
        }

        @Override
        public void remove()
        {
            throw new IllegalStateException("CharacterIterator is read-only.");
        }
    }
}
