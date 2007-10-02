package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.io.Serializable;

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
class Names implements Serializable{
    private static final long serialVersionUID = 1911603124143509407L;
    private final String fullyName;
    private String className;
    private String packageName;

    public Names(final String fullyName){
        this.fullyName = fullyName.replace('/', '.');
    }

    public String getFullyName(){
        return fullyName;
    }

    public String getClassName(){
        if(className == null){
            generateClassAndPackageName();
        }
        return className;
    }

    public String getPackageName(){
        if(packageName == null){
            generateClassAndPackageName();
        }
        return packageName;
    }

    private void generateClassAndPackageName(){
        final int index = fullyName.lastIndexOf('.');
        this.className = fullyName.substring(index + 1);
        this.packageName = "";
        if(index > 0){
            packageName = fullyName.substring(0, index - 1);
        }
    }
}
