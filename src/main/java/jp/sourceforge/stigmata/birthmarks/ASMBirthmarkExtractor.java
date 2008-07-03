package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractionFailedException;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Abstract birthmark extractor using ASM.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class ASMBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public ASMBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @SuppressWarnings("deprecation")
    public ASMBirthmarkExtractor(){
        super();
    }

    public abstract BirthmarkExtractVisitor
        createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkEnvironment environment);

    @Override
    public Birthmark extract(Birthmark birthmark, InputStream in,
                             BirthmarkEnvironment environment) throws BirthmarkExtractionFailedException{
        BirthmarkExtractionFailedException bee = new BirthmarkExtractionFailedException();

        try{
            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(false);
            BirthmarkExtractVisitor visitor = createExtractVisitor(writer, birthmark, environment);
            reader.accept(visitor, false);

            if(!visitor.isSuccess()){
                bee.addCauses(visitor.getCauses());
            }

            return visitor.getBirthmark();
        } catch(IOException e){
            bee.addCause(e);
            throw bee;
        }
    }
}