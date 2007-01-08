package jp.naist.se.stigmata.birthmarks.cvfv;

/*
 * $Id: SMCBirthmarkExtractor.java 122 2006-10-06 03:38:54Z harua-t $
 */

import java.io.IOException;
import java.io.InputStream;

import jp.naist.se.stigmata.AbstractBirthmarkExtractor;
import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.PlainBirthmark;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 122 $ $Date: 2006-10-06 12:38:54 +0900 (Fri, 06 Oct 2006) $
 */
public class ConstantValueOfFieldVariableBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public ConstantValueOfFieldVariableBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @Override
    public Birthmark extract(InputStream in, BirthmarkContext context) throws IOException{
        Birthmark birthmark = new PlainBirthmark(getProvider().getType());

        ClassReader reader = new ClassReader(in);
        ClassWriter writer = new ClassWriter(false);
        ClassVisitor visitor = new ConstantValueOfFieldVariableBirthmarkExtractVisitor(writer, birthmark, context);
        reader.accept(visitor, false);

        return birthmark;
    }
}
