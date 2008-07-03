package jp.sourceforge.stigmata;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * test case.
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class StigmataTest{
    private BirthmarkContext context;
    private BirthmarkEnvironment environment;

    @Before
    public void prepare(){
        Stigmata stigmata = Stigmata.getInstance();
        context = stigmata.createContext();
        environment = context.getEnvironment();
    }

    @Test
    public void checkAvailableServices() throws Exception{
        Assert.assertNotNull(environment.getService("smc"));
        Assert.assertNotNull(environment.getService("cvfv"));
        Assert.assertNotNull(environment.getService("is"));
        Assert.assertNotNull(environment.getService("uc"));
        Assert.assertNotNull(environment.getService("kgram"));
    }

    @Test
    public void checkSmcBirthmarkService() throws Exception{
        BirthmarkSpi service = environment.getService("smc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkCvfvBirthmarkService() throws Exception{
        BirthmarkSpi service = environment.getService("cvfv");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.cvfv.ConstantValueOfFieldVariableBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkIsBirthmarkService() throws Exception{
        BirthmarkSpi service = environment.getService("is");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.comparators.PlainBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkUcBirthmarkService() throws Exception{
        BirthmarkSpi service = environment.getService("uc");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.uc.UsedClassesBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }

    @Test
    public void checkKgramBirthmarkService() throws Exception{
        BirthmarkSpi service = environment.getService("kgram");

        Assert.assertNotNull(service.getComparator());
        Assert.assertNotNull(service.getExtractor());
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator",
            service.getComparator().getClass().getName()
        );
        Assert.assertEquals(
            "jp.sourceforge.stigmata.birthmarks.kgram.KGramBasedBirthmarkExtractor",
            service.getExtractor().getClass().getName()
        );
    }
}
