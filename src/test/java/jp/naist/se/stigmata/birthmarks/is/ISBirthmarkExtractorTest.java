package jp.naist.se.stigmata.birthmarks.is;

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

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("is"));

        Birthmark birthmark = array[0].getBirthmark("is");
        Assert.assertEquals(birthmark.getType(), "is");
        Assert.assertEquals(birthmark.getElementCount(), 2);

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertNull(elements[0].getValue());
        Assert.assertEquals(elements[1].getValue(), "java.lang.Object");
    }

    @Test
    public void checkISBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "is", },
            new String[] { "target/classes/jp/naist/se/stigmata/ConfigFileParser.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("is"));

        Birthmark birthmark = array[0].getBirthmark("is");
        Assert.assertEquals(birthmark.getType(), "is");
        Assert.assertEquals(birthmark.getElementCount(), 3);

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertNull(elements[0].getValue());
        Assert.assertEquals(elements[1].getValue(), "org.xml.sax.helpers.DefaultHandler");
        Assert.assertEquals(elements[2].getValue(), "java.lang.Object");
    }
}
