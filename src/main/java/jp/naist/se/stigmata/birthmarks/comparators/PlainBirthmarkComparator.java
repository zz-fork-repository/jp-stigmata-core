package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PlainBirthmarkComparator implements BirthmarkComparator{
    private BirthmarkSpi spi;

    public PlainBirthmarkComparator(){
    }

    public PlainBirthmarkComparator(BirthmarkSpi spi){
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
        int len = element1.length + element2.length;
        int frac = 0;
        for(int i = 0; i < element1.length && i < element2.length; i++){
            if(element1[i].equals(element2[i])){
                frac += 2;
            }
        }

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
