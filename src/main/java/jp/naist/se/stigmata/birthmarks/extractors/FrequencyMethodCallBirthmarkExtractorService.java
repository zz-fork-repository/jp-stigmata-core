package jp.naist.se.stigmata.birthmarks.extractors;

/*
 * $Id: UsedClassesBirthmarkExtractorService.java 140 2007-06-28 10:48:47Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.fmc.FrequencyMethodCallBirthmarkExtractor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 140 $ $Date: 2007-06-28 19:48:47 +0900 (Thu, 28 Jun 2007) $
 */
public class FrequencyMethodCallBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "fmc";
    }

    public String getExtractorClassName(){
        return "jp.naist.se.stigmata.birthmarks.fmc.FrequencyMethodCallBirthmarkExtractor";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new FrequencyMethodCallBirthmarkExtractor(service);
    }
}