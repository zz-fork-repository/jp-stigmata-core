package jp.naist.se.stigmata.birthmarks.uc;

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
public class UCBirthmarkExtractorTest{
    private Stigmata stigmata;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
    }

    @Test
    public void checkUCBirthmark() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "uc", },
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", }
        );

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("uc"));

        Birthmark birthmark = array[0].getBirthmark("uc");
        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(21, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("java.io.ByteArrayInputStream",           elements[ 0].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream",          elements[ 1].getValue());
        Assert.assertEquals("java.io.File",                           elements[ 2].getValue());
        Assert.assertEquals("java.io.FileInputStream",                elements[ 3].getValue());
        Assert.assertEquals("java.io.InputStream",                    elements[ 4].getValue());
        Assert.assertEquals("java.lang.Class",                        elements[ 5].getValue());
        Assert.assertEquals("java.lang.Double",                       elements[ 6].getValue());
        Assert.assertEquals("java.lang.Object",                       elements[ 7].getValue());
        Assert.assertEquals("java.lang.String",                       elements[ 8].getValue());
        Assert.assertEquals("java.lang.StringBuilder",                elements[ 9].getValue());
        Assert.assertEquals("java.lang.System",                       elements[10].getValue());
        Assert.assertEquals("java.net.URI",                           elements[11].getValue());
        Assert.assertEquals("java.net.URL",                           elements[12].getValue());
        Assert.assertEquals("java.util.ArrayList",                    elements[13].getValue());
        Assert.assertEquals("java.util.Iterator",                     elements[14].getValue());
        Assert.assertEquals("java.util.List",                         elements[15].getValue());
        Assert.assertEquals("java.util.Map",                          elements[16].getValue());
        Assert.assertEquals("java.util.Set",                          elements[17].getValue());
        Assert.assertEquals("java.util.logging.Logger",               elements[18].getValue());
        Assert.assertEquals("javax.imageio.spi.ServiceRegistry",      elements[19].getValue());
        Assert.assertEquals("org.apache.commons.beanutils.BeanUtils", elements[20].getValue());
    }

    @Test
    public void checkUCBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "uc", },
            new String[] { "target/classes/jp/naist/se/stigmata/RoundRobinComparisonResultSet.class", }
        );

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("uc"));

        Birthmark birthmark = array[0].getBirthmark("uc");
        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(9, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("java.lang.Object",    elements[0].getValue());
        Assert.assertEquals("java.net.URL",        elements[1].getValue());
        Assert.assertEquals("java.util.Arrays",    elements[2].getValue());
        Assert.assertEquals("java.util.HashMap",   elements[3].getValue());
        Assert.assertEquals("java.util.Iterator",  elements[4].getValue());
        Assert.assertEquals("java.util.List",      elements[5].getValue());
        Assert.assertEquals("java.util.Map",       elements[6].getValue());
        Assert.assertEquals("java.util.Map$Entry", elements[7].getValue());
        Assert.assertEquals("java.util.Set",       elements[8].getValue());
    }
}
