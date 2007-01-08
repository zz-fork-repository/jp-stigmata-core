package jp.naist.se.stigmata.birthmarks.smc;

/*
 * $Id: SMCBirthmarkExtractor.java 122 2006-10-06 03:38:54Z harua-t $
 */

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.PlainBirthmarkComparator;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class SequentialMethodCallBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new SequentialMethodCallBirthmarkExtractor(this);

    public String getType(){
        return "smc";
    }

    public String getDescription(){
        return "Sequence of method call which order is appeared in method definition.";
    }

    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    public BirthmarkComparator getComparator(){
        return comparator;
    }
}
