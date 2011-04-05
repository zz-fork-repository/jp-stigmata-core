package jp.sourceforge.stigmata.birthmarks.is;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.sourceforge.stigmata.birthmarks.NullBirthmarkElement;
import jp.sourceforge.stigmata.utils.WellknownClassManager;

import org.objectweb.asm.ClassVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public InheritanceStructureBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces){
        try {
            Class<?> c = getEnvironment().getClasspathContext().findClass(name.replace('/', '.'));
            if(c != null && !c.isInterface()){
                addISBirthmark(c);
            }
        } catch (ClassNotFoundException ex){
            addFailur(ex);
        }
    }

    private void addISBirthmark(Class<?> c){
        WellknownClassManager wcm = getEnvironment().getWellknownClassManager();
        do{
            String className = c.getName();
            BirthmarkElement element = null;
            if(wcm.isWellKnownClass(className)){
                element = new BirthmarkElement(className);
            }
            else{
                element = NullBirthmarkElement.getInstance();
            }

            addElement(element);
            c = c.getSuperclass();
        } while(!c.getName().equals("java.lang.Object"));
        addElement(new BirthmarkElement("java.lang.Object"));
    }
}
