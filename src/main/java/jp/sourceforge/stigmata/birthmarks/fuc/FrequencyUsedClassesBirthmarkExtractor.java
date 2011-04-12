package jp.sourceforge.stigmata.birthmarks.fuc;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.birthmarks.FrequencyBirthmark;
import jp.sourceforge.stigmata.birthmarks.FrequencyBirthmarkElement;
import jp.sourceforge.stigmata.birthmarks.uc.UsedClassesBirthmarkExtractVisitor;
import jp.sourceforge.stigmata.spi.BirthmarkService;

import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
 */
public class FrequencyUsedClassesBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyUsedClassesBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public FrequencyUsedClassesBirthmarkExtractor(){
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
        return new FrequencyBirthmark(getProvider().getType());
    }

    public BirthmarkElement buildElement(String value){
        return new FrequencyBirthmarkElement(value);
    }
}
