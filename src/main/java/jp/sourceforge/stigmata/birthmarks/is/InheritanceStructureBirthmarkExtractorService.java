package jp.sourceforge.stigmata.birthmarks.is;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkExtractorService;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "is";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new InheritanceStructureBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Inheritance Structure birthmark";
    }
}