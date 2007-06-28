package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id: SequentialMethodCallBirthmarkService.java 130 2007-06-13 10:08:01Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.AbstractBirthmarkService;
import jp.naist.se.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 130 $ $Date: 2007-06-13 19:08:01 +0900 (Wed, 13 Jun 2007) $
 */
public class FrequencyMethodCallBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyMethodCallBirthmarkExtractor(this);

    public String getType(){
        return "fmc";
    }

    public String getDefaultDescription(){
        return "Frequency of method call which order is appeared in method definition.";
    }

    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    public BirthmarkComparator getComparator(){
        return comparator;
    }

    public boolean isExpert(){
        return false;
    }

    public boolean isUserDefined(){
        return false;
    }
}
