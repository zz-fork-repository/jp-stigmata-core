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
        Assert.assertEquals(birthmark.getElementCount(), 76);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        int index = 0;
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.FileInputStream#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#exists");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.System#getProperty");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#exists");
        Assert.assertEquals(elements[index++].getValue(), "java.io.FileInputStream#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Object#getClass");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Class#getResourceAsStream");
        Assert.assertEquals(elements[index++].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.File#toURI");
        Assert.assertEquals(elements[index++].getValue(), "java.net.URI#toURL");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#iterator");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.net.URL#openStream");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#size");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#toArray");
        Assert.assertEquals(elements[index++].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Object#getClass");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Class#getName");
        Assert.assertEquals(elements[index++].getValue(), "java.util.logging.Logger#getLogger");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#valueOf");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#toString");
        Assert.assertEquals(elements[index++].getValue(), "java.util.logging.Logger#warning");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#size");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#toArray");
        Assert.assertEquals(elements[index++].getValue(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Double#valueOf");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#add");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[index++].getValue(), "java.util.List#iterator");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[index++].getValue(), "org.apache.commons.beanutils.BeanUtils#describe");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Map#remove");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Map#remove");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Map#keySet");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Set#iterator");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.String#valueOf");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#append");
        Assert.assertEquals(elements[index++].getValue(), "java.lang.StringBuilder#toString");
        Assert.assertEquals(elements[index++].getValue(), "org.apache.commons.beanutils.BeanUtils#setProperty");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[index++].getValue(), "java.io.ByteArrayInputStream#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.ByteArrayOutputStream#<init>");
        Assert.assertEquals(elements[index++].getValue(), "java.io.ByteArrayOutputStream#write");
        Assert.assertEquals(elements[index++].getValue(), "java.io.InputStream#read");
        Assert.assertEquals(elements[index++].getValue(), "java.io.ByteArrayOutputStream#toByteArray");
        Assert.assertEquals(elements[index++].getValue(), "java.io.ByteArrayOutputStream#close");
        Assert.assertEquals(elements[index++].getValue(), "javax.imageio.spi.ServiceRegistry#lookupProviders");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#next");
        Assert.assertEquals(elements[index++].getValue(), "java.util.Iterator#hasNext");
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
        Assert.assertEquals(birthmark.getElementCount(), 10);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        Assert.assertEquals(elements[0].toString(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[1].toString(), "java.util.Arrays#asList");
        Assert.assertEquals(elements[2].toString(), "java.util.Arrays#asList");
        Assert.assertEquals(elements[3].toString(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[4].toString(), "java.util.Arrays#asList");
        Assert.assertEquals(elements[5].toString(), "java.util.Arrays#asList");
        Assert.assertEquals(elements[6].toString(), "java.util.List#size");
        Assert.assertEquals(elements[7].toString(), "java.util.List#size");
        Assert.assertEquals(elements[8].toString(), "java.util.List#size");
        Assert.assertEquals(elements[9].toString(), "java.util.List#size");
    }
}
