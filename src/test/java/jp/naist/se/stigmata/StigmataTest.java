package jp.naist.se.stigmata;

/*
 * $Id$
 */

import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * test case.
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class StigmataTest{
    private BirthmarkEnvironment context;

    @Before
    public void prepare(){
        context = Stigmata.getInstance().createEnvironment();
    }

    @Test
    public void checkAvailableServices() throws Exception{
        Assert.assertNotNull(context.getService("smc"));
        Assert.assertNotNull(context.getService("cvfv"));
        Assert.assertNotNull(context.getService("is"));
        Assert.assertNotNull(context.getService("uc"));
        Assert.assertNotNull(context.getService("kgram"));
    }

    @Test
    public void checkSmcBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("smc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkCvfvBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("cvfv");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.cvfv.ConstantValueOfFieldVariableBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkIsBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("is");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkUcBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("uc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.uc.UsedClassesBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkKgramBirthmarkService() throws Exception{
        BirthmarkSpi service = context.getService("kgram");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.naist.se.stigmata.birthmarks.kgram.KGramBasedBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }
}
