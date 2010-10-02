/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.path.SimplePath;

public abstract class AbstractSQLTemplatesTest {
    
    private static final QSurvey survey1 = new QSurvey("survey1");
    
    private static final QSurvey survey2 = new QSurvey("survey2");
    
    protected SQLQueryImpl query = new SQLQueryImpl(createTemplates().newLineToSingleSpace());
    
    protected abstract SQLTemplates createTemplates();
    
    @Test
    public void NoFrom(){
        query.getMetadata().addProjection(new SimplePath<Integer>(Integer.class,"1"));
        assertEquals("select 1 from dual", query.toString());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union(){        
        SimplePath<Integer> one = new SimplePath<Integer>(Integer.class,"1");
        SimplePath<Integer> two = new SimplePath<Integer>(Integer.class,"2");
        SimplePath<Integer> three = new SimplePath<Integer>(Integer.class,"3");
        SimplePath<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        UnionBuilder union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 as col1 from dual) " +
        	"union " +
        	"(select 2 from dual) " +
        	"union " +
        	"(select 3 from dual)", union.toString());
    }
    
    @Test
    public void InnerJoin(){        
        query.from(survey1).innerJoin(survey2);
        assertEquals("from SURVEY survey1 inner join SURVEY survey2", query.toString());
    }
    
    protected SQLSubQuery sq(){
        return new SQLSubQuery();
    }
    
}
