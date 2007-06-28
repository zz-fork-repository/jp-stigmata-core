package jp.naist.se.stigmata.birthmarks.uc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class UsedClassesBirthmarkExtractor extends ASMBirthmarkExtractor{
    public UsedClassesBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public UsedClassesBirthmarkExtractor(){
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
        return new UsedClassesBirthmark(getProvider().getType());
    }
}
