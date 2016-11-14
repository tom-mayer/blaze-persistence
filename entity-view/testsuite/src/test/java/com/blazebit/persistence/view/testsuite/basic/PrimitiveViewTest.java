/*
 * Copyright 2014 - 2016 Blazebit.
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
package com.blazebit.persistence.view.testsuite.basic;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.testsuite.entity.PrimitiveDocument;
import com.blazebit.persistence.testsuite.entity.PrimitivePerson;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import com.blazebit.persistence.view.testsuite.AbstractEntityViewTest;
import com.blazebit.persistence.view.testsuite.basic.model.*;
import com.blazebit.persistence.view.testsuite.entity.Document;
import com.blazebit.persistence.view.testsuite.entity.Person;
import com.blazebit.persistence.view.testsuite.entity.Version;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityTransaction;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Christian Beikov
 * @since 1.2
 */
public class PrimitiveViewTest extends AbstractEntityViewTest {

    protected EntityViewManager evm;

    @Override
    protected Class<?>[] getEntityClasses() {
        return new Class<?>[]{
                PrimitiveDocument.class,
                PrimitivePerson.class
        };
    }

    @Before
    public void initEvm() {
        EntityViewConfiguration cfg = EntityViews.createDefaultConfiguration();
        cfg.addEntityView(PrimitiveSimpleDocumentView.class);
        cfg.addEntityView(PrimitiveDocumentView.class);
        cfg.addEntityView(PrimitivePersonView.class);
        evm = cfg.createEntityViewManager(cbf, em.getEntityManagerFactory());
    }

    private PrimitiveDocument doc1;
    private PrimitiveDocument doc2;

    private PrimitivePerson o1;
    private PrimitivePerson o2;

    @Before
    public void setUp() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            doc1 = new PrimitiveDocument("doc1");
            doc2 = new PrimitiveDocument("doc2");

            o1 = new PrimitivePerson("pers1");
            o2 = new PrimitivePerson("pers2");
            o1.setPartnerDocument(doc1);
            o2.setPartnerDocument(doc2);

            doc1.setOwner(o1);
            doc2.setOwner(o2);

            doc1.getContacts().put(1, o1);
            doc2.getContacts().put(1, o2);

            em.persist(o1);
            em.persist(o2);

            em.persist(doc1);
            em.persist(doc2);

            em.flush();
            tx.commit();
            em.clear();

            doc1 = em.find(PrimitiveDocument.class, doc1.getId());
            doc2 = em.find(PrimitiveDocument.class, doc2.getId());
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimple() {
        CriteriaBuilder<PrimitiveDocument> criteria = cbf.create(em, PrimitiveDocument.class, "d")
            .orderByAsc("id");
        EntityViewSetting<PrimitiveDocumentView, CriteriaBuilder<PrimitiveDocumentView>> setting;
        setting = EntityViewSetting.create(PrimitiveDocumentView.class);
        List<PrimitiveDocumentView> results = evm.applySetting(setting, criteria).getResultList();

        assertEquals(2, results.size());
        // Doc1
        assertEquals(doc1.getId(), results.get(0).getId());
        assertEquals(doc1.getName(), results.get(0).getName());
        assertEquals(o1.getId(), results.get(0).getOwner().getId().longValue());
        assertEquals(o1.getName(), results.get(0).getOwner().getName());
        // Doc2
        assertEquals(doc2.getId(), results.get(1).getId());
        assertEquals(doc2.getName(), results.get(1).getName());
        assertEquals(o2.getId(), results.get(1).getOwner().getId().longValue());
        assertEquals(o2.getName(), results.get(1).getOwner().getName());

        results.get(0).setId(123L);
        results.get(0).setName("Abc");
    }
}