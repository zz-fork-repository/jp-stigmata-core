package jp.naist.se.stigmata.birthmarks.cvfv;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.Stigmata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CVFVBirthmarkExtractorTest{
    private Stigmata stigmata;
    private BirthmarkContext context;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
        context = stigmata.createContext();
        context.addExtractionType("cvfv");
    }

    @Test
    public void checkCVFVBirthmark() throws Exception{
        ExtractionResultSet ers = stigmata.createEngine().extract(
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", },
            context
        );
        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals("cvfv", birthmark.getType());
        Assert.assertEquals(3, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[0].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[1].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[2].getClass().getName());

        Assert.assertEquals("Ljp/naist/se/stigmata/Stigmata;",
                            ((TypeAndValueBirthmarkElement)elements[0]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[0]).getValue());

        Assert.assertEquals("Ljp/naist/se/stigmata/BirthmarkEnvironment;",
                            ((TypeAndValueBirthmarkElement)elements[1]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[1]).getValue());

        Assert.assertEquals("Ljava/util/List;",
                            ((TypeAndValueBirthmarkElement)elements[2]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[2]).getValue());
    }

    @Test
    public void checkCVFVBirthmark2() throws Exception{
        ExtractionResultSet ers = stigmata.createEngine().extract(
            new String[] { "target/classes/jp/naist/se/stigmata/result/RoundRobinComparisonResultSet.class", },
            context
        );

        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals(birthmark.getType(), "cvfv");
        Assert.assertEquals(3, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[0].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[1].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[2].getClass().getName());

        Assert.assertEquals("I",  ((TypeAndValueBirthmarkElement)elements[0]).getSignature());
        Assert.assertEquals(-1,   ((TypeAndValueBirthmarkElement)elements[0]).getValue());
        Assert.assertEquals("Z",  ((TypeAndValueBirthmarkElement)elements[1]).getSignature());
        Assert.assertEquals(null, ((TypeAndValueBirthmarkElement)elements[1]).getValue());
        Assert.assertEquals("Z",  ((TypeAndValueBirthmarkElement)elements[2]).getSignature());
        Assert.assertEquals(0,    ((TypeAndValueBirthmarkElement)elements[2]).getValue());
    }
}
