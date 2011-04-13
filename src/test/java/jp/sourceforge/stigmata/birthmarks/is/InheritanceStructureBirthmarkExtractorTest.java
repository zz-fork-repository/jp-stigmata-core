package jp.sourceforge.stigmata.birthmarks.is;

import java.io.FileInputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.NullBirthmarkElement;
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
public class InheritanceStructureBirthmarkExtractorTest{
    private BirthmarkExtractor extractor; 

    @Before
    public void setup(){
        extractor = new InheritanceStructureBirthmarkService().getExtractor();
        BirthmarkEnvironment env = BirthmarkEnvironment.getDefaultEnvironment();
        WellknownClassManager manager = env.getWellknownClassManager();
        manager.add(new WellknownClassJudgeRule("java.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
        manager.add(new WellknownClassJudgeRule("javax.", MatchType.PREFIX, MatchPartType.FULLY_NAME));
    }

    @Test
    public void checkBirthmark() throws Exception{
        Birthmark birthmark = extractor.extract(new FileInputStream("target/test-classes/resources/HelloWorldFrame.class"));
        Assert.assertEquals("is", birthmark.getType());

        BirthmarkElement[] elements = birthmark.getElements();
        Assert.assertEquals(2, elements.length);

        Assert.assertTrue(elements[0] instanceof NullBirthmarkElement);
        Assert.assertEquals("java.lang.Object", elements[1].getValue());
    }
}
