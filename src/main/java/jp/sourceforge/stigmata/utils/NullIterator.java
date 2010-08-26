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
    @Override
    public boolean hasNext(){
        return false;
    }

    @Override
    public T next(){
        throw new NoSuchElementException("no more object");
    }

    @Override
    public void remove(){
    }
}
