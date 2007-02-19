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
 * @version$Revision$ $Date$
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

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals(birthmark.getType(), "cvfv");
        Assert.assertEquals(birthmark.getElementCount(), 3);

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals(elements[0].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[1].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[2].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");

        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[0]).getSignature(),
                            "Ljp/naist/se/stigmata/Stigmata;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[0]).getValue(), null);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[1]).getSignature(),
                            "Ljp/naist/se/stigmata/BirthmarkContext;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[1]).getValue(), null);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[2]).getSignature(), "Z");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[2]).getValue(), 0);
    }

    @Test
    public void checkCVFVBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "cvfv", },
            new String[] { "target/classes/jp/naist/se/stigmata/ConfigFileParser.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("cvfv"));

        Birthmark birthmark = array[0].getBirthmark("cvfv");
        Assert.assertEquals(birthmark.getType(), "cvfv");
        Assert.assertEquals(birthmark.getElementCount(), 9);

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals(elements[0].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[1].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[2].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[3].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[4].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[5].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[6].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[7].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");
        Assert.assertEquals(elements[8].getClass().getName(),
                            "jp.naist.se.stigmata.birthmarks.cvfv.TypeAndValueBirthmarkElement");

        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[0]).getSignature(),
                            "Ljp/naist/se/stigmata/BirthmarkContext;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[0]).getValue(), null);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[1]).getSignature(),
                            "Ljp/naist/se/stigmata/utils/WellknownClassManager;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[1]).getValue(), null);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[2]).getSignature(), "Z");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[2]).getValue(), 0);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[3]).getSignature(), "Z");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[3]).getValue(), 0);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[4]).getSignature(), "Z");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[4]).getValue(), 0);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[5]).getSignature(), "I");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[5]).getValue(), 0);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[6]).getSignature(), "I");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[6]).getValue(), 0);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[7]).getSignature(),
                            "Ljava/lang/String;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[7]).getValue(), null);
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[8]).getSignature(),
                            "Ljava/lang/String;");
        Assert.assertEquals(((TypeAndValueBirthmarkElement)elements[8]).getValue(), null);
    }
}
