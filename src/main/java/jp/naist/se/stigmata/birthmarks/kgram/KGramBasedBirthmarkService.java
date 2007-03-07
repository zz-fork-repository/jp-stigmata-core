package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id: ConstantValueOfFieldVariableBirthmarkService.java 59 2007-03-03 03:42:06Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.AbstractBirthmarkService;
import jp.naist.se.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision: 59 $ $Date: 2007-03-03 12:42:06 +0900 (Sat, 03 Mar 2007) $
 */
public class KGramBasedBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new KGramBasedBirthmarkExtractor(this);

    public String getType(){
        return "kgram";
    }

    public String getDefaultDescription(){
        return "k-gram based birthmark.";
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
