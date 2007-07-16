package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class SequentialMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public SequentialMethodCallBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public SequentialMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkEnvironment environment){
        return new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, environment);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, ExtractionUnit.ARCHIVE, ExtractionUnit.PACKAGE, };
    }
}
