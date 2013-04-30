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
package com.github.rjeschke.neetutils.json;

import com.github.rjeschke.neetutils.json.annotations.JSONObject;

/**
 * A tagging interface for simple JSON objects.
 * 
 * <p>
 * Fields with visibility specified by {@link JSONObject} get automatically
 * encoded to JSON or decoded from. You may specify a
 * {@code static <T> createJSONInstance()} method for automatic JSON decoding,
 * otherwise the default constructor is used.
 * </p>
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 */
public interface JSONMarshallable
{
    // Tagging interface
}
