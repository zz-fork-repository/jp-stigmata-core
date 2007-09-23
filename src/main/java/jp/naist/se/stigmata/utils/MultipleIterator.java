package jp.naist.se.stigmata.utils;

/* 
 * $Id$
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class MultipleIterator<T> implements Iterator<T>{
    private List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
    private int index = 0;
    private Iterator<T> current;
    private boolean finished = false;

    public MultipleIterator(){
    }

    public MultipleIterator(Iterator<T>[] iteratorArray){
        for(Iterator<T> iterator: iteratorArray){
            iterators.add(iterator);
        }
    }

    public MultipleIterator(Iterator<Iterator<T>> iterator){
        while(iterator.hasNext()){
            iterators.add(iterator.next());
        }
    }

    public MultipleIterator(Collection<Iterator<T>> collection){
        this(collection.iterator());
    }

    public void add(Iterator<T> iterator){
        iterators.add(iterator);
    }

    public void remove(Iterator<T> iterator){
        iterators.remove(iterator);
    }

    private void nextIterator(){
        if(index >= iterators.size()){
            current = null;
            finished = true;
            return;
        }
        current = iterators.get(index);
        index++;
    }

    public void remove(){
        current.remove();
    }

    /**
     */
    public boolean hasNext(){
        if(current == null){
            nextIterator();
        }
        boolean flag = current.hasNext();

        if(!flag){
            nextIterator();
        }

        return finished || current.hasNext();
    }

    /** 
     */
    public T next(){
        if(hasNext()){
            return current.next();
        }

        throw new NoSuchElementException();
    }
}
