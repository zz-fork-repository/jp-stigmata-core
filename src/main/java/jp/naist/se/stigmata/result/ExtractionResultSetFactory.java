package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ExtractionResultSet;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class ExtractionResultSetFactory{
    private static ExtractionResultSetFactory instance = new ExtractionResultSetFactory();
    private Map<String, Class<? extends ExtractionResultSet>> map = new HashMap<String, Class<? extends ExtractionResultSet>>();

    private ExtractionResultSetFactory(){
        map.put(BirthmarkStoreTarget.MEMORY.name(),        MemoryExtractionResultSet.class);
        map.put(BirthmarkStoreTarget.XMLFILE.name(),       XmlFileExtractionResultSet.class);
        map.put(BirthmarkStoreTarget.MEMORY_SINGLE.name(), SingleExtractionResultSet.class);
    }

    public static ExtractionResultSetFactory getInstance(){
        return instance;
    }

    public ExtractionResultSet createResultSet(BirthmarkContext context){
        return createResultSet(context.getStoreTarget(), context);
    }

    public ExtractionResultSet createResultSet(BirthmarkStoreTarget store, BirthmarkContext context){
        try{
            Class<? extends ExtractionResultSet> clazz = map.get(store.name());
            Constructor<? extends ExtractionResultSet> constructor = clazz.getConstructor(BirthmarkContext.class);
            return constructor.newInstance(context);

        }catch(NoSuchMethodException e){
        }catch(IllegalArgumentException e){
        }catch(InstantiationException e){
        }catch(IllegalAccessException e){
        }catch(InvocationTargetException e){
        }
        return null;
    }
}
