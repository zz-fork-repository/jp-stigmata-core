package jp.naist.se.stigmata.birthmarks.smc;

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
public class SmcBirthmarkExtractorTest{
    private Stigmata stigmata;

    @Before
    public void setup(){
        stigmata = Stigmata.getInstance();
    }

    @Test
    public void checkSmcBirthmark() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "smc", },
            new String[] { "target/classes/jp/naist/se/stigmata/Stigmata.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("smc"));

        Birthmark birthmark = array[0].getBirthmark("smc");
        Assert.assertEquals(birthmark.getType(), "smc");
        Assert.assertEquals(birthmark.getElementCount(), 62);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        Assert.assertEquals(elements[ 0].getValue(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[ 1].getValue(), "java.io.FileInputStream#<init>");
        Assert.assertEquals(elements[ 2].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[ 3].getValue(), "java.io.File#exists");
        Assert.assertEquals(elements[ 4].getValue(), "java.lang.System#getProperty");
        Assert.assertEquals(elements[ 5].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[ 6].getValue(), "java.io.File#exists");
        Assert.assertEquals(elements[ 7].getValue(), "java.io.FileInputStream#<init>");
        Assert.assertEquals(elements[ 8].getValue(), "java.lang.Object#getClass");
        Assert.assertEquals(elements[ 9].getValue(), "java.lang.Class#getResourceAsStream");
        Assert.assertEquals(elements[10].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[11].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[12].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[13].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[14].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[15].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[16].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[17].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[18].getValue(), "java.io.File#toURI");
        Assert.assertEquals(elements[19].getValue(), "java.net.URI#toURL");
        Assert.assertEquals(elements[20].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[21].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[22].getValue(), "java.util.List#iterator");
        Assert.assertEquals(elements[23].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[24].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[25].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[26].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[27].getValue(), "java.net.URL#openStream");
        Assert.assertEquals(elements[28].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[29].getValue(), "java.util.List#size");
        Assert.assertEquals(elements[30].getValue(), "java.util.List#toArray");
        Assert.assertEquals(elements[31].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[32].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[33].getValue(), "java.lang.Object#getClass");
        Assert.assertEquals(elements[34].getValue(), "java.lang.Class#getName");
        Assert.assertEquals(elements[35].getValue(), "java.util.logging.Logger#getLogger");
        Assert.assertEquals(elements[36].getValue(), "java.lang.StringBuilder#<init>");
        Assert.assertEquals(elements[37].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[38].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[39].getValue(), "java.lang.StringBuilder#toString");
        Assert.assertEquals(elements[40].getValue(), "java.util.logging.Logger#warning");
        Assert.assertEquals(elements[41].getValue(), "java.util.List#size");
        Assert.assertEquals(elements[42].getValue(), "java.util.List#toArray");
        Assert.assertEquals(elements[43].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[44].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[45].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[46].getValue(), "java.lang.Double#valueOf");
        Assert.assertEquals(elements[47].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[48].getValue(), "java.util.List#iterator");
        Assert.assertEquals(elements[49].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[50].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[51].getValue(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[52].getValue(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[53].getValue(), "java.io.ByteArrayInputStream#<init>");
        Assert.assertEquals(elements[54].getValue(), "java.io.ByteArrayOutputStream#<init>");
        Assert.assertEquals(elements[55].getValue(), "java.io.InputStream#read");
        Assert.assertEquals(elements[56].getValue(), "java.io.ByteArrayOutputStream#write");
        Assert.assertEquals(elements[57].getValue(), "java.io.ByteArrayOutputStream#toByteArray");
        Assert.assertEquals(elements[58].getValue(), "java.io.ByteArrayOutputStream#close");
        Assert.assertEquals(elements[59].getValue(), "javax.imageio.spi.ServiceRegistry#lookupProviders");
        Assert.assertEquals(elements[60].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[61].getValue(), "java.util.Iterator#next");
    }

    @Test
    public void checkSmcBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "smc", },
            new String[] { "target/classes/jp/naist/se/stigmata/RoundRobinComparisonResultSet.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("smc"));

        Birthmark birthmark = array[0].getBirthmark("smc");
        Assert.assertEquals(birthmark.getType(), "smc");
        Assert.assertEquals(birthmark.getElementCount(), 2);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        Assert.assertEquals(elements[0].toString(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[1].toString(), "java.lang.Object#<init>");
    }
}
