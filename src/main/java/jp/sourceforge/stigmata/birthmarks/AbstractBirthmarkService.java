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
    public String getDisplayType(){
        return getDisplayType(Locale.getDefault());
    }

    public String getDisplayType(Locale locale){
        LocalizedDescriptionManager manager = LocalizedDescriptionManager.getInstance();
        String type = manager.getDisplayType(locale, getType());
        if(type == null){
            type = getType();
        }
        return type;
    }

    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    public String getDescription(Locale locale){
        LocalizedDescriptionManager manager = LocalizedDescriptionManager.getInstance();
        String description = manager.getDescription(locale, getType());
        if(description == null){
            description = getDefaultDescription();
        }
        return description;
    }

    public abstract BirthmarkComparator getComparator();

    public String getComparatorClassName(){
        return getComparator().getClass().getName();
    }

    public abstract BirthmarkExtractor getExtractor();

    public String getExtractorClassName(){
        return getExtractor().getClass().getName();
    }

    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }

    public String getPreprocessorClassName(){
        BirthmarkPreprocessor preprocessor = getPreprocessor();
        String name = null;
        if(preprocessor != null){
            name = preprocessor.getClass().getName();
        }
        return name;
    }

    public abstract String getType();

    public abstract String getDefaultDescription();

    public boolean isExperimental(){
        return true;
    }

    public boolean isUserDefined(){
        return true;
    }

    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

    public String getVendorName(){
        return getClass().getPackage().getImplementationVendor();
    }

    public Birthmark buildBirthmark(){
    	return getExtractor().createBirthmark();
    }

    public BirthmarkElement buildBirthmarkElement(String value){
    	if(value == null || value.equals("<null>")){
    		return NullBirthmarkElement.getInstance();
    	}
		return new BirthmarkElement(value);
    }
}
