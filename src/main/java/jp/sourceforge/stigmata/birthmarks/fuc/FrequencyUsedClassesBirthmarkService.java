package jp.sourceforge.stigmata.birthmarks.fuc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.AbstractBirthmarkService;
import jp.sourceforge.stigmata.birthmarks.FrequencyBirthmarkElement;
import jp.sourceforge.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
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

    public BirthmarkElement createBirthmarkElement(String value){
    	return new FrequencyBirthmarkElement(value);
    }
}
