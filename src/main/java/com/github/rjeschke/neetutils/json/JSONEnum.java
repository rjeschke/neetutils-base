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

/**
 * An interface intended to be used with {@link JSONMarshallable} on {@code enums}.
 * <p>
 * If you implement this interface then you also have to implement a {@code static <enum> fromJSONString(final String string)} method with transforms the given
 * String into an enum.
 * </p>
 * <p>
 * If you don't use this interface on enums, enums will be serialized by {@link Enum#toString()} and deserialized using {@link Enum#valueOf(String)}.
 * </p>
 *
 * @author René Jeschke (rene_jeschke@yahoo.de)
 *
 */
public interface JSONEnum
{
    public String toJSONString();
}
