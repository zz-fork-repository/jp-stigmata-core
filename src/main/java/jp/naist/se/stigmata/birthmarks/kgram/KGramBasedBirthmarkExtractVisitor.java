package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id: ConstantValueOfFieldVariableBirthmarkExtractVisitor.java 59 2007-03-03 03:42:06Z tama3 $
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 59 $ $Date: 2007-03-03 12:42:06 +0900 (Sat, 03 Mar 2007) $
 */
public class KGramBasedBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private Birthmark birthmark;
    private int kvalue;
    private List<Integer> opcodes = new ArrayList<Integer>();

    public KGramBasedBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, context);
        this.birthmark = birthmark;
    }

    public int getKValue(){
        return kvalue;
    }

    public void setKValue(int kvalue){
        this.kvalue = kvalue;
    }

    public void visitEnd(){
        Set<KGram> kgrams = new HashSet<KGram>();
        if(opcodes.size() >= getKValue()){
            int kvalue = getKValue();
            int max = opcodes.size() - (kvalue - 1);
            for(int i = 0; i < max; i++){
                KGram kgram = new KGram(kvalue);
                for(int j = 0; j < kvalue; j++){
                    kgram.set(j, opcodes.get(i + j));
                }
                kgrams.add(kgram);
            }
        }
        for(KGram kgram: kgrams){
            birthmark.addElement(new KGramBasedBirthmarkElement(kgram));
        }
    }

    @Override
    public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4){
        MethodVisitor visitor = super.visitMethod(arg0, arg1, arg2, arg3, arg4);
        MethodVisitor opcodeVisitor = new OpcodeExtractionMethodVisitor(visitor, opcodes);

        return opcodeVisitor;
    }

}
