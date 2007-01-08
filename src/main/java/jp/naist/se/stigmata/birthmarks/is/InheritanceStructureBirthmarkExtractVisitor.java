package jp.naist.se.stigmata.birthmarks.is;

/*
 * $Id: SMCBirthmarkExtractor.java 122 2006-10-06 03:38:54Z harua-t $
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.utils.WellknownClassManager;

import org.objectweb.asm.ClassVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class InheritanceStructureBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private Birthmark birthmark;

    public InheritanceStructureBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, context);
        this.birthmark = birthmark;
    }

    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces){
        try {
            Class c = getContext().getBytecodeContext().findClass(name.replace('/', '.'));
            if(c != null && !c.isInterface()){
                addISBirthmark(c);
            }
        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    private void addISBirthmark(Class c){
        WellknownClassManager wcm = getContext().getWellknownClassManager();
        do{
            String className = c.getName();

            if(wcm.isWellKnownClass(className)){
                birthmark.addElement(new BirthmarkElement(className));
            }
            else{
                birthmark.addElement(BirthmarkElement.NULL);
            }
            c = c.getSuperclass();
        } while(!c.getName().equals("java.lang.Object"));
        birthmark.addElement(new BirthmarkElement("java.lang.Object"));
    }
}
