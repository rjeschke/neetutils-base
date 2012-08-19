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
package com.github.rjeschke.neetutils.audio;

public class LPF24dBConfLadder
{
    private LPF6dBVar l0, l1, l2, l3;
    private double coef, q = 0;

    public LPF24dBConfLadder(double fs)
    {
        this.l0 = new LPF6dBVar(fs);
        this.l1 = new LPF6dBVar(fs);
        this.l2 = new LPF6dBVar(fs);
        this.l3 = new LPF6dBVar(fs);
        this.setCutoff(fs * 0.1);
        this.setLadderResponse();
    }

    public LPF24dBConfLadder setLadderResponse()
    {
        return this.setCoefficients(1, 1, 1, 1);
    }

    public LPF24dBConfLadder setTBResponse()
    {
        return this.setCoefficients(0.128018856477392, 1.03819857760185, 2.325387083413, 3.2355668045375);
    }

    public LPF24dBConfLadder setEMSResponse()
    {
        return this.setCoefficients(0.082932801682732, 0.9656890053367, 2.7561575531646, 4.5303560551817);
    }

    public LPF24dBConfLadder setDResponse()
    {
        return this.setCoefficients(0.083518178391154, 1.0139013273720, 2.7987196223090, 4.2195282830636);
    }

    public LPF24dBConfLadder setCoefficients(double a, double b, double c, double d)
    {
        this.l0.setFeedback(a);
        this.l1.setFeedback(b);
        this.l2.setFeedback(c);
        this.l3.setFeedback(d);
        this.recalc();
        return this;
    }

    public void setCutoff(final double freq)
    {
        this.l0.setCutoff(freq);
        this.l1.setCutoff(freq);
        this.l2.setCutoff(freq);
        this.l3.setCutoff(freq);
        this.recalc();
    }

    public void setQ(double q)
    {
        this.q = q;
        this.recalc();
    }

    private void recalc()
    {
        this.coef = this.q / (1.0 + this.q * this.l3.coef(this.l2.coef(this.l1.coef(this.l0.coef(1)))));
    }

    public void reset()
    {
        this.l0.reset();
        this.l1.reset();
        this.l2.reset();
        this.l3.reset();
    }

    public LPF24dBConfLadder setClipper(Clipper clipper)
    {
        this.l0.setClipper(clipper);
        this.l1.setClipper(clipper);
        this.l2.setClipper(clipper);
        this.l3.setClipper(clipper);
        return this;
    }

    public double process(double input)
    {
        return this.l3.tick(this.l2.tick(this.l1.tick(this.l0.tick(input
                - this.l3.output(this.l2.output(this.l1.output(this.l0.output(input)))) * this.coef))));
    }
}
