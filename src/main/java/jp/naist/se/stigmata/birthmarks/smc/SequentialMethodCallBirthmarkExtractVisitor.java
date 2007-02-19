package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;

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
public class SequentialMethodCallBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private Birthmark birthmark;

    public SequentialMethodCallBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, context);
        this.birthmark = birthmark;
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions){

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        return new MethodAdapter(visitor){
            public void visitMethodInsn(int opcode, String owner, String name, String desc){
                String className = owner.replace('/', '.');
                if(getContext().getWellknownClassManager().isWellKnownClass(className)){
                    birthmark.addElement(new MethodCallBirthmarkElement(className, name, desc));
                }
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        };
    }
}
