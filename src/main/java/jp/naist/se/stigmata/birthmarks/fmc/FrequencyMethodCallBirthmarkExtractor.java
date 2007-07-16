package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.birthmarks.FrequencyBirthmark;
import jp.naist.se.stigmata.birthmarks.FrequencyBirthmarkElement;
import jp.naist.se.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FrequencyMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyMethodCallBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public FrequencyMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkEnvironment environment){
        return new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, environment){
            @Override
            protected void addElement(String className, String methodName, String description){
                addElement(new FrequencyBirthmarkElement(className + "#" + methodName + description));
            }            
        };
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
