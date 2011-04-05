package jp.sourceforge.stigmata.birthmarks.extractors;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.fmc.FrequencyMethodCallBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class FrequencyMethodCallBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "fmc";
    }

    @Override
    public String getExtractorClassName(){
        return FrequencyMethodCallBirthmarkExtractor.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new FrequencyMethodCallBirthmarkExtractor(service);
    }
}