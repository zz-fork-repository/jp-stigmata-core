package jp.naist.se.stigmata.birthmarks.cvfv;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ConstantValueOfFieldVariableBirthmarkExtractor extends ASMBirthmarkExtractor{
    public ConstantValueOfFieldVariableBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context) {
        return new ConstantValueOfFieldVariableBirthmarkExtractVisitor(writer, birthmark, context);
    }
}
