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

import com.github.rjeschke.neetutils.io.NInputStream;
import com.github.rjeschke.neetutils.io.NOutputStream;

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
@Deprecated
class Layer
{
    final TransferFunction tf;
    final int              numInputs;
    final int              numOutputs;
    final int              width;
    final double[]         matrix;

    Layer(final TransferFunction tf, final int numInputs, final int numOutputs)
    {
        this.tf = tf;
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
        this.width = numInputs + 1;
        this.matrix = new double[this.width * this.numOutputs];
    }

    @Override
    public Layer clone()
    {
        final Layer l = new Layer(this.tf, this.numInputs, this.numOutputs);
        System.arraycopy(this.matrix, 0, l.matrix, 0, this.matrix.length);
        return l;
    }

    double[] eval(final double[] inputs, final double[] outputs)
    {
        for (int y = 0; y < this.numOutputs; y++)
        {
            double o = 0;
            final int p = y * this.width;
            for (int x = 0; x < this.numInputs; x++)
                o += inputs[x] * this.matrix[p + x];
            outputs[y] = this.tf.map(o + this.matrix[p + this.numInputs]);
        }
        return outputs;
    }

    State createState()
    {
        return new State(this.numOutputs);
    }

    State createState(final double[] outputs)
    {
        return new State(outputs);
    }

    void toStream(final NOutputStream out) throws IOException
    {
        out.write32(this.numInputs);
        out.write32(this.numOutputs);
        this.tf.toStream(out);
        for (int i = 0; i < this.matrix.length; i++)
            out.writeDouble(this.matrix[i]);
    }

    public void zeroNaNsAndInfs()
    {
        for (int i = 0; i < this.matrix.length; i++)
        {
            final double d = this.matrix[i];
            if (Double.isNaN(d) || Double.isInfinite(d)) this.matrix[i] = 0;
        }
    }

    static Layer fromStream(final NInputStream in) throws IOException
    {
        final int a = in.readI32();
        final int b = in.readI32();
        final TransferFunction tf = TransferFunctions.fromStream(in);
        final Layer l = new Layer(tf, a, b);
        for (int i = 0; i < l.matrix.length; i++)
            l.matrix[i] = in.readDouble();
        return l;
    }

    static class State
    {
        public final double[] values;

        State(final int values)
        {
            this.values = new double[values];
        }

        State(final double[] values)
        {
            this.values = values;
        }
    }
}
