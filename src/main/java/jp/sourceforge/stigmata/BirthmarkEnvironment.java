package jp.sourceforge.stigmata;

/*
 * $Id$
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.spi.ServiceRegistry;

import jp.sourceforge.stigmata.filter.ComparisonPairFilterManager;
import jp.sourceforge.stigmata.reader.ClasspathContext;
import jp.sourceforge.stigmata.result.history.ExtractedBirthmarkServiceManager;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.utils.WellknownClassManager;

/**
 * This class represents the context for extracting/comparing birthmarks.
 * 
 * @author  Haruaki TAMADA
 * @version  $Revision$
 */
public class BirthmarkEnvironment{
    /**
     * Default environment. All instance of this class is based on default environment.
     */
    private static BirthmarkEnvironment DEFAULT_ENVIRONMENT = new BirthmarkEnvironment(true);

    /**
     * home directory path.
     */
    private static String HOME_DIRECTORY_PATH;

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
     * history manager.
     */
    private ExtractedBirthmarkServiceManager historyManager;

    /**
     * 
     */
    private ClassLoader loader;

    /**
     * constructor for root environment
     */
    private BirthmarkEnvironment(boolean flag){
        manager = new WellknownClassManager();
        classpathContext = new ClasspathContext();
        filterManager = new ComparisonPairFilterManager(this);
        historyManager = new ExtractedBirthmarkServiceManager(this);
    }

    /**
     * constructor for specifying parent environment.
     */
    public BirthmarkEnvironment(BirthmarkEnvironment parent){
        this.parent = parent;
        this.manager = new WellknownClassManager(parent.getWellknownClassManager());
        this.classpathContext = new ClasspathContext(parent.getClasspathContext());
        this.filterManager = new ComparisonPairFilterManager(this, parent.getFilterManager());
        this.historyManager = new ExtractedBirthmarkServiceManager(this, parent.getHistoryManager());
    }

    /**
     * returns the default birthmark environment.
     */
    public static final BirthmarkEnvironment getDefaultEnvironment(){
        return DEFAULT_ENVIRONMENT;
    }

    public static synchronized final String getStigmataHome(){
        if(HOME_DIRECTORY_PATH == null){
            String stigmataHome = System.getProperty("stigmata.home");
            if(stigmataHome == null){
                stigmataHome = System.getenv("STIGMATA_HOME");
            }
            if(stigmataHome == null){
                String parent = System.getProperty("user.home");
                if(parent == null){
                    parent = System.getenv("HOME");
                }
                if(parent == null){
                    parent = ".";
                }
                // for windows
                if(parent.startsWith("C:\\Documents and Settings\\")){
                    stigmataHome = parent + File.separator + "Application Data" + File.separator + "stigmata";
                }
                else{
                    stigmataHome = parent + File.separator + ".stigmata";
                }
            }
            HOME_DIRECTORY_PATH = stigmataHome;
        }
        return HOME_DIRECTORY_PATH;
    }

    static void resetSettings(){
        DEFAULT_ENVIRONMENT = new BirthmarkEnvironment(false);
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
        boolean contains = properties.containsKey(key);
        String old = getProperty(key);
        properties.put(key, value);

        // value is updated?
        if(!((old != null && old.equals(value)) ||
             (contains && old == null && value == null))){
            firePropertyEvent(new PropertyChangeEvent(this, key, old, value));
        }
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

    public <T> Iterator<T> lookupProviders(Class<T> providerClass){
        Iterator<T> iterator;
        if(loader != null){
            iterator = ServiceRegistry.lookupProviders(providerClass, loader);
        }
        else{
            iterator = ServiceRegistry.lookupProviders(providerClass);
        }
        return iterator;
    }

    /**
     * return birthmark services lookup from current class path.
     */
    public synchronized BirthmarkSpi[] findServices(){
        List<BirthmarkSpi> list = getServiceList();

        for(Iterator<BirthmarkSpi> i = lookupProviders(BirthmarkSpi.class); i.hasNext(); ){
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

    public ComparisonPairFilterManager getFilterManager(){
        return filterManager;
    }

    public ExtractedBirthmarkServiceManager getHistoryManager(){
        return historyManager;
    }

    void setClassLoader(ClassLoader loader){
        this.loader = loader;
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
}
