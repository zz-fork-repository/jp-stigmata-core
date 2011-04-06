package jp.sourceforge.stigmata.birthmarks.extractors;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "is";
    }

    @Override
    public String getExtractorClassName(){
        return InheritanceStructureBirthmarkExtractor.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new InheritanceStructureBirthmarkExtractor(service);
    }
}