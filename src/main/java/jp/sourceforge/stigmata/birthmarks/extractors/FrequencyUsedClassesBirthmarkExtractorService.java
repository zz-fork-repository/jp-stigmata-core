package jp.sourceforge.stigmata.birthmarks.extractors;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.fuc.FrequencyUsedClassesBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class FrequencyUsedClassesBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "fuc";
    }

    @Override
    public String getExtractorClassName(){
        return FrequencyUsedClassesBirthmarkExtractor.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new FrequencyUsedClassesBirthmarkExtractor(service);
    }
}