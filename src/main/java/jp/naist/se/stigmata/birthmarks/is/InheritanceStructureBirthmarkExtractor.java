package jp.naist.se.stigmata.birthmarks.is;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;

import jp.naist.se.stigmata.AbstractBirthmarkExtractor;
import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElementClassNotFoundException;
import jp.naist.se.stigmata.BirthmarkExtractionException;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.birthmarks.PlainBirthmark;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class InheritanceStructureBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public InheritanceStructureBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @SuppressWarnings("deprecation")
    public InheritanceStructureBirthmarkExtractor(){
        super();
    }

    @Override
    public Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionException{
        BirthmarkElementClassNotFoundException e = new BirthmarkElementClassNotFoundException();

        Birthmark birthmark = new PlainBirthmark(getProvider().getType());
        try{
            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(false);
            BirthmarkExtractVisitor visitor = new InheritanceStructureBirthmarkExtractVisitor(writer, birthmark, context);
            reader.accept(visitor, false);

            if(!visitor.isSuccess()){
                for(Throwable t: visitor.getCauses()){
                    if(t instanceof ClassNotFoundException){
                        e.addClassName(t.getMessage());
                    }
                    else{
                        e.addCause(t);
                    }
                }
            }
        } catch(IOException ee){
            e.addCause(ee);
        } finally{
            if(e.isFailed()){
                throw e;
            }
        }
        return birthmark;
     }
}
