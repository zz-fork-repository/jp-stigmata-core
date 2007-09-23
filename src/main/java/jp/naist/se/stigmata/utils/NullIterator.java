package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Haruaki Tamada 
 * @version $Revision$ $Date$
 */
public class NullIterator<T> implements Iterator<T>{
    public boolean hasNext(){
        return false;
    }

    public T next(){
        throw new NoSuchElementException("no more object");
    }

    public void remove(){
    }
}
