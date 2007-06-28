package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id: SequentialMethodCallBirthmarkExtractVisitor.java 130 2007-06-13 10:08:01Z tama3 $
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
 * @version $Revision: 130 $ $Date: 2007-06-13 19:08:01 +0900 (Wed, 13 Jun 2007) $
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
                    addElement(new FrequencyOfMethodBirthmarkElement(className, name, desc));
                }
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        };
    }
}
