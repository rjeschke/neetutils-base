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

/**
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
@Deprecated
public class BackpropMomentumTrainer implements Trainer
{
    double step;
    double alpha;
    Net    net;
    Net    oldDeltas;
    double min, max, sum;

    public BackpropMomentumTrainer(final Net net, final double step, final double alpha)
    {
        this.net = net;
        this.step = step;
        this.alpha = alpha;
        this.oldDeltas = net.clone().clear();
    }

    public double getDeltaChangeMinimum()
    {
        return this.min;
    }

    public double getDeltaChangeMaximum()
    {
        return this.max;
    }

    public double getDeltaChangeAverage()
    {
        return this.sum;
    }

    @Override
    public void train(final double[] input, final double[] expectedOutput)
    {
        final State[] netState = this.net.createExtraStates(input);
        final State[] deltas = this.net.createExtraStates(new double[this.net.numInputs]);
        this.net.run(netState);

        double[] os, ds, dso;

        os = netState[netState.length - 1].values;
        ds = deltas[netState.length - 1].values;
        for (int i = 0; i < os.length; i++)
        {
            final double o = os[i];
            ds[i] = o * (1.0 - o) * (expectedOutput[i] - o);
        }

        for (int i = this.net.layers.length - 1; i >= 0; i--)
        {
            final Layer l = this.net.layers[i];

            os = netState[i].values;
            ds = deltas[i + 1].values;
            dso = deltas[i].values;

            for (int x = 0; x < l.numInputs; x++)
            {
                double e = 0;
                for (int y = 0; y < l.numOutputs; y++)
                {
                    e += ds[y] * l.matrix[y * l.width + x];
                }
                final double o = os[x];
                dso[x] = o * (1 - o) * e;
            }
        }

        this.max = this.sum = 0;
        this.min = Double.MAX_VALUE;

        int runs = 0;

        for (int i = 0; i < this.net.layers.length; i++)
        {
            final Layer l = this.net.layers[i];
            final Layer l2 = this.oldDeltas.layers[i];
            runs += (l.numInputs + 1) * l.numOutputs;

            for (int y = 0; y < l.numOutputs; y++)
            {
                final int p = y * l.width;
                final double d = this.step * deltas[i + 1].values[y];
                l.matrix[p + l.numInputs] += l2.matrix[p + l.numInputs] = this.updateDeltas(d + this.alpha
                        * l2.matrix[p + l.numInputs]);

                for (int x = 0; x < l.numInputs; x++)
                    l.matrix[p + x] += l2.matrix[p + x] = this.updateDeltas(d * netState[i].values[x] + this.alpha
                            * l2.matrix[p + x]);

            }
        }

        if (runs != 0) this.sum /= runs;
    }

    private double updateDeltas(final double delta)
    {
        final double da = Math.abs(delta);
        this.min = Math.min(this.min, da);
        this.max = Math.max(this.max, da);
        this.sum += da;
        return delta;
    }

    public void setStep(final double v)
    {
        this.step = v;
    }

    public void setAlpha(final double v)
    {
        this.alpha = v;
    }
}
