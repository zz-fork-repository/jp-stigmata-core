package jp.naist.se.stigmata.birthmarks.is;

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
public class ISBirthmarkExtractorTest{
    private Stigmata stigmata;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
    }

    @Test
    public void checkISBirthmark() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "is", },
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", }
        );

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("is"));

        Birthmark birthmark = array[0].getBirthmark("is");
        Assert.assertEquals("is", birthmark.getType());
        Assert.assertEquals(2, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertNull(elements[0].getValue());
        Assert.assertEquals("java.lang.Object", elements[1].getValue());
    }

    @Test
    public void checkISBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "is", },
            new String[] { "target/classes/jp/naist/se/stigmata/RoundRobinComparisonResultSet.class", }
        );

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("is"));

        Birthmark birthmark = array[0].getBirthmark("is");
        Assert.assertEquals("is", birthmark.getType());
        Assert.assertEquals(2, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertNull(elements[0].getValue());
        Assert.assertEquals("java.lang.Object", elements[1].getValue());
    }
}
