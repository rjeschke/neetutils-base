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
import java.util.HashMap;

import com.github.rjeschke.neetutils.SysUtils;
import com.github.rjeschke.neetutils.ai.Neuron.NConnect;
import com.github.rjeschke.neetutils.io.NInputStream;
import com.github.rjeschke.neetutils.io.NOutputStream;

public class Net
{
    ArrayList<Layer> layers = new ArrayList<Layer>();
    
    public Net()
    {
        //
    }
    
    public void addLayer(int neurons)
    {
        this.addLayer(neurons, null);
    }

    public void addLayer(int neurons, TransferFunction tf)
    {
        if(this.layers.isEmpty())
        {
            this.layers.add(new Layer(LayerType.INPUT, neurons, 1, tf == null ? new UnityTransferFunction() : tf));
        }
        else
        {
            if(tf == null)
                throw new IllegalArgumentException("TransferFunction can not be null");
            final Layer l = this.layers.get(this.layers.size() - 1);
            this.layers.add(new Layer(LayerType.HIDDEN, neurons, l.neurons.length, tf));
        }
    }
    
    public void finish()
    {
        if(this.layers.size() < 2)
            throw new IllegalStateException("At least two layers are necessary");
        this.layers.get(this.layers.size() - 1).type = LayerType.OUTPUT;
        
        for(int i = 0; i < this.layers.size() - 1; i++)
        {
            final Layer l0 = this.layers.get(i);
            final Layer l1 = this.layers.get(i + 1);
            for(int t0 = 0; t0 < l0.neurons.length; t0++)
            {
                final Neuron no = l0.neurons[t0];
                for(int t1 = 0; t1 < l1.neurons.length; t1++)
                {
                    no.connect(l1.neurons[t1], t0);
                }
            }
        }
        final Layer l = this.layers.get(0);
        for(int t0 = 0; t0 < l.neurons.length; t0++)
        {
            final Neuron no = l.neurons[t0];
            no.bias = 0;
            for(int t1 = 0; t1 < no.weights.length; t1++)
            {
                no.weights[t1] = 1;
            }
        }
        int nc = 0;
        for(int i = 0; i < this.layers.size(); i++)
        {
            final Layer l0 = this.layers.get(i);
            for(int t0 = 0; t0 < l0.neurons.length; t0++)
                l0.neurons[t0].index = nc++;
        }
    }
    
    public void randomize()
    {
        for(int i = 1; i < this.layers.size(); i++)
        {
            final Layer l = this.layers.get(i);
            for(int n = 0; n < l.neurons.length; n++)
            {
                final Neuron no = l.neurons[n];
                no.bias = SysUtils.rndDoubleBipolar();
                for(int t = 0; t < no.weights.length; t++)
                    no.weights[t] = SysUtils.rndDoubleUnipolar();
            }
        }
    }
    
    public double[] run(double[] inputs, double[] outputs)
    {
        Layer l;
        for(int i = 0; i < this.layers.size(); i++)
            this.layers.get(i).reset();
        
        l = this.layers.get(0);
        for(int n = 0; n < l.neurons.length; n++)
        {
            l.neurons[n].setInput(0, inputs[n]);
        }
        
        for(int i = 0; i < this.layers.size(); i++)
        {
            l = this.layers.get(i);
            for(int n = 0; n < l.neurons.length; n++)
                l.neurons[n].propagate();
        }

        l = this.layers.get(this.layers.size() - 1);
        for(int i = 0; i < l.neurons.length; i++)
            outputs[i] = l.neurons[i].output;
        
        return outputs;
    }
    
    public double[] train(double[] inputs, double[] outputs, double learn)
    {
        double[] outs = new double[outputs.length];
        
        this.run(inputs, outs);

        Layer l = this.layers.get(this.layers.size() - 1);
        for(int i = 0; i < l.neurons.length; i++)
            l.neurons[i].setExpectedOutputValue(outputs[i]);
        
        for(int i = this.layers.size() - 2; i >= 0; i--)
        {
            l = this.layers.get(i);
            for(int n = 0; n < l.neurons.length; n++)
                l.neurons[n].backPropagatePrepare();
        }

        for(int i = this.layers.size() - 2; i >= 0; i--)
        {
            l = this.layers.get(i);
            for(int n = 0; n < l.neurons.length; n++)
                l.neurons[n].backPropagate(learn);
        }
        
        return outs;
    }
    
    public void toStream(NOutputStream out) throws IOException
    {
        out.write32(this.layers.size());
        for(Layer l : this.layers)
            l.toStream(out);
    }
    
    public static Net fromStream(NInputStream in) throws IOException
    {
        HashMap<Integer, Neuron> map = new HashMap<Integer, Neuron>();
        Net net = new Net();
        int ls = in.readI32();
        for(int i = 0; i < ls; i++)
            net.layers.add(Layer.fromStream(in));
        
        for(Layer l : net.layers)
        {
            for(Neuron n : l.neurons)
                map.put(n.index, n);
        }
        
        for(Layer l : net.layers)
        {
            for(Neuron n : l.neurons)
                for(NConnect nc : n.cons)
                    nc.n = map.get(nc.n.index);
        }

        return net;
    }
}
