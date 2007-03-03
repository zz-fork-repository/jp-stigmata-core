package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id: DPMatchingBirthmarkComparator.java 53 2007-02-22 02:46:40Z tama3 $
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * calculate similarities between two birthmarks by edit distance
 * algorithm (levenshtein distance).
 *
 * @author Haruaki TAMADA
 * @version $Revision: 53 $ $Date: 2007-02-22 11:46:40 +0900 (Thu, 22 Feb 2007) $
 */
public class ExtendedEditDistanceBirthmarkComparator extends EditDistanceBirthmarkComparator{
    public ExtendedEditDistanceBirthmarkComparator(){
    }

    public ExtendedEditDistanceBirthmarkComparator(BirthmarkSpi spi){
        super(spi);
    }

    public double compare(Birthmark b1, Birthmark b2) {
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        int[][] distance = createDistanceMatrics(element1, element2);

        int length = element1.length;
        if(length > element2.length){
            length = element2.length;
        }
        int d = distance[element1.length][element2.length];

        return (double)(1 - (d + Math.abs(element1.length - element2.length))) / length;
    }
}
