package jp.naist.se.stigmata.birthmarks.cvfv;

/*
 * $Id$
 */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.Stigmata;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CVFVBirthmarkExtractorTest{
    private Stigmata stigmata;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
    }

    @Test
    public void checkCVFVBirthmark() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "cvfv", },
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", }
        );

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals("cvfv", birthmark.getType());
        Assert.assertEquals(6, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[0].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[1].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[2].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[3].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[4].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[5].getClass().getName());

        Assert.assertEquals("Ljp/naist/se/stigmata/Stigmata;",
                            ((TypeAndValueBirthmarkElement)elements[0]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[0]).getValue());

        Assert.assertEquals("Ljp/naist/se/stigmata/BirthmarkEnvironment;",
                            ((TypeAndValueBirthmarkElement)elements[1]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[1]).getValue());

        Assert.assertEquals("Z",   ((TypeAndValueBirthmarkElement)elements[2]).getSignature());
        Assert.assertEquals(0,     ((TypeAndValueBirthmarkElement)elements[2]).getValue());

        Assert.assertEquals("Ljava/util/Stack;", ((TypeAndValueBirthmarkElement)elements[3]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[3]).getValue());

        Assert.assertEquals("Ljp/naist/se/stigmata/event/WarningMessages;",
                            ((TypeAndValueBirthmarkElement)elements[4]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[4]).getValue());

        Assert.assertEquals("Ljava/util/List;", ((TypeAndValueBirthmarkElement)elements[5]).getSignature());
        Assert.assertNull(((TypeAndValueBirthmarkElement)elements[5]).getValue());
    }

    @Test
    public void checkCVFVBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "cvfv", },
            new String[] { "target/classes/jp/naist/se/stigmata/RoundRobinComparisonResultSet.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals(birthmark.getType(), "cvfv");
        Assert.assertEquals(birthmark.getElementCount(), 6);

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[0].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[1].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[2].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[3].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[4].getClass().getName());
        Assert.assertEquals("jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement",
                            elements[5].getClass().getName());

        Assert.assertEquals("Ljava/util/List;",
                            ((TypeAndValueBirthmarkElement)elements[0]).getSignature());
        Assert.assertEquals(null, ((TypeAndValueBirthmarkElement)elements[0]).getValue());
        Assert.assertEquals("Ljava/util/List;",
                            ((TypeAndValueBirthmarkElement)elements[1]).getSignature());
        Assert.assertEquals(null, ((TypeAndValueBirthmarkElement)elements[1]).getValue());
        Assert.assertEquals("Ljp/naist/se/stigmata/BirthmarkEnvironment;",
                            ((TypeAndValueBirthmarkElement)elements[2]).getSignature());
        Assert.assertEquals(null, ((TypeAndValueBirthmarkElement)elements[2]).getValue());

        Assert.assertEquals("I", ((TypeAndValueBirthmarkElement)elements[3]).getSignature());
        Assert.assertEquals(1,   ((TypeAndValueBirthmarkElement)elements[3]).getValue());
        Assert.assertEquals("Z", ((TypeAndValueBirthmarkElement)elements[4]).getSignature());
        Assert.assertEquals(1,   ((TypeAndValueBirthmarkElement)elements[4]).getValue());
        Assert.assertEquals("Z", ((TypeAndValueBirthmarkElement)elements[5]).getSignature());
        Assert.assertEquals(0,   ((TypeAndValueBirthmarkElement)elements[5]).getValue());
    }
}
