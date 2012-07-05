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

public class Layer
{
    LayerType type;
    Neuron[] neurons;
    
    Layer(LayerType type, int neurons, int inputs, TransferFunction tf)
    {
        this.type = type;
        this.neurons = new Neuron[neurons];
        for(int i = 0; i < neurons; i++)
            this.neurons[i] = new Neuron(inputs, tf);
    }
    
    Layer(LayerType type, int neurons)
    {
        this.type = type;
        this.neurons = new Neuron[neurons];
    }
    
    public void reset()
    {
        for(int n = 0; n < this.neurons.length; n++)
            this.neurons[n].reset();
    }
    
    public void toStream(NOutputStream out) throws IOException
    {
        out.write32(this.type.index);
        out.write32(this.neurons.length);
        for(Neuron n : this.neurons)
            n.toStream(out);
    }
    
    public static Layer fromStream(NInputStream in) throws IOException
    {
        LayerType lt = LayerType.fromInt(in.readI32());
        int count = in.readI32();
        Layer l = new Layer(lt, count);
        for(int i = 0; i < count; i++)
            l.neurons[i] = Neuron.fromStream(in);
        return l;
    }
}
