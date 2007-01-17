package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
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
}
