/*
 * Copyright 2014 Blazebit.
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

package com.blazebit.persistence;

import com.blazebit.persistence.entity.Workflow;
import com.blazebit.persistence.function.ConcatenateFunction;
import com.blazebit.persistence.function.ZeroFunction;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spi.JpqlFunction;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.persistence.Tuple;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Christian Beikov
 * @since 1.0
 */
public class FunctionTest extends AbstractCoreTest {
    
    @Override
    protected Class<?>[] getEntityClasses() {
        return new Class<?>[]{
            Workflow.class
        };
    }
    
    @Test
    public void testCustomFunctionNoArgs() {
        CriteriaBuilder<Tuple> cb = cbf.create(em, Tuple.class).from(Workflow.class)
            .select("FUNCTION('zero')");
        String expectedQuery = "SELECT " + function("zero") + " FROM Workflow workflow";
        assertEquals(expectedQuery, cb.getQueryString());
    }
    
    @Test
    public void testCustomFunctionSingleArg() {
        CriteriaBuilder<Tuple> cb = cbf.create(em, Tuple.class).from(Workflow.class)
            .select("FUNCTION('zero', id)");
        String expectedQuery = "SELECT " + function("zero", "workflow.id") + " FROM Workflow workflow";
        assertEquals(expectedQuery, cb.getQueryString());
    }
    
    @Test
    public void testCustomFunctionMultipleArgs() {
        CriteriaBuilder<Tuple> cb = cbf.create(em, Tuple.class).from(Workflow.class)
            .select("FUNCTION('zero', id, id)");
        String expectedQuery = "SELECT " + function("zero", "workflow.id", "workflow.id") + " FROM Workflow workflow";
        assertEquals(expectedQuery, cb.getQueryString());
    }
}
