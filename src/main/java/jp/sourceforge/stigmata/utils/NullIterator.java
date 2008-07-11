package jp.sourceforge.stigmata.utils;

/*
 * $Id$
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Haruaki Tamada 
 * @version $Revision$ 
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
