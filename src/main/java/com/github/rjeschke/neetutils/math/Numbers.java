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
package com.github.rjeschke.neetutils.math;

import com.github.rjeschke.neetutils.fn.FnCombine;
import com.github.rjeschke.neetutils.fn.FnFoldStep;

public final class Numbers
{
    private Numbers()
    {
        //
    }

    private final static Integer[] integerCache;
    private final static Character[] characterCache;
    private final static int maxIntCache;
    private final static boolean hasCharacterCache;
    
    static
    {
        maxIntCache = System.getProperty("com.github.rjeschke.neetutils.smallIntegerCache") != null ? 256 : 65536;
        hasCharacterCache = System.getProperty("com.github.rjeschke.neetutils.noCharacterCache") == null;
        
        integerCache = new Integer[128 + maxIntCache];
        for(int i = -128; i < maxIntCache; i++)
        {
            integerCache[i + 128] = Integer.valueOf(i);
        }
        
        if(hasCharacterCache)
        {
            characterCache = new Character[65536];
            for(int i = 0; i < 65536; i++)
            {
                characterCache[i] = Character.valueOf((char)i);
            }
        }
        else
        {
            characterCache = null;
        }
    }

    public final static Integer integerOf(int value)
    {
        return (value > -129 && value < maxIntCache) ? integerCache[value + 128] : Integer.valueOf(value);
    }

    public final static Character characterOf(char value)
    {
        return hasCharacterCache ? characterCache[value] : Character.valueOf(value);
    }
    
    public final static Type getType(Number a)
    {
        if(a instanceof Byte) return Type.BYTE;
        if(a instanceof Short) return Type.SHORT;
        if(a instanceof Integer) return Type.INT;
        if(a instanceof Long) return Type.LONG;
        if(a instanceof Float) return Type.FLOAT;
        if(a instanceof Double) return Type.DOUBLE;
        throw new ArithmeticException("Unsupported number type: " + a);
    }

    private final static Type getLargerType(Number a, Number b)
    {
        final Type ta = getType(a);
        final Type tb = getType(b);
        return tb.size > ta.size ? tb : ta;
    }

    public final static Number add(Number a, Number b)
    {
        switch(getLargerType(a, b))
        {
        case BYTE:
            return Byte.valueOf((byte)(a.byteValue() + b.byteValue()));
        case SHORT:
            return Short.valueOf((short)(a.shortValue() + b.shortValue()));
        case INT:
            return integerOf(a.intValue() + b.intValue());
        case LONG:
            return Long.valueOf(a.longValue() + b.longValue());
        case FLOAT:
            return Float.valueOf(a.floatValue() + b.floatValue());
        case DOUBLE:
            return Double.valueOf(a.doubleValue() + b.doubleValue());
        }
        return null;
    }

    public final static FnCombine<Number, Number, Number> add()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(Number a, Number b)
            {
                return Numbers.add(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> addFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(Number a, Number b)
            {
                return Numbers.add(a, b);
            }
        };
    }

    public final static Number sub(Number a, Number b)
    {
        switch(getLargerType(a, b))
        {
        case BYTE:
            return Byte.valueOf((byte)(a.byteValue() - b.byteValue()));
        case SHORT:
            return Short.valueOf((short)(a.shortValue() - b.shortValue()));
        case INT:
            return integerOf(a.intValue() - b.intValue());
        case LONG:
            return Long.valueOf(a.longValue() - b.longValue());
        case FLOAT:
            return Float.valueOf(a.floatValue() - b.floatValue());
        case DOUBLE:
            return Double.valueOf(a.doubleValue() - b.doubleValue());
        }
        return null;
    }

    public final static FnCombine<Number, Number, Number> sub()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(Number a, Number b)
            {
                return Numbers.sub(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> subFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(Number a, Number b)
            {
                return Numbers.sub(a, b);
            }
        };
    }

    public final static Number mul(Number a, Number b)
    {
        switch(getLargerType(a, b))
        {
        case BYTE:
            return Byte.valueOf((byte)(a.byteValue() * b.byteValue()));
        case SHORT:
            return Short.valueOf((short)(a.shortValue() * b.shortValue()));
        case INT:
            return integerOf(a.intValue() * b.intValue());
        case LONG:
            return Long.valueOf(a.longValue() * b.longValue());
        case FLOAT:
            return Float.valueOf(a.floatValue() * b.floatValue());
        case DOUBLE:
            return Double.valueOf(a.doubleValue() * b.doubleValue());
        }
        return null;
    }

    public final static FnCombine<Number, Number, Number> mul()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(Number a, Number b)
            {
                return Numbers.mul(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> mulFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(Number a, Number b)
            {
                return Numbers.mul(a, b);
            }
        };
    }

    public final static Number div(Number a, Number b)
    {
        switch(getLargerType(a, b))
        {
        case BYTE:
            return Byte.valueOf((byte)(a.byteValue() / b.byteValue()));
        case SHORT:
            return Short.valueOf((short)(a.shortValue() / b.shortValue()));
        case INT:
            return integerOf(a.intValue() / b.intValue());
        case LONG:
            return Long.valueOf(a.longValue() / b.longValue());
        case FLOAT:
            return Float.valueOf(a.floatValue() / b.floatValue());
        case DOUBLE:
            return Double.valueOf(a.doubleValue() / b.doubleValue());
        }
        return null;
    }

    public final static FnCombine<Number, Number, Number> div()
    {
        return new FnCombine<Number, Number, Number>()
        {
            @Override
            public Number applyCombine(Number a, Number b)
            {
                return Numbers.div(a, b);
            }
        };
    }

    public final static FnFoldStep<Number, Number> divFold()
    {
        return new FnFoldStep<Number, Number>()
        {
            @Override
            public Number applyFoldStep(Number a, Number b)
            {
                return Numbers.div(a, b);
            }
        };
    }

    public enum Type
    {
        BYTE(0), SHORT(1), INT(2), LONG(3), FLOAT(4), DOUBLE(5);

        public final int size;

        private Type(final int size)
        {
            this.size = size;
        }
    }
}
