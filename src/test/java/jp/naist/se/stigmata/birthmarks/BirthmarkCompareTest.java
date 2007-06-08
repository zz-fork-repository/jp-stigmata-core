package jp.naist.se.stigmata.birthmarks;

import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.Stigmata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BirthmarkCompareTest{
    private Stigmata stigmata;
    private BirthmarkSet[] birthmarks;

    @Before
    public void readBirthmarks() throws Exception{
        stigmata = Stigmata.getInstance();
        birthmarks = stigmata.extract(
            new String[] { "cvfv", "smc", "uc", "is", "kgram", },
            new String[] {
                "target/classes/jp/naist/se/stigmata/Stigmata.class",
                "target/classes/jp/naist/se/stigmata/Main.class",
                "target/classes/jp/naist/se/stigmata/BirthmarkContext.class",
            }
        );
    }

    @Test
    public void equalsTest() throws Exception{
        Assert.assertEquals(3, birthmarks.length);

        Assert.assertEquals(1d, stigmata.compare(birthmarks[0], birthmarks[0]), 1E-6);
        Assert.assertEquals(1d, stigmata.compare(birthmarks[1], birthmarks[1]), 1E-6);
        Assert.assertEquals(1d, stigmata.compare(birthmarks[2], birthmarks[2]), 1E-6);
    }
}