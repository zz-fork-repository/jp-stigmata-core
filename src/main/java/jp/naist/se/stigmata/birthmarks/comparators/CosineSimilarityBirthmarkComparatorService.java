package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id: DPMatchingBirthmarkComparatorService.java 130 2007-06-13 10:08:01Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by cosine similarity algorithm.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 130 $ $Date: 2007-06-13 19:08:01 +0900 (Wed, 13 Jun 2007) $
 */
public class CosineSimilarityBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "cosine";
    }

    public String getComparatorClassName(){
        return "jp.naist.se.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new CosineSimilarityBirthmarkComparator(service);
    }
}

