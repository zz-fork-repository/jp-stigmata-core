package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.WellknownClassManager;

/**
 * This class represents the context for extracting/comparing birthmarks.
 * 
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class BirthmarkEnvironment{
    /**
     * Default environment. All instance of this class is based on default environment.
     */
    private static BirthmarkEnvironment DEFAULT_ENVIRONMENT = new BirthmarkEnvironment(true);

    /**
     * parent of this environment.
     */
    private BirthmarkEnvironment parent;

    /**
     * context for classpath.
     */
    private ClasspathContext classpathContext;

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
     * listeners for updating properties.
     */
    private List<PropertyChangeListener> propertyListeners = new ArrayList<PropertyChangeListener>();

    /**
     * filter manager.
     */
    private ComparisonPairFilterManager filterManager;

    /**
     * @deprecated moved to BirthmarkContext
     */
    private ExtractionUnit unit;

    /**
     * constructor for root environment
     */
    private BirthmarkEnvironment(boolean flag){
        manager = new WellknownClassManager();
        classpathContext = ClasspathContext.getDefaultContext();
        filterManager = new ComparisonPairFilterManager();
    }

    /**
     * default constructor. The instance constructed by this constructor has
     * default environment as the parent.
     */
    public BirthmarkEnvironment(){
        this(getDefaultEnvironment());
    }

    /**
     * constructor for specifying parent environment.
     */
    public BirthmarkEnvironment(BirthmarkEnvironment parent){
        this.parent = parent;
        this.manager = new WellknownClassManager(parent.getWellknownClassManager());
        this.classpathContext = new ClasspathContext(parent.getClasspathContext());
        this.filterManager = new ComparisonPairFilterManager(parent.getFilterManager());
    }

    /**
     * returns the default birthmark environment.
     */
    public static final BirthmarkEnvironment getDefaultEnvironment(){
        return DEFAULT_ENVIRONMENT;
    }

    public BirthmarkEnvironment getParent(){
        return parent;
    }

    /**
     * remove property mapped given key.
     */
    public void removeProperty(String key){
        String old = properties.get(key);
        properties.remove(key);
        firePropertyEvent(new PropertyChangeEvent(this, key, old, null));
    }

    /**
     * add given property.
     */
    public void addProperty(String key, String value){
        String old = getProperty(key);
        properties.put(key, value);
        firePropertyEvent(new PropertyChangeEvent(this, key, old, value));
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

    /**
     * fire property change event to listeners.
     * @param e Event object.
     */
    private void firePropertyEvent(PropertyChangeEvent e){
        for(PropertyChangeListener listener: propertyListeners){
            listener.propertyChange(e);
        }
    }

    /**
     * add listener for updating properties.
     */
    public void addPropertyListener(PropertyChangeListener listener){
        propertyListeners.add(listener);
    }

    /**
     * remove specified listener.
     */
    public void removePropertyListener(PropertyChangeListener listener){
        propertyListeners.remove(listener);
    }

    public void clearProperties(){
        properties.clear();
    }

    public Iterator<String> propertyKeys(){
        Set<String> set = new HashSet<String>();
        if(parent != null){
            for(Iterator<String> i = parent.propertyKeys(); i.hasNext(); ){
                set.add(i.next());
            }
        }
        set.addAll(properties.keySet());
        return set.iterator();
    }

    /**
     * returns the classpath context.
     */
    public ClasspathContext getClasspathContext(){
        return classpathContext;
    }

    /**
     * add given birthmark service to this environment.
     */
    public synchronized void addService(BirthmarkSpi service){
        if(parent == null || parent.getService(service.getType()) == null){
            services.put(service.getType(), service);
        }
    }

    /**
     * remove given birthmark service from this environment.
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
     * return all birthmark services searching traverse to root environment.
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
     * find the all birthmark services searching to root environment.
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

    public ComparisonPairFilterManager getFilterManager(){
        return filterManager;
    }

    public void setExtractionUnit(ExtractionUnit unit){
        this.unit = unit;
    }

    public ExtractionUnit getExtractionUnit(){
        return unit;
    }
}
