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
package com.github.rjeschke.neetutils.fn;

public final class CharacterFns
{
    private CharacterFns()
    {
        // 1up
    }

    public final static FnPredicate<Character> notWhitespace()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return !Character.isWhitespace(a) && !Character.isSpaceChar(a);
            }
        };
    }

    public final static FnPredicate<Character> isWhitespace()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isWhitespace(a) || Character.isSpaceChar(a);
            }
        };
    }

    public final static FnPredicate<Character> isLetter()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isLetter(a);
            }
        };
    }

    public final static FnPredicate<Character> notLetter()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return !Character.isLetter(a);
            }
        };
    }

    public final static FnPredicate<Character> isDigit()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isDigit(a);
            }
        };
    }

    public final static FnPredicate<Character> notDigit()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return !Character.isDigit(a);
            }
        };
    }

    public final static FnPredicate<Character> isLetterOrDigit()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isLetterOrDigit(a);
            }
        };
    }

    public final static FnPredicate<Character> notLetterOrDigit()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return !Character.isLetterOrDigit(a);
            }
        };
    }

    public final static FnPredicate<Character> isUpperCase()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isUpperCase(a);
            }
        };
    }

    public final static FnPredicate<Character> isLowerCase()
    {
        return new FnPredicate<Character>()
        {
            @Override
            public boolean applyPredicate(final Character a)
            {
                return Character.isLowerCase(a);
            }
        };
    }
}
