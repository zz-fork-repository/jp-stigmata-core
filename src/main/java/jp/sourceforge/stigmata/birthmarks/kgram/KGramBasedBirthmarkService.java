package jp.sourceforge.stigmata.birthmarks.kgram;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new KGramBasedBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "kgram";
    }

    @Override
    public String getDescription(){
        return "k-gram based birthmark.";
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
