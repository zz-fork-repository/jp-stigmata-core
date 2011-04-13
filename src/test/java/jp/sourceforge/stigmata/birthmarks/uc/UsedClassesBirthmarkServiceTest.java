package jp.sourceforge.stigmata.birthmarks.uc;

import jp.sourceforge.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class UsedClassesBirthmarkServiceTest{
    private UsedClassesBirthmarkService service;

    @Before
    public void setUp(){
        service = new UsedClassesBirthmarkService();
    }

    @Test
    public void testBasic(){
        Assert.assertEquals("uc", service.getType());
        Assert.assertEquals("Used classes birthmark", service.getDescription());
        Assert.assertFalse(service.isExperimental());
        Assert.assertFalse(service.isUserDefined());
        Assert.assertEquals(UsedClassesBirthmarkExtractor.class, service.getExtractor().getClass());
        Assert.assertEquals(LogicalAndBirthmarkComparator.class, service.getComparator().getClass());
        Assert.assertNull(service.getPreprocessor());
    }
}
