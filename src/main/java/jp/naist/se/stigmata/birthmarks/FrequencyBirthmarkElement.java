package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class FrequencyBirthmarkElement extends BirthmarkElement implements ValueCountable{
    private static final long serialVersionUID = 4454345943098520436L;

    private int count = 1;

    public FrequencyBirthmarkElement(String value){
        this(value, 1);
    }

    public FrequencyBirthmarkElement(String value, int count){
        super(value);
        this.count = count;
    }

    void incrementValueCount(){
        count++;
    }

    @Override
    public boolean equals(Object o){
        boolean flag = false;
        if(o instanceof FrequencyBirthmarkElement){
            FrequencyBirthmarkElement fmbe = (FrequencyBirthmarkElement)o;
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
        int hash = super.hashCode();
        int shift = getValueCount() % 32;

        // cyclic shift
        for(int i = 0; i < shift; i++){
            int v = hash & 1;
            hash = hash >>> 1 | v << 31;
        }

        return hash;
    }

    public String getValueName(){
        return (String)super.getValue();
    }

    public int getValueCount(){
        return count;
    }
}
