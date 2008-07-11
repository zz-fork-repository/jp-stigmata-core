package jp.sourceforge.stigmata.birthmarks.extractors;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.cvfv.ConstantValueOfFieldVariableBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class ConstantValueOfFieldVariableBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "cvfv";
    }

    public String getExtractorClassName(){
        return ConstantValueOfFieldVariableBirthmarkExtractor.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new ConstantValueOfFieldVariableBirthmarkExtractor(service);
    }
}