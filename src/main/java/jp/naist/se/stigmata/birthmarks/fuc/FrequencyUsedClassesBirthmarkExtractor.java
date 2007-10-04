package jp.naist.se.stigmata.birthmarks.fuc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.birthmarks.FrequencyBirthmark;
import jp.naist.se.stigmata.birthmarks.uc.UsedClassesBirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FrequencyUsedClassesBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyUsedClassesBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public FrequencyUsedClassesBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkEnvironment environment){
        return new UsedClassesBirthmarkExtractVisitor(writer, birthmark, environment);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE, ExtractionUnit.ARCHIVE, 
        };
    }

    @Override
    public Birthmark createBirthmark(){
        return new FrequencyBirthmark(getProvider().getType());
    }
}
