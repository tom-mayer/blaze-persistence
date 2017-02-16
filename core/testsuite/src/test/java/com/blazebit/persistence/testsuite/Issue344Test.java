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

package com.blazebit.persistence.testsuite;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.testsuite.entity.SchemaEntity;
import org.junit.Test;

/**
 *
 * @author Christian Beikov
 * @since 1.2.0
 */
public class Issue344Test extends AbstractCoreTest {

    @Override
    protected Class<?>[] getEntityClasses() {
        return new Class[]{
                SchemaEntity.class
        };
    }

    @Test
    public void testBuild() {
        CriteriaBuilder<SchemaEntity> criteria = cbf.create(em, SchemaEntity.class, "d");
        criteria.getQueryString();
        // Can't actually run this because the schema does not exist, but at least building the model worked
//        criteria.getResultList();
    }
}
