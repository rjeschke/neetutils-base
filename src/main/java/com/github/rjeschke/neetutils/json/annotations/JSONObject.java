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
package com.github.rjeschke.neetutils.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.rjeschke.neetutils.json.JSONMarshallable;
import com.github.rjeschke.neetutils.json.JSONObjectVisibility;

/**
 * {@link JSONMarshallable} field visibility annotation.
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JSONObject
{
    /**
     * A bit-wise combination of {@link JSONObjectVisibility} values. Default is {@link JSONObjectVisibility#PUBLIC}.
     *
     * @return The visibility bit mask.
     */
    int visibility() default JSONObjectVisibility.PUBLIC;

    /**
     * Specify if {@code null} values are ignored or not. Default is {@code true}.
     *
     * @return If {@code null} should be ignored or not.
     */
    boolean ignoreNull() default true;
}
