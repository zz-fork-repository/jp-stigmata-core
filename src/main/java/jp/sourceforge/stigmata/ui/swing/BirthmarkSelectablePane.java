package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public abstract class BirthmarkSelectablePane extends JPanel implements BirthmarkServiceListener{
	private static final long serialVersionUID = 7057130952947891635L;

	private StigmataFrame stigmata;
    private Set<String> selectedServices = new HashSet<String>();
    private Map<String, BirthmarkSelection> services;
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();
    private boolean expertmode;

    public BirthmarkSelectablePane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initServices();
        stigmata.addBirthmarkServiceListener(this);
    }

    protected abstract void updateLayouts();

    public Messages getMessages(){
        return stigmata.getMessages();
    }

    public void setExpertMode(boolean expertmode){
        this.expertmode = expertmode;
        updateLayouts();
    }

    public boolean isExpertMode(){
        return expertmode;
    }

    public String[] getSelectedServiceTypes(){
        return selectedServices.toArray(new String[selectedServices.size()]);
    }

    public BirthmarkSpi getService(String type){
        BirthmarkSelection elem = services.get(type);
        if(elem != null){
            return elem.getService();
        }
    
        return null;
    }

    public String[] getServiceTypes(){
        String[] serviceArray = new String[services.size()];
        int index = 0;
        for(String key: services.keySet()){
            BirthmarkSpi service = services.get(key).getService();
            serviceArray[index] = service.getType();
            index++;
        }
        return serviceArray;
    }

    public boolean hasService(String type){
        return services.get(type) != null;
    }

    public void select(String type, boolean flag){
        if(flag){
            selectedServices.add(type);
        }
        else{
            selectedServices.remove(type);
        }
        fireEvent();
    }

    public void select(BirthmarkSpi service, boolean flag){
        select(service.getType(), flag);
    }

    public void reset(){
        selectedServices.clear();
        initServices();
        expertmode = false;
        updateLayouts();
        fireEvent();
    }

    public void addDataChangeListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public void serviceAdded(BirthmarkSpi service){
        if(services.get(service.getType()) == null){
            BirthmarkSelection elem = new BirthmarkSelection(service);
            selectedServices.add(service.getType());
            services.put(service.getType(), elem);
        }
        updateLayouts();
        fireEvent();
    }

    public void serviceRemoved(BirthmarkSpi service){
        BirthmarkSelection elem = services.get(service);
        if(elem != null){
            selectedServices.remove(service);
            services.remove(service);
        }
        fireEvent();
    }

    public Iterator<String> serviceNames(){
        return services.keySet().iterator();
    }

    protected BirthmarkSelection getSelection(String type){
        return services.get(type);
    }

    protected Iterator<BirthmarkSelection> birthmarkSelections(){
        return services.values().iterator();
    }

    protected void fireEvent(){
        for(DataChangeListener listener: listeners){
            listener.valueChanged(this);
        }
    }

    private void initServices(){
        BirthmarkSpi[] serviceArray = stigmata.getEnvironment().getServices();

        services = new LinkedHashMap<String, BirthmarkSelection>();
        for(BirthmarkSpi service: serviceArray){
            BirthmarkSelection elem = new BirthmarkSelection(service);
            services.put(service.getType(), elem);
        }
    }
}