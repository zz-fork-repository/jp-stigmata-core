package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.util.Iterator;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revission$ $Date$
 */
public class ArrayIterator<T> implements Iterator<T>{
    private T[] values;
    private int length;
    private int currentIndex = 0;

    public ArrayIterator(T[] values){
        this.values = values;
        if(values != null){
            length = values.length;
        }
        else{
            length = 0;
        }
    }

    public boolean hasNext(){
        return currentIndex < length;
    }

    public T next(){
        T value = values[currentIndex];
        currentIndex++;
        return value;
    }

    public void remove(){
    }
}
