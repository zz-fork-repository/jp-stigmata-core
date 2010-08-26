package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.util.Locale;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * Abstract class for {@link BirthmarkSpi <code>BirthmarkSpi</code>}
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public abstract class AbstractBirthmarkService implements BirthmarkSpi{
    @Override
    public String getDisplayType(){
        return getDisplayType(Locale.getDefault());
    }

    @Override
    public String getDisplayType(Locale locale){
        LocalizedDescriptionManager manager = LocalizedDescriptionManager.getInstance();
        String type = manager.getDisplayType(locale, getType());
        if(type == null){
            type = getType();
        }
        return type;
    }

    @Override
    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    @Override
    public String getDescription(Locale locale){
        LocalizedDescriptionManager manager = LocalizedDescriptionManager.getInstance();
        String description = manager.getDescription(locale, getType());
        if(description == null){
            description = getDefaultDescription();
        }
        return description;
    }

    @Override
    public abstract BirthmarkComparator getComparator();

    @Override
    public String getComparatorClassName(){
        return getComparator().getClass().getName();
    }

    @Override
    public abstract BirthmarkExtractor getExtractor();

    @Override
    public String getExtractorClassName(){
        return getExtractor().getClass().getName();
    }

    @Override
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }

    @Override
    public String getPreprocessorClassName(){
        BirthmarkPreprocessor preprocessor = getPreprocessor();
        String name = null;
        if(preprocessor != null){
            name = preprocessor.getClass().getName();
        }
        return name;
    }

    @Override
    public abstract String getType();

    @Override
    public abstract String getDefaultDescription();

    @Override
    public boolean isExperimental(){
        return true;
    }

    @Override
    public boolean isUserDefined(){
        return true;
    }

    @Override
    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public String getVendorName(){
        return getClass().getPackage().getImplementationVendor();
    }

    @Override
    public Birthmark buildBirthmark(){
    	return getExtractor().createBirthmark();
    }

    @Override
    public BirthmarkElement buildBirthmarkElement(String value){
    	if(value == null || value.equals("<null>")){
    		return NullBirthmarkElement.getInstance();
    	}
		return new BirthmarkElement(value);
    }
}
