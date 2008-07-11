package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import java.lang.reflect.Constructor;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * Service provider interface for birthmarks which are defined in
 * configuration files.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class BirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private Class<? extends BirthmarkExtractor> extractorClass;
    private Class<? extends BirthmarkComparator> comparatorClass;
    private String type;
    private String displayType;
    private String description;
    private BirthmarkExtractor extractorObject;
    private BirthmarkComparator comparatorObject;
    private boolean userDefined = true;
    private BirthmarkEnvironment environment;

    public BirthmarkService(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public BirthmarkService(){
    }

    public void setBirthmarkEnvironment(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public void setExtractorClassName(String extractor){
        try{
            Class<?> c;
            if(environment == null){
                c = Class.forName(extractor);
            }
            else{
                c = environment.getClasspathContext().findClass(extractor);
            }
            extractorClass = c.asSubclass(BirthmarkExtractor.class);
            extractorObject = null;
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setComparatorClassName(String comparator){
        try{
            Class<?> c;
            if(environment == null){
                c = Class.forName(comparator);
            }
            else{
                c = environment.getClasspathContext().findClass(comparator);
            }
            comparatorClass = c.asSubclass(BirthmarkComparator.class);
            comparatorObject = null;
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setType(String type){
        this.type = type;
    }

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return type;
    }

    public void setDisplayType(String displayType){
        this.displayType = displayType;
    }

    public String getDisplayType(){
        return displayType;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        String desc = description;
        if(description == null){
            desc = "";
        }
        return desc;
    }

    /**
     * returns a description of the birthmark this service provides.
     */
    @Override
    public String getDefaultDescription(){
        return description;
    }

    public String getExtractorClassName(){
        return extractorClass.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(){
        if(extractorObject == null){
            try{
                Constructor<? extends BirthmarkExtractor> c = extractorClass.getConstructor(BirthmarkSpi.class);
                extractorObject = c.newInstance(this);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return extractorObject;
    }

    public String getComparatorClassName(){
        return comparatorClass.getName();
    }

    /**
     * returns a comparator for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(){
        if(comparatorObject == null){
            try{
                Constructor<? extends BirthmarkComparator> c = comparatorClass.getConstructor(BirthmarkSpi.class);
                comparatorObject = c.newInstance(this);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return comparatorObject;
    }

    public boolean isUserDefined(){
        return userDefined;
    }

    public void setUserDefined(boolean userDefined){
        this.userDefined = userDefined;
    }
}

