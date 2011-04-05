package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by default matching algorithm.
 *
 * @author Haruaki TAMADA
 */
public class PlainBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "plain";
    }

    @Override
    public String getComparatorClassName(){
        return PlainBirthmarkComparator.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new PlainBirthmarkComparator(service);
    }
}

