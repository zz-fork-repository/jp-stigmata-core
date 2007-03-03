package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import java.util.List;
import java.util.ArrayList;

import jp.naist.se.stigmata.BirthmarkContext;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractVisitor extends ClassAdapter{
    private BirthmarkContext context;
    private List<Throwable> causes = new ArrayList<Throwable>();

    public BirthmarkExtractVisitor(ClassVisitor visitor, BirthmarkContext context){
        super(visitor);
        this.context = context;
    }

    protected BirthmarkContext getContext(){
        return context;
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
