package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkSelectionPane extends javax.swing.JPanel {
    private static final long serialVersionUID = 3209854654743223453L;

    private StigmataFrame stigmata;
    private List<String> selectedServices = new ArrayList<String>();
    private List<BirthmarkSpi> serviceList = new ArrayList<BirthmarkSpi>();
    private List<JCheckBox> checks = new ArrayList<JCheckBox>();
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();

    public BirthmarkSelectionPane(StigmataFrame stigmata) {
        this.stigmata = stigmata;
        initComponents();
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

    public void reset(){
        selectedServices = new ArrayList<String>();
        for(JCheckBox check: checks){
            check.setSelected(true);
        }

        for(BirthmarkSpi service: serviceList){
            selectedServices.add(service.getType());
        }
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

    private void fireEvent(){
        for(DataChangeListener listener: listeners){
            listener.valueChanged(this);
        }
    }

    private void initComponents() {
        BirthmarkSpi[] services = stigmata.getContext().getServices();

        setLayout(new GridLayout(1, services.length));

        for(BirthmarkSpi service: services){
            serviceList.add(service);
            JCheckBox check = new JCheckBox(new BirthmarkSelectAction(service, this));
            check.setToolTipText(service.getDescription());
            add(check, BorderLayout.CENTER);
            checks.add(check);
        }
        reset();
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

