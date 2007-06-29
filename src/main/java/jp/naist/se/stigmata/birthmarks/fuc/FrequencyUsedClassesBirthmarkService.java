package jp.naist.se.stigmata.birthmarks.fuc;

/*
 * $Id: UsedClassesBirthmarkService.java 140 2007-06-28 10:48:47Z tama3 $
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
 * @version $Revision: 140 $ $Date: 2007-06-28 19:48:47 +0900 (Thu, 28 Jun 2007) $
 */
public class FrequencyUsedClassesBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyUsedClassesBirthmarkExtractor(this);

    public String getType(){
        return "fuc";
    }

    public String getDefaultDescription(){
        return "Frequency of used classes in target class.";
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
