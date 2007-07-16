package jp.naist.se.stigmata.birthmarks.is;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkElementClassNotFoundException;
import jp.naist.se.stigmata.BirthmarkExtractionFailedException;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.AbstractBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
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
                throw e;
            }
        }
        return birthmark;
     }

    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, };
    }
}
