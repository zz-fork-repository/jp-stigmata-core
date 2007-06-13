package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by edit distance algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class EditDistanceBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "editdistancee";
    }

    public String getComparatorClassName(){
        return "jp.naist.se.stigmata.birthmarks.comparators.EditDistanceBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new EditDistanceBirthmarkComparator(service);
    }
}

