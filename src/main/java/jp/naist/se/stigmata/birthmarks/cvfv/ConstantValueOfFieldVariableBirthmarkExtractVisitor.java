package jp.naist.se.stigmata.birthmarks.cvfv;

/*
 * $Id: SMCBirthmarkExtractor.java 122 2006-10-06 03:38:54Z harua-t $
 */

import java.util.HashMap;
import java.util.Map;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ConstantValueOfFieldVariableBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private Birthmark birthmark;
    private Map<String, TypeAndValueBirthmarkElement> elements = new HashMap<String, TypeAndValueBirthmarkElement>();
    private String className;

    public ConstantValueOfFieldVariableBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, context);
        this.birthmark = birthmark;
    }

    public void visitEnd(){
        for(String key: elements.keySet()){
            birthmark.addElement(elements.get(key));
        }
        super.visitEnd();
    }

    public void visit(int version, int access, String name, String signature,
        String superName, String[] interfaces){
        this.className = name;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
        String signature, Object value){

        FieldVisitor visitor = super.visitField(access, name, desc, signature, value);

        TypeAndValueBirthmarkElement e = elements.get(name);
        if(e == null){
            e = new TypeAndValueBirthmarkElement(name, desc, value);
        }
        else{
            if(value != null){
                e.setValue(value);
            }
        }
        elements.put(name, e);

        return visitor;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        if(name.equals("<init>") || name.equals("<clinit>")){
            visitor = new MethodAdapter(visitor){
                private Object constant;

                public void visitIntInsn(int opcode, int operand){
                    if(opcode == Opcodes.BIPUSH || opcode == Opcodes.SIPUSH){
                        constant = new Integer(operand);
                    }
                    super.visitIntInsn(opcode, operand);
                }

                public void visitLdcInsn(Object object){
                    constant = object;
                    super.visitLdcInsn(object);
                }

                public void visitFieldInsn(int opcode, String owner, String name, String desc){
                    if(className.equals(owner)){
                        TypeAndValueBirthmarkElement e = elements.get(name);
                        if(e == null){
                            if(!checkCast(desc, constant)){
                                constant = null;
                            }
                            e = new TypeAndValueBirthmarkElement(name, desc, constant);
                        }
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }

        return visitor;
    }

    private boolean checkCast(String desc, Object constant){
        if(constant instanceof Integer){
            return desc.equals("Ljava/lang/Integer;") || desc.equals("I") || 
                desc.equals("S") || desc.equals("Z") ||  desc.equals("C") || 
                desc.equals("B");
        }
        else if(constant instanceof Float){
            return desc.equals("Ljava/lang/Float;") || desc.equals("F"); 
        }
        else if(constant instanceof Double){
            return desc.equals("Ljava/lang/Double;") || desc.equals("D"); 
        }
        else if(constant instanceof Long){
            return desc.equals("Ljava/lang/Long;") || desc.equals("J"); 
        }
        else if(constant instanceof String){
            return desc.equals("Ljava/lang/String;");
        }
        return false;
    }
}
