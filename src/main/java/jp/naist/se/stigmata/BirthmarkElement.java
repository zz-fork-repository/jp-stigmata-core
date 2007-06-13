package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.Serializable;

/**
 * element of birthmark.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class BirthmarkElement implements Serializable{
    private static final long serialVersionUID = 943675475343245243L;

    /**
     * element value.
     */
    private String value;

    /**
     * construct birthmark element with given value. 
     */
    public BirthmarkElement(String value) {
        this.value = value;
    }

    /**
     * return the value of this element.
     */
    public Object getValue(){
        return value;
    }

    /**
     * to string.
     */
    public String toString(){
        return value;
    }

    /**
     * hash code for overriding equals method.
     */
    public int hashCode(){
        if(getValue() == null){
            return 0;
        }
        else{
            return getValue().hashCode();
        }
    }

    /**
     * equals method.
     */
    public boolean equals(Object o){
        if(o instanceof BirthmarkElement){
            if(getValue() != null){
                return getValue().equals(((BirthmarkElement)o).getValue());
            }
            else{
                return ((BirthmarkElement)o).getValue() == null;
            }
        }
        return false;
    }
}
