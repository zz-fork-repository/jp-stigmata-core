package jp.sourceforge.stigmata.birthmarks.is;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkEngine;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.Stigmata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class ISBirthmarkExtractorTest{
    private BirthmarkEngine engine;
    private BirthmarkContext context;

    @Before
    public void setup(){
        engine = Stigmata.getInstance().createEngine();
        context = Stigmata.getInstance().createContext();
        context.addBirthmarkType("is");
    }

    @Test
    public void checkISBirthmark() throws Exception{
        ExtractionResultSet ers = engine.extract(
            new String[] { "target/classes/jp/sourceforge/stigmata/Stigmata.class", },
            context
        );

        BirthmarkSet[] array = ers.getBirthmarkSets();

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
        ExtractionResultSet ers = engine.extract(
            new String[] { "target/classes/jp/sourceforge/stigmata/result/RoundRobinComparisonResultSet.class", },
            context
        );
        BirthmarkSet[] array = ers.getBirthmarkSets();

        Assert.assertEquals(1, array.length);
        Assert.assertNotNull(array[0].getBirthmark("is"));

        Birthmark birthmark = array[0].getBirthmark("is");
        Assert.assertEquals("is", birthmark.getType());
        Assert.assertEquals(3, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertNull(elements[0].getValue());
        Assert.assertNull(elements[1].getValue());
        Assert.assertEquals("java.lang.Object", elements[2].getValue());
    }
}
