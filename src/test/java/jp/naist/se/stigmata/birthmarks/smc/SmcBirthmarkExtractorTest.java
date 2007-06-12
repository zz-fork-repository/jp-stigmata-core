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

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("smc"));

        Birthmark birthmark = array[0].getBirthmark("smc");
        Assert.assertEquals("smc", birthmark.getType());
        Assert.assertEquals(77, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement",
                elements[i].getClass().getName()
            );
        }
        int index = 0;
        Assert.assertEquals("java.lang.Object#<init>",              elements[index++].getValue());
        Assert.assertEquals("java.io.FileInputStream#<init>",       elements[index++].getValue());
        Assert.assertEquals("java.io.File#<init>",                  elements[index++].getValue());
        Assert.assertEquals("java.io.File#exists",                  elements[index++].getValue());
        Assert.assertEquals("java.lang.System#getProperty",         elements[index++].getValue());
        Assert.assertEquals("java.io.File#<init>",                  elements[index++].getValue());
        Assert.assertEquals("java.io.File#exists",                  elements[index++].getValue());
        Assert.assertEquals("java.io.FileInputStream#<init>",       elements[index++].getValue());
        Assert.assertEquals("java.lang.Object#getClass",            elements[index++].getValue());
        Assert.assertEquals("java.lang.Class#getResourceAsStream",  elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList#<init>",           elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList#<init>",           elements[index++].getValue());
        Assert.assertEquals("java.lang.String#endsWith",            elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.lang.String#endsWith",            elements[index++].getValue());
        Assert.assertEquals("java.lang.String#endsWith",            elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.io.File#<init>",                  elements[index++].getValue());
        Assert.assertEquals("java.io.File#toURI",                   elements[index++].getValue());
        Assert.assertEquals("java.net.URI#toURL",                   elements[index++].getValue());
        Assert.assertEquals("java.lang.String#endsWith",            elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.util.List#iterator",              elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
        Assert.assertEquals("java.net.URL#openStream",              elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.util.List#size",                  elements[index++].getValue());
        Assert.assertEquals("java.util.List#toArray",               elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList#<init>",           elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.lang.Object#getClass",            elements[index++].getValue());
        Assert.assertEquals("java.lang.Class#getName",              elements[index++].getValue());
        Assert.assertEquals("java.util.logging.Logger#getLogger",   elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#<init>",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#toString",     elements[index++].getValue());
        Assert.assertEquals("java.util.logging.Logger#warning",     elements[index++].getValue());
        Assert.assertEquals("java.util.List#size",                  elements[index++].getValue());
        Assert.assertEquals("java.util.List#toArray",               elements[index++].getValue());
        Assert.assertEquals("java.util.ArrayList#<init>",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
        Assert.assertEquals("java.lang.Double#valueOf",             elements[index++].getValue());
        Assert.assertEquals("java.util.List#add",                   elements[index++].getValue());
        Assert.assertEquals("java.util.List#iterator",              elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
        Assert.assertEquals("java.lang.Double#doubleValue",         elements[index++].getValue());
        Assert.assertEquals("java.lang.Double#doubleValue",         elements[index++].getValue());
        Assert.assertEquals("org.apache.commons.beanutils.BeanUtils#describe",
                            elements[index++].getValue());
        Assert.assertEquals("java.util.Map#remove",                 elements[index++].getValue());
        Assert.assertEquals("java.util.Map#remove",                 elements[index++].getValue());
        Assert.assertEquals("java.util.Map#keySet",                 elements[index++].getValue());
        Assert.assertEquals("java.util.Set#iterator",               elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#<init>",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.String#valueOf",             elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#append",       elements[index++].getValue());
        Assert.assertEquals("java.lang.StringBuilder#toString",     elements[index++].getValue());
        Assert.assertEquals("org.apache.commons.beanutils.BeanUtils#setProperty",
                            elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayInputStream#<init>",  elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream#<init>", elements[index++].getValue());
        Assert.assertEquals("java.io.InputStream#read",             elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream#write",  elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream#toByteArray",
                            elements[index++].getValue());
        Assert.assertEquals("java.io.ByteArrayOutputStream#close",  elements[index++].getValue());
        Assert.assertEquals("javax.imageio.spi.ServiceRegistry#lookupProviders",
                            elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#hasNext",           elements[index++].getValue());
        Assert.assertEquals("java.util.Iterator#next",              elements[index++].getValue());
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
        Assert.assertEquals("smc", birthmark.getType());
        Assert.assertEquals(25, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement",
                elements[i].getClass().getName()
            );
        }
        // <init>(BirthmarkSet[], BirthmarkContext, boolean)
        Assert.assertEquals("java.lang.Object#<init>",      elements[ 0].toString());
        Assert.assertEquals("java.util.Arrays#asList",      elements[ 1].toString());
        Assert.assertEquals("java.util.Arrays#asList",      elements[ 2].toString());
        Assert.assertEquals("java.lang.Object#<init>",      elements[ 3].toString());

        // <init>(BirthmarkSet[], BirthmarkSet[], BirthmarkContext, boolean)
        Assert.assertEquals("java.util.Arrays#asList",      elements[ 4].toString());
        Assert.assertEquals("java.util.Arrays#asList",      elements[ 5].toString());

        // setCompareSamePair
        Assert.assertEquals("java.util.List#size",          elements[ 6].toString());
        Assert.assertEquals("java.util.List#size",          elements[ 7].toString());
        Assert.assertEquals("java.util.List#size",          elements[ 8].toString());
        Assert.assertEquals("java.util.List#size",          elements[ 9].toString());

        // getComparisonSources
        Assert.assertEquals("java.util.HashMap#<init>",     elements[10].toString());
        Assert.assertEquals("java.util.List#iterator",      elements[11].toString());
        Assert.assertEquals("java.util.Iterator#next",      elements[12].toString());
        Assert.assertEquals("java.util.Map#put",            elements[13].toString());
        Assert.assertEquals("java.util.Iterator#hasNext",   elements[14].toString());
        Assert.assertEquals("java.util.List#iterator",      elements[15].toString());
        Assert.assertEquals("java.util.Iterator#next",      elements[16].toString());
        Assert.assertEquals("java.util.Map#put",            elements[17].toString());
        Assert.assertEquals("java.util.Iterator#hasNext",   elements[18].toString());
        Assert.assertEquals("java.util.Map#size",           elements[19].toString());
        Assert.assertEquals("java.util.Map#entrySet",       elements[20].toString());
        Assert.assertEquals("java.util.Set#iterator",       elements[21].toString());
        Assert.assertEquals("java.util.Iterator#next",      elements[22].toString());
        Assert.assertEquals("java.util.Map$Entry#getValue", elements[23].toString());
        Assert.assertEquals("java.util.Iterator#hasNext",   elements[24].toString());
    }
}
