package jp.sourceforge.stigmata.birthmarks.extractors;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class InheritanceStructureBirthmarkExtractorService extends AbstractBirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType(){
        return "is";
    }

    public String getExtractorClassName(){
        return "jp.sourceforge.stigmata.birthmarks.is.InheritanceStructureBirthmarkExtractor";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        return new InheritanceStructureBirthmarkExtractor(service);
    }
}