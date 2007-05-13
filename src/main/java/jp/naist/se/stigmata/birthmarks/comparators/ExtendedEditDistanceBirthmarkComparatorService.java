package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id: BirthmarkSpi.java 20 2007-01-17 02:06:01Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by extended edit distance algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 20 $ $Date: 2007-01-17 11:06:01 +0900 (Wed, 17 Jan 2007) $
 */
public class ExtendedEditDistanceBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "editdistanceext";
    }

    public String getComparatorClassName(){
        return "jp.naist.se.stigmata.birthmarks.comparators.ExtendedEditDistanceBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new ExtendedEditDistanceBirthmarkComparator(service);
    }
}

