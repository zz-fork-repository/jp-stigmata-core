package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkSelectionPane extends JPanel{
    private static final long serialVersionUID = 3209854654743223453L;

    private StigmataFrame stigmata;
    private Set<String> selectedServices = new HashSet<String>();
    private List<BirthmarkSpi> serviceList;
    private Map<BirthmarkSpi, JCheckBox> checks;
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();
    private boolean geekmode = false;

    public BirthmarkSelectionPane(StigmataFrame stigmata) {
        this.stigmata = stigmata;
        initServices();
    }

    public void select(BirthmarkSpi service, boolean flag){
        if(flag){
            selectedServices.add(service.getType());
        }
        else{
            selectedServices.remove(service.getType());
        }
        fireEvent();
    }

    public void refresh(){
        initServices();
        updateLayouts();
    }

    public void setGeekMode(boolean geekmode){
        this.geekmode = geekmode;
        updateLayouts();
    }

    public boolean isGeekMode(){
        return geekmode;
    }

    public void reset(){
        selectedServices.clear();
        initServices();
        geekmode = false;
        updateLayouts();
        fireEvent();
    }

    public void addDataChangeListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public String[] getServices(){
        String[] services = new String[serviceList.size()];
        for(int i = 0; i < services.length; i++){
            BirthmarkSpi service = serviceList.get(i);
            services[i] = service.getType();
        }
        return services;
    }

    public String[] getSelectedServices(){
        String[] services = selectedServices.toArray(new String[selectedServices.size()]);
        return services;
    }

    public void updateService(){
        Map<BirthmarkSpi, JCheckBox> newChecks = new HashMap<BirthmarkSpi, JCheckBox>();
        BirthmarkSpi[] services = stigmata.getContext().getServices();
        for(BirthmarkSpi service: services){
            if(checks.get(service) == null){ // added service is found
                JCheckBox check = new JCheckBox(new BirthmarkSelectAction(service, this));
                check.setToolTipText(service.getDescription());
                check.setSelected(true);
                newChecks.put(service, check);
                selectedServices.add(service.getType());
                serviceList.add(service);
            }
            else{ // unchanged services
                newChecks.put(service, checks.get(service));
                checks.remove(service);
            }
        }
        for(BirthmarkSpi remainService: checks.keySet()){
            JCheckBox check = checks.get(remainService);
            selectedServices.remove(remainService.getType());
            serviceList.remove(remainService);
            remove(check);
        }
        this.checks = newChecks;
        updateLayouts();
    }

    private void fireEvent(){
        for(DataChangeListener listener: listeners){
            listener.valueChanged(this);
        }
    }

    private Dimension calculateDimension(){
        int rows = 1;
        int cols = 0;
        if(!isGeekMode()){
            for(BirthmarkSpi service: serviceList){
                if(!service.isExpert()){
                    cols++;
                }
            }
        }
        else{
            cols = serviceList.size();
        }

        if(cols > 4){
            rows = (cols / 3);
            cols = 3;
            if((cols % 3) != 0) rows++;
        }

        return new Dimension(cols, rows);
    }

    /**
     * update layouts and update selected birthmarks list.
     */
    private void updateLayouts(){
        removeAll();
        Dimension d = calculateDimension();
        setLayout(new GridLayout(d.height, d.width));

        for(BirthmarkSpi service: serviceList){
            JCheckBox check = checks.get(service);
            check.setVisible(true);
            if(!service.isExpert() || (isGeekMode() && service.isExpert())){
                add(check);
            }
            else{
                check.setVisible(false);
            }

            if(check.isSelected() && check.isVisible()){
                selectedServices.add(service.getType());
            }
            else{
                selectedServices.remove(service.getType());
            }
        }
        updateUI();
    }

    private void initServices(){
        BirthmarkSpi[] services = stigmata.getContext().getServices();

        serviceList = new ArrayList<BirthmarkSpi>();
        checks = new HashMap<BirthmarkSpi, JCheckBox>();
        for(BirthmarkSpi service: services){
            JCheckBox check = new JCheckBox(new BirthmarkSelectAction(service, this));
            check.setToolTipText(service.getDescription());
            check.setSelected(true);
            checks.put(service, check);
            serviceList.add(service);
        }
    }

    private static class BirthmarkSelectAction extends AbstractAction{
        private static final long serialVersionUID = 3209843547654234L;

        private BirthmarkSpi service;
        private BirthmarkSelectionPane birthmarks;

        /**
         * Creates a new instance of BirthmarkSelectAction
         */
        public BirthmarkSelectAction(BirthmarkSpi service, BirthmarkSelectionPane birthmarks){
            super(service.getDisplayType());
            this.service = service;
            this.birthmarks = birthmarks;
        }

        public void actionPerformed(ActionEvent e) {
            JCheckBox check = (JCheckBox)e.getSource();
            birthmarks.select(service, check.isSelected());
        }
    }
}
