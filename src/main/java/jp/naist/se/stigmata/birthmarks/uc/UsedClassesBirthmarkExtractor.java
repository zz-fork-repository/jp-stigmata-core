package jp.naist.se.stigmata.birthmarks.uc;

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
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class UsedClassesBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public UsedClassesBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    @Override
    public Birthmark extract(InputStream in, BirthmarkContext context) throws IOException{
        Birthmark birthmark = new PlainBirthmark(getProvider().getType());

        ClassReader reader = new ClassReader(in);
        ClassWriter writer = new ClassWriter(false);
        ClassVisitor visitor = new UsedClassesBirthmarkExtractVisitor(writer, birthmark, context);
        reader.accept(visitor, false);

        return birthmark;
    }
}
