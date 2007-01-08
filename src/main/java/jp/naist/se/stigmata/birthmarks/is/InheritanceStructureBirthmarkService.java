package jp.naist.se.stigmata.birthmarks.is;

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
public class InheritanceStructureBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new InheritanceStructureBirthmarkExtractor(this);

    public String getType(){
        return "is";
    }

    public String getDescription(){
        return "Inheritance sequence to root class and user classes is replaced to <null>.";
    }

    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    public BirthmarkComparator getComparator(){
        return comparator;
    }
}
