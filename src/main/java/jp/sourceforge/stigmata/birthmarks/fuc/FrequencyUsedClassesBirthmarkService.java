package jp.sourceforge.stigmata.birthmarks.fuc;

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
public class FrequencyUsedClassesBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyUsedClassesBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "fuc";
    }

    @Override
    public String getDescription(){
        return "Frequency of used classes in target class.";
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
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }
}
