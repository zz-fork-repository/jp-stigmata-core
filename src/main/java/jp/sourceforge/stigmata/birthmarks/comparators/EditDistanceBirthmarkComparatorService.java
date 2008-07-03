package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

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
        return EditDistanceBirthmarkComparator.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new EditDistanceBirthmarkComparator(service);
    }
}

