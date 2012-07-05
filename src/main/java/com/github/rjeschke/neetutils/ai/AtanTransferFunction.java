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

import java.io.IOException;

import com.github.rjeschke.neetutils.io.NOutputStream;

public class AtanTransferFunction implements TransferFunction
{
    final static double REC_PI = 1.0 / Math.PI;
    
    @Override
    public double map(double input)
    {
        return 0.5 - Math.atan(-input) * REC_PI;
    }

    @Override
    public void toStream(NOutputStream out) throws IOException
    {
        out.write32(TransferFunctionType.ATAN.index);
    }
}
