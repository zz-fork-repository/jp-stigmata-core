package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id$
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
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class SequentialMethodCallBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public SequentialMethodCallBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @Override
    public Birthmark extract(InputStream in, BirthmarkContext context) throws IOException{
        Birthmark birthmark = new PlainBirthmark(getProvider().getType());

        ClassReader reader = new ClassReader(in);
        ClassWriter writer = new ClassWriter(false);
        ClassVisitor visitor = new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, context);
        reader.accept(visitor, false);

        return birthmark;
    }
}
