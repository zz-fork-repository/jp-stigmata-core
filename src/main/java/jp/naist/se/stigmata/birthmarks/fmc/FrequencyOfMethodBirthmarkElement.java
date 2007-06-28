package jp.naist.se.stigmata.birthmarks.fmc;

import jp.naist.se.stigmata.birthmarks.ValueCountable;
import jp.naist.se.stigmata.birthmarks.smc.MethodCallBirthmarkElement;

public class FrequencyOfMethodBirthmarkElement extends MethodCallBirthmarkElement implements ValueCountable{
    private static final long serialVersionUID = 4454345943098520436L;

    private int count = 1;

    public FrequencyOfMethodBirthmarkElement(String className, String methodName, String signature){
        super(className, methodName, signature);
    }

    void incrementValueCount(){
        count++;
    }

    @Override
    public boolean equals(Object o){
        boolean flag = false;
        if(o instanceof FrequencyOfMethodBirthmarkElement){
            FrequencyOfMethodBirthmarkElement fmbe = (FrequencyOfMethodBirthmarkElement)o;
            flag = super.equals(fmbe) && getValueCount() == fmbe.getValueCount();
        }
        return flag;
    }

    @Override
    public Object getValue(){
        return getValueCount() + ": " + getValueName();
    }

    @Override
    public int hashCode(){
        return System.identityHashCode(this);
    }

    public String getValueName(){
        return getClassName() + "#" + getMethodName() + "!" + getSignature();
    }

    public int getValueCount(){
        return count;
    }
}
