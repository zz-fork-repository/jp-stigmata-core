package jp.naist.se.stigmata.birthmarks.extractors;

/*
 * $Id: BirthmarkSpi.java 20 2007-01-17 02:06:01Z tama3 $
 */

import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.birthmarks.kgram.KGramBasedBirthmarkExtractor;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 20 $ $Date: 2007-01-17 11:06:01 +0900 (Wed, 17 Jan 2007) $
 */
public class KGramBasedBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "kgram";
    }

    public String getExtractorClassName(){
        return "jp.naist.se.stigmata.birthmarks.kgram.KGramBasedBirthmarkExtractor";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new KGramBasedBirthmarkExtractor(service);
    }
}