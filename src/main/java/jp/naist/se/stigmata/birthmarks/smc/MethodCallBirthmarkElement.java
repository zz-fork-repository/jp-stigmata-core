package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id: MethodCallBirthmarkElement.java 76 2006-09-08 17:59:27Z harua-t $
 */

import java.io.Serializable;

import jp.naist.se.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 76 $ $Date: 2006-09-09 02:59:27 +0900 (Sat, 09 Sep 2006) $
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
