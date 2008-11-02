package jp.sourceforge.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents k-gram of the some sequence. 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class KGram<T> implements Serializable{
    private static final long serialVersionUID = 273465874532523L;
    // private List<T> list = new ArrayList<T>();
    private int maxLength = 4;
    private T[] values;

    /**
     * constructor.
     * @param kvalue the number of elements of this object.
     */
    public KGram(int kvalue){
        setKValue(kvalue);
    }

    /**
     * sets k-value. 
     * @param kvalue the number of elements of this object.
     */
    public void setKValue(int kvalue){
        this.maxLength = kvalue;
    }

    /**
     * returns k-value which is the number of elements.
     * @return the number of elements.
     */
    public int getKValue(){
        return maxLength;
    }

    /**
     * returns string representation of this object.
     */
    public String toString(){
        StringBuffer buffer = new StringBuffer("{ ");
        for(int i = 0; i < maxLength; i++){
            if(i != 0) buffer.append(", ");
            buffer.append(get(i));
        }
        buffer.append(" }");
        return new String(buffer);
    }

    /**
     * sets the given value to kgram element at given index.
     * @param index index.
     * @param value value.
     */
    @SuppressWarnings("unchecked")
    public void set(int index, T value){
        if(index < 0 || index >= maxLength){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + (maxLength - 1) + ": " + index);
        }
        if(value == null){
            throw new NullPointerException("null value");
        }
        if(values == null){
            values = (T[])Array.newInstance(value.getClass(), getKValue());
        }
        values[index] = value;
    }

    /**
     * returns an object of given index.
     */
    public T get(int index){
        T returnValue = null;
        if(index < 0 || index >= maxLength){
            throw new ArrayIndexOutOfBoundsException("expected 0-" + (maxLength - 1) + ": " + index);
        }
        if(values != null){
            returnValue = values[index];
        }

        return returnValue;
    }

    /**
     * adds value at last index.
     * 
     * this object is called with given 2 when following situation, 
     * <ul>
     *   <li>``{ 1, 3, null, null }'' -&gt; ``{ 1, 2, 3, null }'' and return 2<li>
     *   <li>``{ 1, null, 3, null }'' -&gt; ``{ 1, 2, 3, null }'' and return 1<li>
     *   <li>``{ 1, 2, 3, 4 }'' -&gt; ``{ 1, 2, 3, 4 }'' and return -1<li>
     * </ul>
     * 
     * @param value value for addition.
     * @return added index.
     */
    pulic int add(T value){
        int index = -1;
        for(int i = 0; i < values.length; i++){
            if(values[i] == null){
                index = i;
                values[i] = value;
                break;
            }
        }
        return index;
    }

    /**
     * returns an array of elements this object has.
     * @return
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(){
        if(values == null){
            throw new IllegalStateException("this object has no elements.");
        }
        T[] newarray = (T[])Array.newInstance(values[0].getClass(), getKValue());
        System.arraycopy(values, 0, newarray, 0, getKValue());
        return newarray;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if(o instanceof KGram){
            KGram kgram = (KGram)o;
            boolean flag = getKValue() == kgram.getKValue();
            for(int i = 0; !flag && i < maxLength; i++){
                if(!get(i).equals(kgram.get(i))){
                    flag = false;
                    break;
                }
            }
            return flag;
        }
        return false;
    }

    public int hashCode(){
        return Arrays.hashCode(values);
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
