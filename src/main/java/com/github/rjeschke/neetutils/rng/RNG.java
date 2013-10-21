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
package com.github.rjeschke.neetutils.rng;

/**
 * Random number generator interface.
 *
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public interface RNG
{
    /**
     * Returns a random integer number.
     *
     * @return A number between <code>Integer.MIN_VALUE</code> and <code>Integer.MAX_VALUE</code>
     */
    public int nextInt();

    /**
     * Returns a random integer number.
     *
     * @return A number between <code>0</code> and <code>max - 1</code>
     */
    public int nextInt(int max);

    /**
     * Returns a random float number.
     *
     * @return A float between 0.0 and 1.0 (exclusively).
     */
    public float nextFloatUnipolar();

    /**
     * Returns a random float number.
     *
     * @return A float between -1.0 and 1.0 (exclusively).
     */
    public float nextFloatBipolar();

    /**
     * Returns a random double number.
     *
     * @return A double between 0.0 and 1.0 (exclusively).
     */
    public double nextDoubleUnipolar();

    /**
     * Returns a random double number.
     *
     * @return A double between -1.0 and 1.0 (exclusively).
     */
    public double nextDoubleBipolar();
}
