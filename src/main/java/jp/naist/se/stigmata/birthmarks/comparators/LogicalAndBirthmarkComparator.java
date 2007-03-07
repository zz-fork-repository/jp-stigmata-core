package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id: PlainBirthmarkComparator.java 20 2007-01-17 02:06:01Z tama3 $
 */

import java.util.Set;
import java.util.HashSet;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * this comparator calculate following formula.
 * let f(p) and f(q) be given birthmarks, then
 * similarity of those birthmarks are defined by |f(p) and f(q)|/|f(p) or f(q)|.
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 20 $ $Date: 2007-01-17 11:06:01 +0900 (Wed, 17 Jan 2007) $
 */
public class LogicalAndBirthmarkComparator implements BirthmarkComparator{
    private BirthmarkSpi spi;

    public LogicalAndBirthmarkComparator(){
    }

    public LogicalAndBirthmarkComparator(BirthmarkSpi spi){
        this.spi = spi;
    }

    public BirthmarkSpi getProvider(){
        return spi;
    }

    public String getType(){
        return spi.getType();
    }

    public double compare(Birthmark b1, Birthmark b2) {
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }


        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        Set<BirthmarkElement> set1 = new HashSet<BirthmarkElement>();
        for(int i = 0; i < element1.length; i++) set1.add(element1[i]);
        Set<BirthmarkElement> set2 = new HashSet<BirthmarkElement>();
        for(int i = 0; i < element2.length; i++) set2.add(element2[i]);

        Set<BirthmarkElement> set = new HashSet<BirthmarkElement>();
        for(BirthmarkElement elem: set1){
            if(set2.contains(elem)) set.add(elem);
        }
        for(BirthmarkElement elem: set2){
            if(set1.contains(elem)) set.add(elem);
        }

        int len = set1.size() + set2.size();
        int frac = set.size() * 2;

        double similarity = (double)frac / (double)len;
        if(len == 0 && frac == 0){
            similarity = 1d;
        }
        return similarity;
    }

    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
