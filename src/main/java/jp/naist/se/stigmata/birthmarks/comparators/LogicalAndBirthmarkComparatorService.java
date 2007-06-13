package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by logical AND algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class LogicalAndBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "and";
    }

    public String getComparatorClassName(){
        return "jp.naist.se.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new LogicalAndBirthmarkComparator(service);
    }
}

