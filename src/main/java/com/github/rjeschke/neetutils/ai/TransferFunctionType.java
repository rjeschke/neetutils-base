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
package com.github.rjeschke.neetutils.ai;

import java.util.HashMap;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
@Deprecated
public enum TransferFunctionType
{
    UNITY(0), STEP(1), LINEAR(2), TANH(3), SIGMOID(4), ATAN(5);

    public final int                                            index;
    private final static HashMap<Integer, TransferFunctionType> TYPE_MAP = new HashMap<>();

    private TransferFunctionType(final int i)
    {
        this.index = i;
    }

    static
    {
        for (final TransferFunctionType t : TransferFunctionType.values())
        {
            TYPE_MAP.put(t.index, t);
        }
    }

    public static TransferFunctionType fromInt(final int i)
    {
        return TYPE_MAP.get(i);
    }
}
