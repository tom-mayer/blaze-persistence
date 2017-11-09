/*
 * Copyright 2014 - 2017 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.persistence.view.impl.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Christian Beikov
 * @since 1.2.0
 */
public class UnorderedMapInstantiator implements MapInstantiator<Map<?, ?>, RecordingMap<Map<?, ?>, ?, ?>> {

    private final Set<Class<?>> allowedSubtypes;
    private final boolean updatable;
    private final boolean optimize;

    public UnorderedMapInstantiator(Set<Class<?>> allowedSubtypes, boolean updatable, boolean optimize) {
        this.allowedSubtypes = allowedSubtypes;
        this.updatable = updatable;
        this.optimize = optimize;
    }

    @Override
    public Map<?, ?> createCollection(int size) {
        return new HashMap<>(size);
    }

    @Override
    public RecordingMap<Map<?, ?>, ?, ?> createRecordingCollection(int size) {
        return new RecordingMap(createCollection(size), allowedSubtypes, updatable, optimize);
    }
}