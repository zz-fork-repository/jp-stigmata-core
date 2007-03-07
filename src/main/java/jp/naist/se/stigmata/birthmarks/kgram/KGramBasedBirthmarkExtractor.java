package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id: SequentialMethodCallBirthmarkExtractor.java 66 2007-03-03 15:11:35Z tama3 $
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.ASMBirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.objectweb.asm.ClassWriter;

/**
 * @author Haruaki TAMADA
 * @version $Revision: 66 $ $Date: 2007-03-04 00:11:35 +0900 (Sun, 04 Mar 2007) $
 */
public class KGramBasedBirthmarkExtractor extends ASMBirthmarkExtractor{
    private int kvalue = 4;

    public KGramBasedBirthmarkExtractor(BirthmarkSpi spi){
        super(spi);
    }

    public KGramBasedBirthmarkExtractor(){
        super();
    }

    public void setKValue(int kvalue){
        this.kvalue = kvalue;
    }

    public int getKValue(){
        return kvalue;
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        KGramBasedBirthmarkExtractVisitor extractor = new KGramBasedBirthmarkExtractVisitor(writer, birthmark, context);
        extractor.setKValue(getKValue());
        return extractor;
    }
}
