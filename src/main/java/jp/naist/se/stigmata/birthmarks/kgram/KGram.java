package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class KGram<T> implements Serializable{
    private static final long serialVersionUID = 273465874532523L;
    private List<T> list = new ArrayList<T>();
    private int maxLength = 4;

    public KGram(int kvalue){
        setKValue(kvalue);
    }

    public void setKValue(int kvalue){
        this.maxLength = kvalue;
    }

    public int getKValue(){
        return maxLength;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer("{ ");
        for(int i = 0; i < maxLength; i++){
            if(i != 0) buffer.append(", ");
            buffer.append(list.get(i));
        }
        buffer.append(" }");
        return new String(buffer);
    }

    public void set(int index, T value){
        if(index < 0 || index >= maxLength){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + maxLength + ": " + index);
        }
        if(list.size() == index){
            list.add(value);
        }
        else{
            list.set(index, value);
        }
    }

    public T get(int index){
        if(index < 0 || index >= maxLength){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + maxLength + ": " + index);
        }
        return list.get(index);
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if(o instanceof KGram){
            KGram kgram = (KGram)o;
            boolean flag = getKValue() == kgram.getKValue();
            for(int i = 0; !flag && i < maxLength; i++){
                if(!get(i).equals(kgram.get(i))){
                    flag = false;
                }
            }
            return flag;
        }
        return false;
    }

    public int hashCode(){
        return list.hashCode();
    }

    @SuppressWarnings("unchecked")
    public static <T> KGram<T>[] buildKGram(T[] values, int kvalue){
        Set<KGram<T>> kgrams = new LinkedHashSet<KGram<T>>();

        if(values.length >= kvalue){
            int max = values.length - (kvalue - 1);
            for(int i = 0; i < max; i++){
                KGram<T> kgram = new KGram<T>(kvalue);
                for(int j = 0; j < kvalue; j++){
                    kgram.set(j, values[i + j]);
                }
                kgrams.add(kgram);
            }
        }
        return kgrams.toArray(new KGram[kgrams.size()]);
    }
}
