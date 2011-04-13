package jp.sourceforge.stigmata.birthmarks.fmc;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 * 
 * @author Haruaki TAMADA
 */
public class FrequencyMethodCallBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyMethodCallBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "fmc";
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
    public String getDescription(){
        return "Frequency of Method Calls";
    }

    @Override
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }
}
