package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class manages message for display.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class Messages{
    private static final String BUNDLE_NAME = "resources.messages";
    private static final Messages instance = new Messages();
    private final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages(){
    }

    public static String getString(String key){
        return instance.get(key);
    }

    public static String[] getStringArray(String key){
        return instance.getArray(key);
    }

    public static boolean hasString(String key){
        return instance.hasValue(key);
    }

    private String get(String key){
        try{
            String value = bundle.getString(key);
            int currentIndex = 0;

            // replace "hoge${fugakey}" to "hogefuga"
            // when fugakey=fuga was defined.
            while((value.indexOf('$', currentIndex) >= 0)){
                int index = value.indexOf('$', currentIndex);
                if(value.charAt(index + 1) == '$'){
                    currentIndex = index + 2;
                }
                else if(value.charAt(index + 1) == '{'){
                    int last = value.indexOf('}', currentIndex + 1);
                    String subkey = value.substring(index + 2, last);
                    if(hasValue(subkey)){
                        String subvalue = get(subkey);
                        StringBuilder builder = new StringBuilder();
                        builder.append(value.substring(0, currentIndex));
                        builder.append(subvalue);
                        builder.append(value.substring(last + 1));
                        currentIndex += subvalue.length() + 1;
                        value = new String(builder);
                    }
                }
            }

            return value;
        }catch(MissingResourceException e){
            e.printStackTrace();
            return "!" + key + "!";
        }
    }

    private String[] getArray(String key){
        String value = get(key);
        return value.split(", *");
    }

    private boolean hasValue(String key){
        try{
            bundle.getString(key);
            return true;
        }catch(MissingResourceException e){
        }
        return false;
    }
}
