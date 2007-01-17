package jp.naist.se.stigmata;

/*
 * $Id$
 */

/**
 * Interface for comparing given two birthmarks and calculate similarity.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkComparator {
    /**
     * compare given two birthmarks and returns calculated similarity. 
     */
    public double compare(Birthmark b1, Birthmark b2);

    /**
     * returns the number of comparison. 
     */
    public int getCompareCount(Birthmark b1, Birthmark b2);

    /**
     * returns the type of birthmarks this comparator supports. 
     */
    public String getType();
}
