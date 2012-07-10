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
import java.util.Arrays;

import com.github.rjeschke.neetutils.SysUtils;
import com.github.rjeschke.neetutils.ai.Layer.State;
import com.github.rjeschke.neetutils.io.NInputStream;
import com.github.rjeschke.neetutils.io.NOutputStream;

public class Net
{
    final Layer[] layers;
    public final int numInputs;
    public final int numOutputs;
    
    Net(int numLayers, int numInputs, int numOutputs)
    {
        this.layers = new Layer[numLayers];
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
    }

    @Override
    public Net clone()
    {
        final Net net = new Net(this.layers.length, this.numInputs, this.numOutputs);
        for(int i = 0; i < this.layers.length; i++)
            net.layers[i] = this.layers[i].clone();
        return net;
    }
    
    public Net randomize()
    {
        for(Layer l : this.layers)
        {
            for(int i = 0; i < l.matrix.length; i++)
                l.matrix[i] = SysUtils.rndDoubleBipolar();
        }
        return this;
    }
    
    public Net clear()
    {
        for(Layer l : this.layers)
            Arrays.fill(l.matrix, 0);
        return this;
    }
    
    public Net zeroNaNsAndInfs()
    {
        for(final Layer l : this.layers)
            l.zeroNaNsAndInfs();
        return this;
    }
    
    public double[] run(double[] inputs, double[] outputs)
    {
        final State[] states = this.createStates(outputs);
        
        for(int i = 0; i < this.layers.length; i++)
        {
            if(i == 0)
                this.layers[i].eval(inputs, states[i].values);
            else
                this.layers[i].eval(states[i - 1].values, states[i].values);
        }
        
        return outputs;
    }

    double[] run(State[] states)
    {
        for(int i = 0; i < this.layers.length; i++)
            this.layers[i].eval(states[i].values, states[i + 1].values);
        return states[this.layers.length].values;
    }

    State[] createStates(double[] outputs)
    {
        final State[] s = new State[this.layers.length];
        for(int i = 0; i < this.layers.length; i++)
        {
            if(i == this.layers.length - 1)
                s[i] = this.layers[i].createState(outputs);
            else
                s[i] = this.layers[i].createState();
        }
        return s;
    }
    
    State[] createExtraStates(double[] inputs)
    {
        final State[] s = new State[this.layers.length + 1];
        for(int i = 0; i < this.layers.length + 1; i++)
        {
            if(i == 0)
                s[i] = new State(inputs);
            else
                s[i] = this.layers[i - 1].createState();
        }
        return s;
    }
    
    public void toStream(NOutputStream out) throws IOException
    {
        out.write32(this.layers.length);
        out.write32(this.numInputs);
        out.write32(this.numOutputs);
        for(final Layer l : this.layers)
            l.toStream(out);
    }
    
    public static Net fromStream(NInputStream in) throws IOException
    {
        final int l = in.readI32();
        final int a = in.readI32();
        final int b = in.readI32();
        final Net net = new Net(l, a, b);
        for(int i = 0; i < l; i++)
            net.layers[i] = Layer.fromStream(in);
        return net;
    }
    
    public static Builder builder(int numInputs)
    {
        return new Builder(numInputs);
    }
    
    public static class Builder
    {
        private int numInputs;
        ArrayList<Integer> outputs = new ArrayList<Integer>();
        ArrayList<TransferFunction> tfs = new ArrayList<TransferFunction>();
        
        Builder(int numInputs)
        {
            this.numInputs = numInputs;
        }
        
        public Builder addLayer(int outputs, TransferFunction tf)
        {
            if(outputs < 1)
                throw new IllegalArgumentException("Number of outputs must be greater than zero");
            this.outputs.add(outputs);
            this.tfs.add(tf);
            return this;
        }
        
        public Net build()
        {
            if(this.outputs.size() == 0)
                throw new IllegalStateException("At least one layer is needed");
            
            final Net net = new Net(this.outputs.size(), this.numInputs, this.outputs.get(this.outputs.size() - 1));
            for(int i = 0; i < net.layers.length; i++)
            {
                net.layers[i] = new Layer(this.tfs.get(i), i == 0 ? this.numInputs : this.outputs.get(i - 1), this.outputs.get(i));
            }
            
            return net;
        }
    }
}
