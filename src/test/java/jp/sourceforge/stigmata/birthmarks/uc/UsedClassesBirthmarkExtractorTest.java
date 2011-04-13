package jp.sourceforge.stigmata.birthmarks.uc;

import java.io.FileInputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.utils.WellknownClassJudgeRule;
import jp.sourceforge.stigmata.utils.WellknownClassJudgeRule.MatchPartType;
import jp.sourceforge.stigmata.utils.WellknownClassJudgeRule.MatchType;
import jp.sourceforge.stigmata.utils.WellknownClassManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Haruaki TAMADA
 */
public class UsedClassesBirthmarkExtractorTest{
    private BirthmarkExtractor extractor; 

    @Before
    public void setup(){
        extractor = new UsedClassesBirthmarkService().getExtractor();
        BirthmarkEnvironment env = BirthmarkEnvironment.getDefaultEnvironment();
        WellknownClassManager manager = env.getWellknownClassManager();
        manager.add(new WellknownClassJudgeRule("java.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
        manager.add(new WellknownClassJudgeRule("javax.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
    }

    @Test
    public void checkBirthmark() throws Exception{
        Birthmark birthmark = extractor.extract(new FileInputStream("target/test-classes/resources/HelloWorldFrame.class"));

        Assert.assertEquals("uc", birthmark.getType());
        Assert.assertEquals(7, birthmark.getElementCount());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals("java.awt.Component", elements[0].getValue());
        Assert.assertEquals("java.awt.Container", elements[1].getValue());
        Assert.assertEquals("java.awt.Font", elements[2].getValue());
        Assert.assertEquals("java.lang.Object", elements[3].getValue());
        Assert.assertEquals("java.lang.String", elements[4].getValue());
        Assert.assertEquals("javax.swing.JFrame", elements[5].getValue());
        Assert.assertEquals("javax.swing.JLabel", elements[6].getValue());
    }
}
