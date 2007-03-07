package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class KGram implements Serializable{
    private static final long serialVersionUID = 273465874532523L;
    private int[] values;

    public KGram(int kvalue){
        values = new int[kvalue];
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer("{ ");
        for(int i = 0; i < values.length; i++){
            if(i != 0) buffer.append(", ");
            buffer.append(values[i]);
        }
        buffer.append(" }");
        return new String(buffer);
    }

    public void set(int index, int value){
        if(index < 0 || index >= values.length){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + values.length + ": " + index);
        }
        values[index] = value;
    }

    public int get(int index){
        if(index < 0 || index >= values.length){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + values.length + ": " + index);
        }
        return values[index];
    }

    public boolean equals(Object o){
        if(o instanceof KGram){
            return Arrays.equals(values, ((KGram)o).values);
        }
        return false;
    }

    public int hashCode(){
        return Arrays.hashCode(values);
    }
}
