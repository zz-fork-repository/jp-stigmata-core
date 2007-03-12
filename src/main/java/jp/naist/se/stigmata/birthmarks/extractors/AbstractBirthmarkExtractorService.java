package jp.naist.se.stigmata.birthmarks.extractors;

/*
 * $Id: BirthmarkSpi.java 20 2007-01-17 02:06:01Z tama3 $
 */

import java.lang.reflect.Constructor;
import java.util.Locale;

import jp.naist.se.stigmata.BirthmarkExtractor;
import jp.naist.se.stigmata.spi.AbstractServiceProvider;
import jp.naist.se.stigmata.spi.BirthmarkExtractorSpi;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.LocalizedDescriptionManager;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 20 $ $Date: 2007-01-17 11:06:01 +0900 (Wed, 17 Jan 2007) $
 */
abstract class AbstractBirthmarkExtractorService extends AbstractServiceProvider implements BirthmarkExtractorSpi{
    /**
     * returns a type of the birthmark this service provides.
     */
    public abstract String getType();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
            locale, getType(), LocalizedDescriptionManager.ServiceCategory.extractor
        );
    }

    /**
     * returns a localized description of the birthmark in default locale.
     */
    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    public abstract String getExtractorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        try{
            Class<?> c = Class.forName(getExtractorClassName());
            Class<? extends BirthmarkExtractor> clazz = c.asSubclass(BirthmarkExtractor.class);
            Constructor<? extends BirthmarkExtractor> constructor = clazz.getConstructor(BirthmarkSpi.class);
            return constructor.newInstance(service);
        } catch(Exception e){
        }
        return null;
    }
}

