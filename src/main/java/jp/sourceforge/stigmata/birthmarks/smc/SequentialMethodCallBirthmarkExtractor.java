package jp.sourceforge.stigmata.birthmarks.smc;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.spi.BirthmarkService;

import org.objectweb.asm.ClassWriter;

/**
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public SequentialMethodCallBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public SequentialMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, context);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, ExtractionUnit.ARCHIVE, ExtractionUnit.PACKAGE, };
    }

    @Override
    public BirthmarkElement buildElement(String value) {
        String className = value.substring(0, value.indexOf('#'));
        String methodName = value.substring(value.indexOf('#') + 1, value.lastIndexOf('!'));
        String signature = value.substring(value.lastIndexOf('!') + 1);

        return new MethodCallBirthmarkElement(className, methodName, signature);
    }
}
