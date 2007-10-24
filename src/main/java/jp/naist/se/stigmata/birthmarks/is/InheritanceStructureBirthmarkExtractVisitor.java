package jp.naist.se.stigmata.birthmarks.is;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.birthmarks.BirthmarkExtractVisitor;
import jp.naist.se.stigmata.birthmarks.NullBirthmarkElement;
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
    public InheritanceStructureBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkEnvironment environment){
        super(visitor, birthmark, environment);
    }

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
