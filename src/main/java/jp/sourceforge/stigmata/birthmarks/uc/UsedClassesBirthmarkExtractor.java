package jp.sourceforge.stigmata.birthmarks.uc;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
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

    @Override
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
