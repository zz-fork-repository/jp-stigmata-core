package jp.sourceforge.stigmata.utils;

import java.util.Iterator;

/**
 * 
 * @author Haruaki Tamada
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

    @Override
    public boolean hasNext(){
        return currentIndex < length;
    }

    @Override
    public T next(){
        T value = values[currentIndex];
        currentIndex++;
        return value;
    }

    @Override
    public void remove(){
    }
}
