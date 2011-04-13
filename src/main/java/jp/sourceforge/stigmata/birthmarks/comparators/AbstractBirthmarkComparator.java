package jp.sourceforge.stigmata.birthmarks.comparators;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * abstract birthmark comparator.
 *
 * @author Haruaki Tamada
 */
public abstract class AbstractBirthmarkComparator implements BirthmarkComparator{
    private BirthmarkService spi;

    @Deprecated
    public AbstractBirthmarkComparator(){
    }

    public AbstractBirthmarkComparator(BirthmarkService spi){
        this.spi = spi;
    }

    public BirthmarkService getProvider(){
        return spi;
    }

    @Override
    public String getType(){
        return spi.getType();
    }

    @Override
    public abstract double compare(Birthmark b1, Birthmark b2, BirthmarkContext context);

    @Override
    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
