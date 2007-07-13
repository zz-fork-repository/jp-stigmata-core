package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkElement;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

/**
 * Abstract visitor class of extracting birthmarks from class file.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class BirthmarkExtractVisitor extends ClassAdapter{
    private Birthmark birthmark;
    private BirthmarkEnvironment context;
    private List<Throwable> causes = new ArrayList<Throwable>();

    public BirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkEnvironment context){
        super(visitor);
        this.birthmark = birthmark;
        this.context = context;
    }

    protected BirthmarkEnvironment getContext(){
        return context;
    }

    protected void addElement(BirthmarkElement element){
        birthmark.addElement(element);
    }

    public Birthmark getBirthmark(){
        return birthmark;
    }

    public synchronized void addFailur(Throwable e){
        causes.add(e);
    }

    public boolean isSuccess(){
       return causes.size() == 0;
    }

    public synchronized Throwable[] getCauses(){
       return causes.toArray(new Throwable[causes.size()]);
    }
}
