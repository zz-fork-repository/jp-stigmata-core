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
 */
public class FrequencyMethodCallBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyMethodCallBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "fmc";
    }

    @Override
    public String getDefaultDescription(){
        return "Frequency of method call which order is appeared in method definition.";
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    @Override
    public BirthmarkComparator getComparator(){
        return comparator;
    }

    @Override
    public boolean isExperimental(){
        return false;
    }

    @Override
    public boolean isUserDefined(){
        return false;
    }

	@Override
	public BirthmarkElement buildBirthmarkElement(String value) {
    	return new FrequencyBirthmarkElement(value);
	}
}
