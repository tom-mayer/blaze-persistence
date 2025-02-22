/*
 * Copyright 2014 - 2023 Blazebit.
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

package com.blazebit.persistence.view.impl.objectbuilder.transformer.correlation;

import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.ObjectBuilder;
import com.blazebit.persistence.view.impl.EntityViewConfiguration;
import com.blazebit.persistence.view.spi.EmbeddingViewJpqlMacro;

/**
 *
 * @author Christian Beikov
 * @since 1.2.0
 */
public final class BasicCorrelator implements Correlator {

    @Override
    public int getElementOffset() {
        return 0;
    }

    @Override
    public ObjectBuilder<?> finish(FullQueryBuilder<?, ?> criteriaBuilder, EntityViewConfiguration entityViewConfiguration, int offset, int tupleSuffix, String correlationRoot, EmbeddingViewJpqlMacro embeddingViewJpqlMacro, boolean nullFlatViewIfEmpty) {
        criteriaBuilder.select(correlationRoot);
        return null;
    }

}
