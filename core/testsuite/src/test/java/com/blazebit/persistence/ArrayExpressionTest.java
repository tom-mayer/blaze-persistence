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

import com.blazebit.persistence.entity.Document;
import com.blazebit.persistence.spi.Criteria;
import java.util.List;
import javax.persistence.Tuple;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author ccbem
 */
public class ArrayExpressionTest extends AbstractPersistenceTest {

    @Test
    public void testSelectPathIndex() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[d.age]");

        assertEquals("SELECT contacts FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = d.age", criteria.getQueryString());
    }

    @Test
    public void testSelectParameterIndex() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[:age]");

        assertEquals("SELECT contacts FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = :age", criteria.getQueryString());
    }

    @Test
    public void testSelectMultipleArrayPath() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[:age].localized[d.age]");

        assertEquals("SELECT localized FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = :age LEFT JOIN contacts.localized localized WITH KEY(localized) = d.age", criteria.getQueryString());
    }

    @Test
    public void testSelectAlternatingArrayPath() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[:age].partnerDocument.versions[d.age]");

        assertEquals("SELECT versions FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = :age LEFT JOIN contacts.partnerDocument partnerDocument LEFT JOIN partnerDocument.versions versions WITH KEY(versions) = d.age", criteria.getQueryString());
    }

    @Test
    // select alias as index is not supported by JPQL
    public void testArrayIndexSelectAlias() {
//        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
//        criteria.select("SUM(d.owner.ownedDocuments.age)", "ageSum").select("d.contacts[:age].partnerDocument.versions[ageSum]");
//
//        assertEquals("SELECT SUM(ownedDocuments.age) AS ageSum, VALUE(versions) FROM Document d LEFT JOIN d.contacts contacts LEFT JOIN contacts.partnerDocument partnerDocument LEFT JOIN partnerDocument.versions versions LEFT JOIN d.owner owner LEFT JOIN owner.ownedDocuments ownedDocuments WHERE KEY(contacts) = :age AND KEY(versions) = ageSum", criteria.getQueryString());
    }

    @Test
    public void testArrayIndexImplicitJoin() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[d.versions.date]");

        assertEquals("SELECT contacts FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = versions.date LEFT JOIN d.versions versions", criteria.getQueryString());
    }
    
    @Test
    public void testArrayIndexExplicitJoinAlias() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("d.contacts[v.date]").leftJoin("d.versions", "v"); 
        
        assertEquals("SELECT contacts FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = v.date LEFT JOIN d.versions v", criteria.getQueryString());
    }
    
    @Test
    public void testRedundantArrayTransformation() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("contacts[1]").where("contacts[1]").ge(0);
        
        assertEquals("SELECT contacts FROM Document d LEFT JOIN d.contacts contacts WITH KEY(contacts) = 1 WHERE contacts >= :param_0", criteria.getQueryString());
    }
    
    @Test
    // Map dereferencing is actually not allowed in JPQL
    public void testMapDereferencing() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("owner.partnerDocument", "x").leftJoin("owner.partnerDocument", "p").where("p.contacts[1].name").ge(0);
        
        assertEquals("SELECT p AS x FROM Document d LEFT JOIN d.owner owner LEFT JOIN owner.partnerDocument p LEFT JOIN p.contacts contacts WITH KEY(contacts) = 1 WHERE contacts.name >= :param_0", criteria.getQueryString());
    }
    
    @Test
    public void testMore() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        criteria.select("owner.partnerDocument", "x").leftJoin("owner.partnerDocument", "p").leftJoin("p.contacts", "c").where("c[1]").ge(0);
        
        assertEquals("SELECT p AS x FROM Document d LEFT JOIN d.owner owner LEFT JOIN owner.partnerDocument p LEFT JOIN p.contacts c WITH KEY(c) = 1 WHERE c >= :param_0", criteria.getQueryString());
    }
    
    @Test
    public void testGetResultList() {
        CriteriaBuilder<Document> criteria = Criteria.from(em, Document.class, "d");
        CriteriaBuilder<Tuple> tupleCrit = criteria.select("owner.localized[1]", "l").leftJoin("owner.localized", "localized").leftJoin("d.contacts", "contacts").where("contacts[1].name").like("%arl%");
        System.out.println(tupleCrit.getQueryString());
        List<Tuple> results = tupleCrit.getResultList();

        
        em.createQuery("SELECT contacts AS l FROM Document d LEFT JOIN d.owner owner LEFT JOIN d.contacts contacts WITH KEY(contacts) = 1 WHERE contacts.name LIKE '%arl%'", Tuple.class).getResultList();
//        System.out.println(results);
    }
}
