package jp.naist.se.stigmata;

/*
 * $Id$
 */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * test case.
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class StigmataTest{
    private BirthmarkContext context;

    @Before
    public void prepare(){
        context = Stigmata.getInstance().createContext();
    }

    @Test
    public void checkAvailableServices() throws Exception{
        Assert.assertNotNull(context.getService("smc"));
        Assert.assertNotNull(context.getService("cvfv"));
        Assert.assertNotNull(context.getService("is"));
        Assert.assertNotNull(context.getService("uc"));
    }

    @Test
    public void checkSmcBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("smc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            service.getComparator().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator"
        );
        Assert.assertEquals(
            service.getExtractor().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkExtractor"
        );
    }

    @Test
    public void checkCvfvBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("cvfv");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            service.getComparator().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator"
        );
        Assert.assertEquals(
            service.getExtractor().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.cvfv.ConstantValueOfFieldVariableBirthmarkExtractor"
        );
    }

    @Test
    public void checkIsBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("is");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            service.getComparator().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator"
        );
        Assert.assertEquals(
            service.getExtractor().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor"
        );
    }

    @Test
    public void checkUcBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("uc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            service.getComparator().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator"
        );
        Assert.assertEquals(
            service.getExtractor().getClass().getName(),
            "jp.naist.se.stigmata.birthmarks.uc.UsedClassesBirthmarkExtractor"
        );
    }
}
