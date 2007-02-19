package jp.naist.se.stigmata.birthmarks.smc;

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
        Assert.assertEquals(birthmark.getElementCount(), 49);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        Assert.assertEquals(elements[ 0].toString(), "java.lang.Object#<init>");
        Assert.assertEquals(elements[ 1].toString(), "java.io.File#<init>");
        Assert.assertEquals(elements[ 2].toString(), "java.io.File#exists");
        Assert.assertEquals(elements[ 3].toString(), "java.lang.System#getProperty");
        Assert.assertEquals(elements[ 4].toString(), "java.io.File#<init>");
        Assert.assertEquals(elements[ 5].toString(), "java.io.File#exists");
        Assert.assertEquals(elements[ 6].toString(), "java.io.FileInputStream#<init>");
        Assert.assertEquals(elements[ 7].toString(), "java.lang.Object#getClass");
        Assert.assertEquals(elements[ 8].toString(), "java.lang.Class#getResourceAsStream");
        Assert.assertEquals(elements[ 9].toString(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[10].toString(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[11].toString(), "java.util.List#add");
        Assert.assertEquals(elements[12].toString(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[13].toString(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[14].toString(), "java.util.List#add");
        Assert.assertEquals(elements[15].toString(), "java.io.File#<init>");
        Assert.assertEquals(elements[16].toString(), "java.io.File#toURI");
        Assert.assertEquals(elements[17].toString(), "java.net.URI#toURL");
        Assert.assertEquals(elements[18].toString(), "java.lang.String#endsWith");
        Assert.assertEquals(elements[19].toString(), "java.util.List#add");
        Assert.assertEquals(elements[20].toString(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[21].toString(), "java.util.List#iterator");
        Assert.assertEquals(elements[22].toString(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[23].toString(), "java.util.Iterator#next");
        Assert.assertEquals(elements[24].toString(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[25].toString(), "java.util.Iterator#next");
        Assert.assertEquals(elements[26].toString(), "java.net.URL#openStream");
        Assert.assertEquals(elements[27].toString(), "java.util.List#add");
        Assert.assertEquals(elements[28].toString(), "java.util.List#size");
        Assert.assertEquals(elements[29].toString(), "java.util.List#toArray");
        Assert.assertEquals(elements[30].toString(), "java.util.ArrayList#<init>");
        Assert.assertEquals(elements[31].toString(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[32].toString(), "java.util.Iterator#next");
        Assert.assertEquals(elements[33].toString(), "java.lang.Double#valueOf");
        Assert.assertEquals(elements[34].toString(), "java.util.List#add");
        Assert.assertEquals(elements[35].toString(), "java.util.List#iterator");
        Assert.assertEquals(elements[36].toString(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[37].toString(), "java.util.Iterator#next");
        Assert.assertEquals(elements[38].toString(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[39].toString(), "java.lang.Double#doubleValue");
        Assert.assertEquals(elements[40].toString(), "java.io.ByteArrayInputStream#<init>");
        Assert.assertEquals(elements[41].toString(), "java.io.ByteArrayOutputStream#<init>");
        Assert.assertEquals(elements[42].toString(), "java.io.InputStream#read");
        Assert.assertEquals(elements[43].toString(), "java.io.ByteArrayOutputStream#write");
        Assert.assertEquals(elements[44].toString(), "java.io.ByteArrayOutputStream#toByteArray");
        Assert.assertEquals(elements[45].toString(), "java.io.ByteArrayOutputStream#close");
        Assert.assertEquals(elements[46].toString(), "javax.imageio.spi.ServiceRegistry#lookupProviders");
        Assert.assertEquals(elements[47].toString(), "java.util.Iterator#hasNext");
        Assert.assertEquals(elements[48].toString(), "java.util.Iterator#next");
    }

    @Test
    public void checkSmcBirthmark2() throws Exception{
        BirthmarkSet[] array = stigmata.extract(
            new String[] { "smc", },
            new String[] { "target/classes/jp/naist/se/stigmata/ConfigFileParser.class", }
        );

        Assert.assertEquals(array.length, 1);
        Assert.assertNotNull(array[0].getBirthmark("smc"));

        Birthmark birthmark = array[0].getBirthmark("smc");
        Assert.assertEquals(birthmark.getType(), "smc");
        Assert.assertEquals(birthmark.getElementCount(), 29);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            Assert.assertEquals(
                elements[0].getClass().getName(),
                "jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement"
            );
        }
        Assert.assertEquals(elements[ 0].toString(), "org.xml.sax.helpers.DefaultHandler#<init>");
        Assert.assertEquals(elements[ 1].toString(), "javax.xml.parsers.SAXParserFactory#newInstance");
        Assert.assertEquals(elements[ 2].toString(), "javax.xml.parsers.SAXParserFactory#newSAXParser");
        Assert.assertEquals(elements[ 3].toString(), "javax.xml.parsers.SAXParser#parse");
        Assert.assertEquals(elements[ 4].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[ 5].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[ 6].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[ 7].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[ 8].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[ 9].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[10].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[11].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[12].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[13].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[14].toString(), "java.lang.String#<init>");
        Assert.assertEquals(elements[15].toString(), "java.lang.String#trim");
        Assert.assertEquals(elements[16].toString(), "java.lang.String#length");
        Assert.assertEquals(elements[17].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[18].toString(), "java.lang.String#<init>");
        Assert.assertEquals(elements[19].toString(), "java.lang.String#trim");
        Assert.assertEquals(elements[20].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[21].toString(), "java.lang.String#<init>");
        Assert.assertEquals(elements[22].toString(), "java.lang.String#trim");
        Assert.assertEquals(elements[23].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[24].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[25].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[26].toString(), "java.lang.String#<init>");
        Assert.assertEquals(elements[27].toString(), "java.lang.String#equals");
        Assert.assertEquals(elements[28].toString(), "java.net.URL#<init>");
    }
}
