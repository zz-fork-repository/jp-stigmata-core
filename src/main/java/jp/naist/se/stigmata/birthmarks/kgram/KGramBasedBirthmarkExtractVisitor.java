package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class KGramBasedBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private int kvalue;
    private List<Integer> opcodes = new ArrayList<Integer>();

    public KGramBasedBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkEnvironment environment){
        super(visitor, birthmark, environment);
    }

    public int getKValue(){
        return kvalue;
    }

    public void setKValue(int kvalue){
        this.kvalue = kvalue;
    }

    public void visitEnd(){
        Set<KGram<Integer>> kgrams = new HashSet<KGram<Integer>>();
        if(opcodes.size() >= getKValue()){
            int kvalue = getKValue();
            int max = opcodes.size() - (kvalue - 1);
            for(int i = 0; i < max; i++){
                KGram<Integer> kgram = new KGram<Integer>(kvalue);
                for(int j = 0; j < kvalue; j++){
                    kgram.set(j, opcodes.get(i + j));
                }
                kgrams.add(kgram);
            }
        }
        for(KGram<Integer> kgram: kgrams){
            addElement(new KGramBasedBirthmarkElement<Integer>(kgram));
        }
    }

    @Override
    public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4){
        MethodVisitor visitor = super.visitMethod(arg0, arg1, arg2, arg3, arg4);
        MethodVisitor opcodeVisitor = new OpcodeExtractionMethodVisitor(visitor, opcodes);

        return opcodeVisitor;
    }
}
