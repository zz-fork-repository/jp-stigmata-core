package jp.naist.se.stigmata;

/*
 * $Id: BirthmarkComparator.java 76 2006-09-08 17:59:27Z harua-t $
 */

/**
 * Interface for comparing given two birthmarks and calculate similarity.
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 76 $ $Date: 2006-09-09 02:59:27 +0900 (Sat, 09 Sep 2006) $
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
