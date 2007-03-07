package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;

import jp.naist.se.stigmata.AbstractBirthmarkExtractor;
import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkExtractionException;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
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

    public abstract BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context);

    @Override
    public Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionException{
        BirthmarkExtractionException bee = new BirthmarkExtractionException();

        try{
            Birthmark birthmark = new PlainBirthmark(getProvider().getType());

            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(false);
            BirthmarkExtractVisitor visitor = createExtractVisitor(writer, birthmark, context);
            reader.accept(visitor, false);

            if(!visitor.isSuccess()){
                bee.addCauses(visitor.getCauses());
            }

            return birthmark;
        } catch(IOException e){
            bee.addCause(e);
            throw bee;
        }
    }
}