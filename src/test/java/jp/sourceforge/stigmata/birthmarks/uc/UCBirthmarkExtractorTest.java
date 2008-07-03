package jp.sourceforge.stigmata.birthmarks.uc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.Stigmata;

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
            new String[] { "target/classes/jp/sourceforge/stigmata/Stigmata.class", },
            context
        );
        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("uc"));

        Birthmark birthmark = array[0].getBirthmark("uc");
        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(18, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        int index = 0;
        Assert.assertEquals("java.io.File",                      elements[index++].getValue());
        Assert.assertEquals("java.io.FileFilter",                elements[index++].getValue());
        Assert.assertEquals("java.io.FileInputStream",           elements[index++].getValue());
        Assert.assertEquals("java.io.FileWriter",                elements[index++].getValue());
        Assert.assertEquals("java.io.InputStream",               elements[index++].getValue());
        Assert.assertEquals("java.io.PrintWriter",               elements[index++].getValue());
        Assert.assertEquals("java.io.Writer",                    elements[index++].getValue());
        Assert.assertEquals("java.lang.Class",                   elements[index++].getValue());
        Assert.assertEquals("java.lang.ClassLoader",             elements[index++].getValue());
        Assert.assertEquals("java.lang.Object",                  elements[index++].getValue());
        Assert.assertEquals("java.lang.String",                  elements[index++].getValue());
        Assert.assertEquals("java.lang.System",                  elements[index++].getValue());
        Assert.assertEquals("java.net.URI",                      elements[index++].getValue());
        Assert.assertEquals("java.net.URL",                      elements[index++].getValue());
        Assert.assertEquals("java.net.URLClassLoader",           elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList",               elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator",                elements[index++].getValue());
        Assert.assertEquals("java.util.List",                    elements[index++].getValue());
    }

    @Test
    public void checkUCBirthmark2() throws Exception{
        ExtractionResultSet ers = stigmata.createEngine().extract(
            new String[] { "target/classes/jp/sourceforge/stigmata/result/RoundRobinComparisonResultSet.class", },
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
