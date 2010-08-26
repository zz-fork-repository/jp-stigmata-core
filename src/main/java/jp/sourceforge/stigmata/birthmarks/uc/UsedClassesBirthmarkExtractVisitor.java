package jp.sourceforge.stigmata.birthmarks.uc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;

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
 * @version $Revision$ 
 */
public class UsedClassesBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public UsedClassesBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark,
            BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    @Override
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

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value){
        FieldVisitor visitor = super.visitField(access, name, desc, signature, value);

        addDescriptor(desc);
        addSignatureClass(signature);

        return visitor;
    }

    @Override
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
            @Override
            public void visitTypeInsn(int opcode, String desc){
                Type type = Type.getType("L" + desc + ";");
                if(checkType(type)){
                    add(desc);
                }
                super.visitTypeInsn(opcode, desc);
            }

            @Override
            public void visitTryCatchBlock(Label start, Label end, Label handle, String desc){
                Type type = Type.getType("L" + desc + ";");
                if(checkType(type)){
                    add(getType(type));
                }
                super.visitTryCatchBlock(start, end, handle, desc);
            }

            @Override
            public void visitMultiANewArrayInsn(String desc, int dims){
                Type type = Type.getType(desc);
                if(checkType(type)){
                    add(getType(type));
                }
                super.visitMultiANewArrayInsn(desc, dims);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature,
                                           Label start, Label end, int index){
                if(checkType(Type.getType(desc))){
                    add(desc);
                }
                addSignatureClass(signature);

                super.visitLocalVariable(name, desc, signature, start, end, index);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc){
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(owner)){
                    add(owner);
                }
                addDescriptor(desc);
                super.visitFieldInsn(opcode, owner, name, desc);
            }
            @Override
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
                @Override
                public void visitClassType(String classType){
                    if(getEnvironment().getWellknownClassManager().isWellKnownClass(classType)){
                        add(classType);
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
            add(desc);
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
        addElement(new BirthmarkElement(normalize(name)));
    }
}
