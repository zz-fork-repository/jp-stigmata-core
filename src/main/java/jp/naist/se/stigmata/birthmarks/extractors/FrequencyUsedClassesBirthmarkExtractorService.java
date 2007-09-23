package jp.naist.se.stigmata.birthmarks.extractors;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.fuc.FrequencyUsedClassesBirthmarkExtractor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FrequencyUsedClassesBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "fuc";
    }

    public String getExtractorClassName(){
        return "jp.naist.se.stigmata.birthmarks.fuc.FrequencyUsedClassesBirthmarkExtractor";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new FrequencyUsedClassesBirthmarkExtractor(service);
    }
}