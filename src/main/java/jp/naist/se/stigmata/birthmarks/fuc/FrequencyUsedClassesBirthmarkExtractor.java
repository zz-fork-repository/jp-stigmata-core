package jp.naist.se.stigmata.birthmarks.fuc;

/*
 * $Id: UsedClassesBirthmarkExtractor.java 140 2007-06-28 10:48:47Z tama3 $
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
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
 * @version $Revision: 140 $ $Date: 2007-06-28 19:48:47 +0900 (Thu, 28 Jun 2007) $
 */
public class FrequencyUsedClassesBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyUsedClassesBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public FrequencyUsedClassesBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new UsedClassesBirthmarkExtractVisitor(writer, birthmark, context);
    }

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
