package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by extended edit distance algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ExtendedEditDistanceBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "editdistanceext";
    }

    public String getComparatorClassName(){
        return "jp.sourceforge.stigmata.birthmarks.comparators.ExtendedEditDistanceBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new ExtendedEditDistanceBirthmarkComparator(service);
    }
}
