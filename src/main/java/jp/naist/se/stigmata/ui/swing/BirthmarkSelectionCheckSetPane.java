package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkSelectionCheckSetPane extends BirthmarkSelectablePane implements BirthmarkServiceListener{
    private static final long serialVersionUID = 3209854654743223453L;

    private StigmataFrame stigmata;
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();
    private Set<String> selectedServices = new HashSet<String>();
    private Map<String, BirthmarkSelection> services;
    private boolean expertmode = false;
    private JPanel checks = new JPanel();

    public BirthmarkSelectionCheckSetPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initServices();
    }

    private void initLayouts(){
        setLayout(new BorderLayout());
        add(checks, BorderLayout.CENTER);

        JButton checkAll = Utility.createButton("checkall");
        JButton uncheckAll = Utility.createButton("uncheckall");

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(checkAll);
        box.add(Box.createHorizontalGlue());        
        box.add(uncheckAll);
        box.add(Box.createHorizontalGlue());
        add(box, BorderLayout.SOUTH);

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                boolean flag = e.getActionCommand().equals("checkall");
                for(String key: services.keySet()){
                    BirthmarkSelection le = services.get(key);
                    le.setSelected(flag);
                    updateLayouts();
                    fireEvent();
                }
            }
        };
        checkAll.addActionListener(listener);
        uncheckAll.addActionListener(listener);
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

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#setExpertMode(boolean)
     */
    public void setExpertMode(boolean expertmode){
        this.expertmode = expertmode;
        updateLayouts();
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#isExpertMode()
     */
    public boolean isExpertMode(){
        return expertmode;
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#reset()
     */
    public void reset(){
        selectedServices.clear();
        initServices();
        expertmode = false;
        updateLayouts();
        fireEvent();
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#addDataChangeListener(jp.naist.se.stigmata.ui.swing.DataChangeListener)
     */
    public void addDataChangeListener(DataChangeListener listener){
        listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#getServices()
     */
    public String[] getServices(){
        String[] serviceArray = new String[services.size()];
        int index = 0;
        for(String key: services.keySet()){
            BirthmarkSpi service = services.get(key).getService();
            serviceArray[index] = service.getType();
            index++;
        }
        return serviceArray;
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#getSelectedServices()
     */
    public String[] getSelectedServices(){
        return selectedServices.toArray(new String[selectedServices.size()]);
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#addService(jp.naist.se.stigmata.spi.BirthmarkSpi)
     */
    public void serviceAdded(BirthmarkSpi service){
        if(services.get(service.getType()) == null){
            BirthmarkSelection elem = new BirthmarkSelection(service);
            selectedServices.add(service.getType());
            services.put(service.getType(), elem);
        }
        updateLayouts();
        fireEvent();
    }

    /* (non-Javadoc)
     * @see jp.naist.se.stigmata.ui.swing.BirthmarkSelectable#getService(java.lang.String)
     */
    public BirthmarkSpi getService(String type){
        BirthmarkSelection elem = services.get(type);
        if(elem != null){
            return elem.getService();
        }

        return null;
    }

    public boolean hasService(String type){
        return services.get(type) != null;
    }

    public void serviceRemoved(BirthmarkSpi service){
        BirthmarkSelection elem = services.get(service);
        if(elem != null){
            selectedServices.remove(service);
            services.remove(service);
        }
        updateLayouts();
        fireEvent();
    }

    private void fireEvent(){
        for(DataChangeListener listener: listeners){
            listener.valueChanged(this);
        }
    }

    /**
     * update layouts and update selected birthmarks list.
     */
    private void updateLayouts(){
        checks.removeAll();
        Dimension d = calculateDimension();
        checks.setLayout(new GridLayout(d.height, d.width));

        for(String key: services.keySet()){
            final BirthmarkSelection elem = services.get(key);
            if(elem.isVisible(isExpertMode())){
                JCheckBox check = new JCheckBox(elem.getService().getDisplayType());
                check.setSelected(elem.isSelected());
                check.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JCheckBox c = (JCheckBox)e.getSource();
                        if(c.isSelected()) selectedServices.add(elem.getService().getType());
                        else               selectedServices.remove(elem.getService().getType());
                        fireEvent();
                    }
                });
                checks.add(check);
            }

            if(elem.isVisible(isExpertMode()) && elem.isSelected()){ 
                selectedServices.add(elem.getType());
            }
            else{
                selectedServices.remove(elem.getType());
            }
        }
        updateUI();
    }

    private void initServices(){
        BirthmarkSpi[] serviceArray = stigmata.getContext().getServices();

        services = new LinkedHashMap<String, BirthmarkSelection>();
        for(BirthmarkSpi service: serviceArray){
            BirthmarkSelection elem = new BirthmarkSelection(service);
            services.put(service.getType(), elem);
        }
    }

    private Dimension calculateDimension(){
        int rows = 1;
        int cols = 0;
        for(String key: services.keySet()){
            BirthmarkSelection selection = services.get(key);
            if(selection.isVisible(isExpertMode())){
                cols++;
            }
        }

        if(cols > 4){
            rows = (cols / 3);
            if((cols % 3) != 0) rows++;
            cols = 3;
        }

        return new Dimension(cols, rows);
    }
}
