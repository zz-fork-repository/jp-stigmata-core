package jp.sourceforge.stigmata.birthmarks.extractors;

/*
 * $Id$
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.spi.AbstractServiceProvider;
import jp.sourceforge.stigmata.spi.BirthmarkExtractorSpi;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public abstract class AbstractBirthmarkExtractorService extends AbstractServiceProvider implements BirthmarkExtractorSpi{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public abstract String getType();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    @Override
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
            locale, getType(), LocalizedDescriptionManager.ServiceCategory.extractor
        );
    }

    /**
     * returns a localized description of the birthmark in default locale.
     */
    @Override
    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    @Override
    public abstract String getExtractorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkSpi service){
        try{
            Class<?> c = Class.forName(getExtractorClassName());
            Class<? extends BirthmarkExtractor> clazz = c.asSubclass(BirthmarkExtractor.class);
            Constructor<? extends BirthmarkExtractor> constructor = clazz.getConstructor(BirthmarkSpi.class);
            return constructor.newInstance(service);
        } catch(NoSuchMethodException e){
        } catch(InstantiationException e){
        } catch(InvocationTargetException e){
        } catch(ClassNotFoundException e){
        } catch(IllegalAccessException e){
        }
        return null;
    }
}

