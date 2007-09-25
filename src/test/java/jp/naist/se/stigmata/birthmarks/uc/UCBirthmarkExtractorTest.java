package jp.naist.se.stigmata.birthmarks.uc;

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
public class UCBirthmarkExtractorTest{
    private Stigmata stigmata;
    private BirthmarkContext context;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
        context = stigmata.createContext();
        context.addBirthmarkType("uc");
    }

    @Test
    public void checkUCBirthmark() throws Exception{
        ExtractionResultSet ers = stigmata.createEngine().extract(
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", },
            context
        );
        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("uc"));

        Birthmark birthmark = array[0].getBirthmark("uc");
        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(11, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        int index = 0;
        Assert.assertEquals("java.io.File",                      elements[index++].getValue());
        Assert.assertEquals("java.io.FileInputStream",           elements[index++].getValue());
        Assert.assertEquals("java.io.InputStream",               elements[index++].getValue());
        Assert.assertEquals("java.lang.Class",                   elements[index++].getValue());
        Assert.assertEquals("java.lang.Object",                  elements[index++].getValue());
        Assert.assertEquals("java.lang.String",                  elements[index++].getValue());
        Assert.assertEquals("java.lang.System",                  elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList",               elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator",                elements[index++].getValue());
        Assert.assertEquals("java.util.List",                    elements[index++].getValue());
        Assert.assertEquals("javax.imageio.spi.ServiceRegistry", elements[index++].getValue());
    }

    @Test
    public void checkUCBirthmark2() throws Exception{
        ExtractionResultSet ers = stigmata.createEngine().extract(
            new String[] { "target/classes/jp/naist/se/stigmata/result/RoundRobinComparisonResultSet.class", },
            context
        );
        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("uc"));

        Birthmark birthmark = array[0].getBirthmark("uc");
        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(9, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        int index = 0;
        Assert.assertEquals("java.lang.Object",     elements[index++].getValue());
        Assert.assertEquals("java.lang.String",     elements[index++].getValue());
        Assert.assertEquals("java.net.URL",         elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList",  elements[index++].getValue());
        Assert.assertEquals("java.util.Collection", elements[index++].getValue());
        Assert.assertEquals("java.util.HashMap",    elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator",   elements[index++].getValue());
        Assert.assertEquals("java.util.List",       elements[index++].getValue());
        Assert.assertEquals("java.util.Map",        elements[index++].getValue());
    }
}
