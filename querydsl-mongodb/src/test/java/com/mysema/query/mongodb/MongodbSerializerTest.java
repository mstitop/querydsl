package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mysema.query.types.Expr;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;

public class MongodbSerializerTest {
    
    private PathBuilder<Object> entityPath;
    private PString title;
    private PNumber<Integer> year;
    private PNumber<Double> gross;

    private PNumber<Long> longField;
    private PNumber<Short> shortField;
    private PNumber<Byte> byteField;
    private PNumber<Float> floatField;
    
    private MongodbSerializer serializer;
    
    @Before
    public void before() {
        serializer = new MongodbSerializer();
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        year = entityPath.getNumber("year", Integer.class);
        gross = entityPath.getNumber("gross", Double.class);
        longField = entityPath.getNumber("longField", Long.class);
        shortField = entityPath.getNumber("shortField", Short.class);
        byteField = entityPath.getNumber("byteField", Byte.class);
        floatField = entityPath.getNumber("floatField", Float.class);
    }
    
    @Test
    public void testEquals() {
        assertQuery(title.eq("A"), dbo("title","A"));
        assertQuery(year.eq(1), dbo("year",1));
        assertQuery(gross.eq(1.0D), dbo("gross", 1.0D));
        assertQuery(longField.eq(1L), dbo("longField", 1L));
        assertQuery(shortField.eq((short)1), dbo("shortField", 1));
        assertQuery(byteField.eq((byte)1), dbo("byteField", 1L));
        assertQuery(floatField.eq(1.0F), dbo("floatField", 1.0F));
    }
    
    @Test
    public void testEqAndEq() {
        assertQuery(
            title.eq("A").and(year.eq(1)), 
            dbo("title","A").append("year", 1)
        );
        
        assertQuery(
            title.eq("A").and(year.eq(1).and(gross.eq(1.0D))), 
            dbo("title","A").append("year", 1).append("gross", 1.0D)
        );
    }
    
    @Test
    public void testNotEq() {
        assertQuery(title.ne("A"), dbo("title", dbo("$ne", "A")));
    }
    
    
    
    private void assertQuery(Expr<?> e, BasicDBObject expected) {
        BasicDBObject result = (BasicDBObject) serializer.handle(e);
        assertEquals(expected, result);
    }

    private static BasicDBObject dbo(String key, Object value) {
        return new BasicDBObject(key, value);
    }
    
    
    

}
