package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.WellknownClassManager;

/**
 * This class represents the context for extracting/comparing birthmarks.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class BirthmarkContext{
    /**
     * Default context. All instance of this class is based on default context.
     */
    private static BirthmarkContext DEFAULT_CONTEXT = new BirthmarkContext(true);

    /**
     * parent of this context.
     */
    private BirthmarkContext parent;

    /**
     * context for classpath.
     */
    private ClasspathContext bytecodeContext;

    /**
     * wellknown class manager. This object judge a class is user made class or
     * wellknown class.
     */
    private WellknownClassManager manager;

    /**
     * collection of services.
     */
    private Map<String, BirthmarkSpi> services = new HashMap<String, BirthmarkSpi>();

    /**
     * properties manager.
     */
    private Map<String, String> properties = new HashMap<String, String>();

    /**
     * constructor for root context
     */
    private BirthmarkContext(boolean flag){
        manager = new WellknownClassManager();
        bytecodeContext = ClasspathContext.getDefaultContext();
    }

    /**
     * default constructor. The instance constructed by this constructor has
     * default context as the parent.
     */
    public BirthmarkContext(){
        this(getDefaultContext());
    }

    /**
     * constructor for specifying parent context.
     */
    public BirthmarkContext(BirthmarkContext parent){
        this.parent = parent;
        this.manager = new WellknownClassManager(parent.getWellknownClassManager());
        this.bytecodeContext = new ClasspathContext(parent.getBytecodeContext());
    }

    /**
     * returns the default birthmark context.
     */
    public static final BirthmarkContext getDefaultContext(){
        return DEFAULT_CONTEXT;
    }

    /**
     * remove property mapped given key.
     */
    public void removeProperty(String key){
        properties.remove(key);
    }

    /**
     * add given property.
     */
    public void addProperty(String key, String value){
        properties.put(key, value);
    }

    /**
     * returns the property mapped given key
     */
    public String getProperty(String key){
        String value = properties.get(key);
        if(value == null && parent != null){
            value = parent.getProperty(key);
        }
        return value;
    }

    public void clearProperties(){
        properties.clear();
    }

    public Iterator<String> propertyKeys(){
        return properties.keySet().iterator();
    }

    /**
     * returns the classpath context.
     */
    public ClasspathContext getBytecodeContext(){
        return bytecodeContext;
    }

    /**
     * add given birthmark service to this context.
     */
    public synchronized void addService(BirthmarkSpi service){
        if(parent == null || parent.getService(service.getType()) == null){
            services.put(service.getType(), service);
        }
    }

    /**
     * remove given birthmark service from this context.
     */
    public void removeService(String type){
        services.remove(type);
    }

    /**
     * return birthmark service registered with given birthmark type.
     */
    public BirthmarkSpi getService(String type){
        BirthmarkSpi service = services.get(type);
        if(service == null && parent != null){
            service = parent.getService(type);
        }
        return service;
    }

    /**
     * return all birthmark services searching traverse to root context.
     * @uml.property  name="services"
     */
    public synchronized BirthmarkSpi[] getServices(){
        List<BirthmarkSpi> list = getServiceList();
        BirthmarkSpi[] services = list.toArray(new BirthmarkSpi[list.size()]);
        Arrays.sort(services, new BirthmarkSpiComparator());

        return services;
    }

    /**
     * return birthmark services lookup from current class path.
     */
    public synchronized BirthmarkSpi[] findServices(){
        List<BirthmarkSpi> list = getServiceList();

        for(Iterator<BirthmarkSpi> i = ServiceRegistry.lookupProviders(BirthmarkSpi.class); i.hasNext();){
            BirthmarkSpi spi = i.next();
            if(getService(spi.getType()) == null){
                list.add(spi);
            }
        }
        BirthmarkSpi[] services = list.toArray(new BirthmarkSpi[list.size()]);
        Arrays.sort(services, new BirthmarkSpiComparator());

        return services;
    }

    /**
     * return wellknown class manager.
     */
    public WellknownClassManager getWellknownClassManager(){
        return manager;
    }

    /**
     * find the all birthmark services searching to root context.
     */
    private List<BirthmarkSpi> getServiceList(){
        List<BirthmarkSpi> list = new ArrayList<BirthmarkSpi>();
        if(parent != null){
            for(BirthmarkSpi spi : parent.getServices()){
                list.add(spi);
            }
        }
        for(String key : services.keySet()){
            list.add(services.get(key));
        }
        return list;
    }
}
