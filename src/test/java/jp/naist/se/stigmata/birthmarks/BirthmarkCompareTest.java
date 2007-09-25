package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEngine;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.Stigmata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class BirthmarkCompareTest{
    private BirthmarkContext context;
    private BirthmarkSet[] birthmarks;
    private BirthmarkEngine engine;

    @Before
    public void readBirthmarks() throws Exception{
        context = Stigmata.getInstance().createContext();
        context.setBirthmarkTypes(new String[] { "cvfv", "smc", "uc", "is", "kgram", });
        engine = new BirthmarkEngine(context.getEnvironment());

        ExtractionResultSet ers = engine.extract(
            new String[] {
                "target/classes/jp/naist/se/stigmata/Stigmata.class",
                "target/classes/jp/naist/se/stigmata/Main.class",
                "target/classes/jp/naist/se/stigmata/BirthmarkEnvironment.class",
            }, context
        );
        birthmarks = ers.getBirthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    @Test
    public void equalsTest() throws Exception{
        Assert.assertEquals(3, birthmarks.length);
        BirthmarkEngine engine = Stigmata.getInstance().createEngine();

        Assert.assertEquals(1d, engine.compareDetails(birthmarks[0], birthmarks[0], context).calculateSimilarity(), 1E-6);
        Assert.assertEquals(1d, engine.compareDetails(birthmarks[1], birthmarks[1], context).calculateSimilarity(), 1E-6);
        Assert.assertEquals(1d, engine.compareDetails(birthmarks[2], birthmarks[2], context).calculateSimilarity(), 1E-6);
    }
}
