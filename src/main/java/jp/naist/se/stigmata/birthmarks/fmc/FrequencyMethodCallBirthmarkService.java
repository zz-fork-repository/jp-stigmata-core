package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.AbstractBirthmarkService;
import jp.naist.se.stigmata.birthmarks.FrequencyBirthmarkElement;
import jp.naist.se.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
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
