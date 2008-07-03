package jp.sourceforge.stigmata.birthmarks.uc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

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
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkEnvironment environment){
        return new UsedClassesBirthmarkExtractVisitor(writer, birthmark, environment);
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
