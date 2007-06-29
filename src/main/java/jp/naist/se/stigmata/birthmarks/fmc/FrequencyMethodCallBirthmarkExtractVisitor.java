package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.birthmarks.FrequencyBirthmarkElement;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FrequencyMethodCallBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public FrequencyMethodCallBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
                                      String signature, String[] exceptions){

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        return new MethodAdapter(visitor){
            public void visitMethodInsn(int opcode, String owner, String name, String desc){
                String className = owner.replace('/', '.');
                if(getContext().getWellknownClassManager().isWellKnownClass(className)){
                    addElement(new FrequencyBirthmarkElement(className + "#" + name + desc));
                }
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        };
    }
}
