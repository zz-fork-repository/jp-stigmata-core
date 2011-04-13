package jp.sourceforge.stigmata.birthmarks.cvfv;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkExtractorService;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class ConstantValueOfFieldVariableBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "cvfv";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new ConstantValueOfFieldVariableBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Constant Value and Field Variable birthmark";
    }
}