package jp.sourceforge.stigmata.birthmarks.is;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.birthmarks.comparators.PlainBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new InheritanceStructureBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "is";
    }

    @Override
    public String getDescription(){
        return "Inheritance sequence to root class and user classes is replaced to <null>.";
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
