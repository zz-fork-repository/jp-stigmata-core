package jp.sourceforge.stigmata.birthmarks.fmc;

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
 * @version $Revision$ 
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

	@Override
	public BirthmarkElement buildBirthmarkElement(String value) {
    	return new FrequencyBirthmarkElement(value);
	}
}
