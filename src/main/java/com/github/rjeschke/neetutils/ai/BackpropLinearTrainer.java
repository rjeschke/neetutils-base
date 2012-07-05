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

import com.github.rjeschke.neetutils.ai.Layer.State;
import com.github.rjeschke.neetutils.ai.Layer;

public class BackpropLinearTrainer implements Trainer
{
    double step;
    Net net;
    State[] deltas;
    
    public BackpropLinearTrainer(Net net, double step)
    {
        this.net = net;
        this.step = step;
        this.deltas = net.createExtraStates(new double[net.numInputs]);
    }
    
    @Override
    public void train(double[] input, double[] expectedOutput)
    {
        final State[] netState = this.net.createExtraStates(input);
        this.net.run(netState);

        double[] os, ds, dso;
        
        os = netState[netState.length - 1].values;
        ds = this.deltas[netState.length - 1].values;
        for(int i = 0; i < os.length; i++)
        {
            final double o = os[i];
            ds[i] = o * (1.0 - o) * (expectedOutput[i] - o);
        }

        for(int i = this.net.layers.length - 1; i >= 0; i--)
        {
            Layer l = this.net.layers[i];
            
            os = netState[i].values;
            ds = this.deltas[i + 1].values;
            dso = this.deltas[i].values;
            
            for(int x = 0; x < l.numInputs; x++)
            {
                double e = 0;
                for(int y = 0; y < l.numOutputs; y++)
                {
                    e += ds[y] * l.matrix[y * l.width + x];
                }
                final double o = os[x];
                dso[x] = o * (1 - o) * e;
            }
        }

        for(int i = 0; i < this.net.layers.length; i++)
        {
            Layer l = this.net.layers[i];

            for(int y = 0; y < l.numOutputs; y++)
            {
                int p = y * l.width;
                double d = this.step * this.deltas[i + 1].values[y];
                l.matrix[p + l.numInputs] += d;
                
                for(int x = 0; x < l.numInputs; x++)
                {
                    l.matrix[p + x] += d * netState[i].values[x];
                }
            }
        }
    }
}
