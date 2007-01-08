package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

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

    public BirthmarkExtractVisitor(ClassVisitor visitor, BirthmarkContext context){
        super(visitor);
        this.context = context;
    }

    protected BirthmarkContext getContext(){
        return context;
    }
}
