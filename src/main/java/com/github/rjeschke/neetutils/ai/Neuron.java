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
import java.util.ArrayList;

import com.github.rjeschke.neetutils.io.NInputStream;
import com.github.rjeschke.neetutils.io.NOutputStream;

public class Neuron
{
    ArrayList<NConnect> cons = new ArrayList<NConnect>();
    TransferFunction tf;
    int index = 0;
    double bias;
    double[] weights;
    double netValue, output, delta;
    
    public Neuron(int inputs, TransferFunction tf)
    {
        this.tf = tf;
        this.weights = new double[inputs];
    }
    
    Neuron(int index)
    {
        this.index = index;
    }
    

    void connect(Neuron n, int i)
    {
        this.cons.add(new NConnect(n, i));
    }
    
    void toStream(NOutputStream out) throws IOException
    {
        out.write32(this.index);
        this.tf.toStream(out);
        out.writeDouble(this.bias);
        out.write32(this.weights.length);
        for(int i = 0; i < this.weights.length; i++)
            out.writeDouble(this.weights[i]);
        out.write32(this.cons.size());
        for(NConnect nc : this.cons)
        {
            out.write32(nc.n.index);
            out.write32(nc.i);
        }
    }
    
    static Neuron fromStream(NInputStream in) throws IOException
    {
        final int index = in.readI32();
        final TransferFunction tf = TransferFunctions.fromStream(in);
        final double bias = in.readDouble();
        final int inputs = in.readI32();
        final Neuron n = new Neuron(inputs, tf);
        n.index = index;
        n.bias = bias;
        for(int i = 0; i < inputs; i++)
            n.weights[i] = in.readDouble();
        final int cons = in.readI32();
        for(int i = 0; i < cons; i++)
        {
            final int nn = in.readI32();
            final int ni = in.readI32();
            n.cons.add(new NConnect(new Neuron(nn), ni));
        }
        return n;
    }
    
    public void reset()
    {
        this.netValue = 0;
    }
    
    public void setInput(int i, double d)
    {
        this.netValue += this.weights[i] * d;
    }

    static void check(double v)
    {
        if(Double.isInfinite(v) || Double.isNaN(v))
            System.err.println(v);
        if(Math.abs(v) >= 1e300)
            System.err.println(v);
    }
    
    public void propagate()
    {
        this.output = this.tf.map(this.netValue + this.bias);
        for(int i = 0; i < this.cons.size(); i++)
        {
            final NConnect nc = this.cons.get(i);
            nc.n.setInput(nc.i, this.output);
        }
    }

    void setExpectedOutputValue(double value)
    {
        this.delta = this.output * (1.0 - this.output) * (value - this.output);
    }
    
    void backPropagatePrepare()
    {
        double e = 0;
        for(int i = 0; i < this.cons.size(); i++)
        {
            final NConnect nc = this.cons.get(i);
            e += nc.n.delta * nc.n.weights[nc.i];
        }
        this.delta = this.output * (1.0 - this.output) * e;
    }

    void backPropagate(double learn)
    {
        this.bias += learn * this.delta;
        for(int i = 0; i < this.cons.size(); i++)
        {
            final NConnect nc = this.cons.get(i);
            nc.n.weights[nc.i] += learn * this.output * nc.n.delta;
        }
    }

    static class NConnect
    {
        public Neuron n;
        public final int i;
        
        public NConnect(final Neuron n, int i)
        {
            this.n = n;
            this.i = i;
        }
    }
}
