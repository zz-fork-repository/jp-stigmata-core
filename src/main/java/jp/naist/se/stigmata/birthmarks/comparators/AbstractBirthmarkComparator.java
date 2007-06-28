package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * abstract birthmark comparator.
 *
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkComparator implements BirthmarkComparator{
    private BirthmarkSpi spi;

    @Deprecated
    public AbstractBirthmarkComparator(){
    }

    public AbstractBirthmarkComparator(BirthmarkSpi spi){
        this.spi = spi;
    }

    public BirthmarkSpi getProvider(){
        return spi;
    }

    public String getType(){
        return spi.getType();
    }

    public abstract double compare(Birthmark b1, Birthmark b2);

    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
