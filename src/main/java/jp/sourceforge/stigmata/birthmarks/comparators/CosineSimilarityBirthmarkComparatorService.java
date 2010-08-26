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
 * @version $Revision$
 */
public class CosineSimilarityBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "cosine";
    }

    @Override
    public String getComparatorClassName(){
        return CosineSimilarityBirthmarkComparator.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        return new CosineSimilarityBirthmarkComparator(service);
    }
}

