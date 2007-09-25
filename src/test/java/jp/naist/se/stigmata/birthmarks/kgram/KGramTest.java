package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class KGramTest{
    private String[] plainValues;
    private String[] complexValues;

    @Before
    public void buildKGrams(){
        plainValues = new String[] { "a", "b", "c", "d", "e", "f", "g", };
        complexValues = new String[] { "a", "b", "r", "a", "c", "a", "d", "a", "b", "r", "a", };
    }

    @Test
    public void checkPlainKGram(){
        KGram<String>[] kgrams = KGram.buildKGram(plainValues, 4);
        assertEquals(4, kgrams.length);

        assertEquals(4, kgrams[0].getKValue());
        assertEquals(4, kgrams[1].getKValue());
        assertEquals(4, kgrams[2].getKValue());
        assertEquals(4, kgrams[3].getKValue());

        assertEquals(new String[] { "a", "b", "c", "d", }, kgrams[0].toArray());
        assertEquals(new String[] { "b", "c", "d", "e", }, kgrams[1].toArray());
        assertEquals(new String[] { "c", "d", "e", "f", }, kgrams[2].toArray());
        assertEquals(new String[] { "d", "e", "f", "g", }, kgrams[3].toArray());
    }

    @Test
    public void checkPlainKGram2(){
        KGram<String>[] kgrams = KGram.buildKGram(plainValues, 3);
        assertEquals(5, kgrams.length);

        assertEquals(3, kgrams[0].getKValue());
        assertEquals(3, kgrams[1].getKValue());
        assertEquals(3, kgrams[2].getKValue());
        assertEquals(3, kgrams[3].getKValue());
        assertEquals(3, kgrams[4].getKValue());

        assertEquals(new String[] { "a", "b", "c", }, kgrams[0].toArray());
        assertEquals(new String[] { "b", "c", "d", }, kgrams[1].toArray());
        assertEquals(new String[] { "c", "d", "e", }, kgrams[2].toArray());
        assertEquals(new String[] { "d", "e", "f", }, kgrams[3].toArray());
        assertEquals(new String[] { "e", "f", "g", }, kgrams[4].toArray());
    }

    @Test
    public void checkComplexKGram(){
        KGram<String>[] kgrams = KGram.buildKGram(complexValues, 3);

        assertEquals(7, kgrams.length);

        assertEquals(new String[] { "a", "b", "r", }, kgrams[0].toArray());
        assertEquals(new String[] { "b", "r", "a", }, kgrams[1].toArray());
        assertEquals(new String[] { "r", "a", "c", }, kgrams[2].toArray());
        assertEquals(new String[] { "a", "c", "a", }, kgrams[3].toArray());
        assertEquals(new String[] { "c", "a", "d", }, kgrams[4].toArray());
        assertEquals(new String[] { "a", "d", "a", }, kgrams[5].toArray());
        assertEquals(new String[] { "d", "a", "b", }, kgrams[6].toArray());
//      assertEquals(new String[] { "a", "b", "r", }, kgrams[0].toArray());
//      assertEquals(new String[] { "b", "r", "a", }, kgrams[0].toArray());
    }
}
