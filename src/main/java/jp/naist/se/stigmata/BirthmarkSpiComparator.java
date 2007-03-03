package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.Comparator;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class BirthmarkSpiComparator implements Comparator<BirthmarkSpi>{
    /**
     * default constructor
     */
    public BirthmarkSpiComparator(){
    }

    public int compare(BirthmarkSpi s1, BirthmarkSpi s2){
        if(s1.isExpert() && !s2.isExpert()){
            return 1;
        }
        else if(!s1.isExpert() && s2.isExpert()){
            return -1;
        }
        else{
            return s1.getType().compareTo(s2.getType());
        }
    }

    public boolean equals(Object o){
        return o == this;
    }
}