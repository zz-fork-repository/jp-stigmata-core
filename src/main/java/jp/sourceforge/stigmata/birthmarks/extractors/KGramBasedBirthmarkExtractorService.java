package jp.sourceforge.stigmata.birthmarks.extractors;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.kgram.KGramBasedBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "kgram";
    }

    @Override
    public String getExtractorClassName(){
        return KGramBasedBirthmarkExtractor.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new KGramBasedBirthmarkExtractor(service);
    }
}