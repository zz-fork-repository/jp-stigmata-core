package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.util.Locale;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * Abstract class for {@link BirthmarkSpi <code>BirthmarkSpi</code>}
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
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

    public String getComparatorClassName(){
        return getComparator().getClass().getName();
    }

    public String getExtractorClassName(){
        return getExtractor().getClass().getName();
    }

    public abstract String getType();

    public abstract String getDefaultDescription();

    public boolean isExpert(){
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
