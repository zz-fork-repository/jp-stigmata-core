package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.AbstractServiceProvider;
import jp.sourceforge.stigmata.spi.BirthmarkComparatorSpi;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * Abstract service provider interface for comparing birthmarks.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
abstract class AbstractBirthmarkComparatorService extends AbstractServiceProvider implements BirthmarkComparatorSpi{
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
            locale, getType(), LocalizedDescriptionManager.ServiceCategory.comparator
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
    public abstract String getComparatorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkSpi service){
        try{
            Class<?> c = Class.forName(getComparatorClassName());
            Class<? extends BirthmarkComparator> clazz = c.asSubclass(BirthmarkComparator.class);
            Constructor<? extends BirthmarkComparator> constructor = clazz.getConstructor(BirthmarkSpi.class);
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

