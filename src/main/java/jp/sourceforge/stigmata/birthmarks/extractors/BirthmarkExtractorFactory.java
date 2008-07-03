package jp.sourceforge.stigmata.birthmarks.extractors;

/*
 * $Id$
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.ExtractorNotFoundException;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractorFactory{
    private BirthmarkEnvironment environment;
    private Map<String, BirthmarkExtractor> extractors = new HashMap<String, BirthmarkExtractor>();

    public BirthmarkExtractorFactory(BirthmarkEnvironment env){
        this.environment = env;
    }

    public BirthmarkExtractor getExtractor(String name) throws ExtractorNotFoundException{
        BirthmarkExtractor extractor = extractors.get(name);
        if(extractor == null){
            extractor = buildExtractor(name);
            extractors.put(name, extractor);
        }
        return extractor;
    }

    @SuppressWarnings("unchecked")
    private BirthmarkExtractor buildExtractor(String birthmarkType) throws ExtractorNotFoundException{
        BirthmarkSpi spi = environment.getService(birthmarkType);
        BirthmarkExtractor extractor = null;
        if(spi != null){
            extractor = spi.getExtractor();
            try{
                if(extractor != null){
                    Map props = BeanUtils.describe(extractor);
                    props.remove("class");
                    props.remove("provider");
                    for(Object keyObject: props.keySet()){
                        String key = "extractor." + spi.getType() + "." + String.valueOf(keyObject);
                        if(environment.getProperty(key) != null){
                            BeanUtils.setProperty(
                                extractor, (String)keyObject, environment.getProperty(key)
                            );
                        }
                    }
                }
            } catch(InvocationTargetException e){
                throw new InternalError(e.getMessage());
            } catch(NoSuchMethodException e){
                throw new InternalError(e.getMessage());
            } catch(IllegalAccessException e){
                throw new InternalError(e.getMessage());
            }
        }
        if(extractor == null){
            throw new ExtractorNotFoundException("extractor not found: " + birthmarkType);
        }

        return extractor;
    }
}
