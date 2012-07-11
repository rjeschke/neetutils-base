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
package com.github.rjeschke.neetutils;

/**
 * Generic Exception to wrap checked exceptions into unchecked exceptions.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 */
public class WrappedCheckedException extends RuntimeException
{
    private static final long serialVersionUID = 8291912688634403030L;

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public WrappedCheckedException(Exception e)
    {
        super(e);
    }

    /**
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return this.getCause().getMessage();
    }

    /**
     * @see Throwable#toString()
     */
    @Override
    public String toString()
    {
        return this.getCause().toString();
    }
}