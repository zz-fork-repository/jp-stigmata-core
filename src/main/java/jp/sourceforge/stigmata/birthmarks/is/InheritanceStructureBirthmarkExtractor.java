package jp.sourceforge.stigmata.birthmarks.is;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkElementClassNotFoundException;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractionFailedException;
import jp.sourceforge.stigmata.ExtractionUnit;
import jp.sourceforge.stigmata.birthmarks.AbstractBirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
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
    public Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkEnvironment environment) throws BirthmarkExtractionFailedException{
        BirthmarkElementClassNotFoundException e = new BirthmarkElementClassNotFoundException();

        try{
            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(false);
            BirthmarkExtractVisitor visitor = new InheritanceStructureBirthmarkExtractVisitor(writer, birthmark, environment);
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
                System.out.printf("fail: %s%n", e.getMessage());
                throw e;
            }
        }
        return birthmark;
     }

    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, };
    }
}
