package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id$
 */

import java.io.Serializable;

import jp.naist.se.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class MethodCallBirthmarkElement extends BirthmarkElement implements Serializable {
    private static final long serialVersionUID = -3178451461780859954L;

    private String className;
    private String methodName;
    private String signature;

    public MethodCallBirthmarkElement(String className, String methodName, String signature) {
        super(className + "#" + methodName);

        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
    }

    public String getClassName(){
        return className;
    }

    public String getMethodName(){
        return methodName;
    }

    public String getSignature(){
        return signature;
    }

    public Object getValue(){
        return getClassName() + "#" + getMethodName();
    }

    public boolean equals(Object o){
        boolean flag = false;
        if(o instanceof MethodCallBirthmarkElement){
            MethodCallBirthmarkElement mcbe = (MethodCallBirthmarkElement)o;

            flag = getClassName().equals(mcbe.getClassName()) &&
                getMethodName().equals(mcbe.getMethodName())  &&
                getSignature().equals(mcbe.getSignature());
        }

        return flag;
    }
}
