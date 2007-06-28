package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id: SequentialMethodCallBirthmarkExtractor.java 130 2007-06-13 10:08:01Z tama3 $
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * @author Haruaki TAMADA
 * @version $Revision: 130 $ $Date: 2007-06-13 19:08:01 +0900 (Wed, 13 Jun 2007) $
 */
public class FrequencyMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyMethodCallBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public FrequencyMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new FrequencyMethodCallBirthmarkExtractVisitor(writer, birthmark, context);
    }

    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE, ExtractionUnit.ARCHIVE, 
        };
    }

    @Override
    public Birthmark createBirthmark(){
        return new FrequencyMethodCallBirthmark(getProvider().getType());
    }
}
