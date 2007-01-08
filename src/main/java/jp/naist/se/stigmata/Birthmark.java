package jp.naist.se.stigmata;

/*
 * $Id: Birthmark.java 105 2006-09-19 10:06:47Z harua-t $
 */

import java.util.Iterator;

/**
 * This interface represents the birthmark.
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 105 $ $Date: 2006-09-19 19:06:47 +0900 (Tue, 19 Sep 2006) $
 */
public interface Birthmark extends Iterable<BirthmarkElement>{
    /**
     * return all elements of this birthmark.
     * 
     * @return all elements.
     */
    public BirthmarkElement[] getElements();

    /**
     * returns the iterator for all elements of this birthmark.
     * @return iterator for accessing to elements of this birthmark.
     */
    public Iterator<BirthmarkElement> iterator();

    /**
     * returns the number of elements of this birthmark.
     * 
     * @return element count
     */
    public int getElementCount();

    /**
     * add element to this birthmark.
     * 
     * @param element new element
     */
    public void addElement(BirthmarkElement element);

    /**
     * return the type of this birthmark.
     * 
     * @return birthmark type
     */
    public String getType();

    /**
     * This method check types of this birthmark and given birthmark are matched.
     * 
     * @param b check target.
     * @return true: same type, false: not same type.
     */
    public boolean isSameType(Birthmark birthmark);
}
