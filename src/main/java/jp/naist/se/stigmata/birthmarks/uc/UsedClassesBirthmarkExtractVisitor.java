package jp.naist.se.stigmata.birthmarks.uc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureWriter;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class UsedClassesBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public UsedClassesBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark,
                                              BirthmarkEnvironment environment){
        super(visitor, birthmark, environment);
    }

    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces){
        addSignatureClass(signature);

        if(getEnvironment().getWellknownClassManager().isWellKnownClass(superName)){
            add(superName);
        }
        for(String i: interfaces){
            if(getEnvironment().getWellknownClassManager().isWellKnownClass(i)){
                add(i);
            }
        }
    }

    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value){
        FieldVisitor visitor = super.visitField(access, name, desc, signature, value);

        addDescriptor(desc);
        addSignatureClass(signature);

        return visitor;
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions){
        if(exceptions != null){
            for(String exception: exceptions){
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(exception)){
                    add(exception);
                }
            }
        }
        addMethodDescriptor(desc);
        addSignatureClass(signature);

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        return new MethodAdapter(visitor){
            public void visitTypeInsn(int opcode, String desc){
                Type type = Type.getType("L" + desc + ";");
                if(checkType(type)){
                    add(normalize(desc));
                }
                super.visitTypeInsn(opcode, desc);
            }

            public void visitTryCatchBlock(Label start, Label end, Label handle, String desc){
                Type type = Type.getType("L" + desc + ";");
                if(checkType(type)){
                    add(getType(type));
                }
                super.visitTryCatchBlock(start, end, handle, desc);
            }

            public void visitMultiANewArrayInsn(String desc, int dims){
                Type type = Type.getType(desc);
                if(checkType(type)){
                    add(getType(type));
                }
                super.visitMultiANewArrayInsn(desc, dims);
            }

            public void visitLocalVariable(String name, String desc, String signature,
                                           Label start, Label end, int index){
                if(checkType(Type.getType(desc))){
                    add(normalize(desc));
                }
                addSignatureClass(signature);

                super.visitLocalVariable(name, desc, signature, start, end, index);
            }

            public void visitFieldInsn(int opcode, String owner, String name, String desc){
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(owner)){
                    add(normalize(owner));
                }
                addDescriptor(desc);
                super.visitFieldInsn(opcode, owner, name, desc);
            }
            public void visitMethodInsn(int opcode, String owner, String name, String desc){
                String className = normalize(owner);
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(className)){
                    add(className);
                }
                addMethodDescriptor(desc);
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        };
    }

    private void addSignatureClass(String signature){
        if(signature != null){
            SignatureReader in = new SignatureReader(signature);
            SignatureWriter writer = new SignatureWriter(){
                public void visitClassType(String classType){
                    if(getEnvironment().getWellknownClassManager().isWellKnownClass(classType)){
                        add(normalize(classType));
                    }
                }
            };
            in.accept(writer);
        }
    }

    private void addMethodDescriptor(String desc){
        Type returnType = Type.getReturnType(desc);
        Type[] args = Type.getArgumentTypes(desc);
        if(checkType(returnType)){
            add(getType(returnType));
        }
        for(Type arg: args){
            if(checkType(arg)){
                add(getType(arg));
            }
        }
    }

    private void addDescriptor(String desc){
        Type type = Type.getType(desc);
        if(checkType(type)){
            add(normalize(desc));
        }
    }

    private String getType(Type type){
        if(type.getSort() == Type.ARRAY){
            while(type.getSort() != Type.ARRAY){
                type = type.getElementType();
            }
        }

        if(type.getSort() == Type.OBJECT){
            return normalize(type.getClassName());
        }

        return null;
    }

    private boolean checkType(Type type){
        if(type.getSort() == Type.ARRAY){
            while(type.getSort() != Type.ARRAY){
                type = type.getElementType();
            }
        }

        if(type.getSort() == Type.OBJECT){
            String className = type.getClassName();
            if(getEnvironment().getWellknownClassManager().isWellKnownClass(className)){
                return true;
            }
        }
        return false;
    }

    private String normalize(String name){
        if(name.startsWith("L") && name.endsWith(";")){
            name = name.substring(1, name.length() - 1);
        }
        name = name.replace('/', '.');

        return name;
    }

    private void add(String name){
        addElement(new BirthmarkElement(name));
    }
}
