package jp.sourceforge.stigmata.birthmarks.smc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class SequentialMethodCallBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public SequentialMethodCallBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkEnvironment environment){
        super(visitor, birthmark, environment);
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
                                      String signature, String[] exceptions){

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        return new MethodAdapter(visitor){
            public void visitMethodInsn(int opcode, String owner, String name, String desc){
                String className = owner.replace('/', '.');
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(className)){
                    addElement(className, name, desc);
                }
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        };
    }

    protected void addElement(String className, String methodName, String description){
        addElement(new MethodCallBirthmarkElement(className, methodName, description));
    }
}
