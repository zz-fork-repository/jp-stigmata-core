package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by cosine similarity algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CosineSimilarityBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "cosine";
    }

    public String getComparatorClassName(){
        return "jp.sourceforge.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new CosineSimilarityBirthmarkComparator(service);
    }
}

