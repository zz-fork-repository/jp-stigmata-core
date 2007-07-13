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
        Assert.assertEquals(24, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        int index = 0;
        Assert.assertEquals("java.io.ByteArrayInputStream",                elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream",               elements[index++].getValue());
        Assert.assertEquals("java.io.File",                                elements[index++].getValue());
        Assert.assertEquals("java.io.FileInputStream",                     elements[index++].getValue());
        Assert.assertEquals("java.io.InputStream",                         elements[index++].getValue());
        Assert.assertEquals("java.lang.Class",                             elements[index++].getValue());
        Assert.assertEquals("java.lang.Double",                            elements[index++].getValue());
        Assert.assertEquals("java.lang.Object",                            elements[index++].getValue());
        Assert.assertEquals("java.lang.String",                            elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder",                     elements[index++].getValue());
        Assert.assertEquals("java.lang.System",                            elements[index++].getValue());
        Assert.assertEquals("java.net.URI",                                elements[index++].getValue());
        Assert.assertEquals("java.net.URL",                                elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList",                         elements[index++].getValue());
        Assert.assertEquals("java.util.Collection",                        elements[index++].getValue());
        Assert.assertEquals("java.util.HashMap",                           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator",                          elements[index++].getValue());
        Assert.assertEquals("java.util.List",                              elements[index++].getValue());
        Assert.assertEquals("java.util.Map",                               elements[index++].getValue());
        Assert.assertEquals("java.util.Set",                               elements[index++].getValue());
        Assert.assertEquals("java.util.Stack",                             elements[index++].getValue());
        Assert.assertEquals("java.util.logging.Logger",                    elements[index++].getValue());
        Assert.assertEquals("javax.imageio.spi.ServiceRegistry",           elements[index++].getValue());
        Assert.assertEquals("org.apache.commons.beanutils.BeanUtils",      elements[index++].getValue());
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
