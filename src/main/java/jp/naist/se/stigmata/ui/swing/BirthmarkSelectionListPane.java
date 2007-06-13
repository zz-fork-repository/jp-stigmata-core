package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkSelectionListPane extends BirthmarkSelectablePane implements BirthmarkServiceListener{
    private static final long serialVersionUID = 3209854654743223453L;

    private StigmataFrame stigmata;
    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();
    private Set<String> selectedServices = new HashSet<String>();
    private Map<String, BirthmarkSelection> services;
    private boolean expertmode = false;
    private DefaultListModel model;
    private JList list;

    public BirthmarkSelectionListPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initServices();

        stigmata.addBirthmarkServiceListener(this);
    }

    private void initLayouts(){
        setLayout(new BorderLayout());
        list = new JList(model = new DefaultListModel());
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
        list.setCellRenderer(new BirthmarkServiceListCellRenderer(new Dimension(250, 20), 60));
        list.setVisibleRowCount(5);
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
                    updateUI();
                    fireEvent();
                }
            }
        };
        checkAll.addActionListener(listener);
        uncheckAll.addActionListener(listener);
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(index);
                BirthmarkSelection elem = (BirthmarkSelection)model.getElementAt(index);
                elem.setSelected(!elem.isSelected());
                updateUI();
            }
        });
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

    public void setExpertMode(boolean expertmode){
        this.expertmode = expertmode;
        updateLayouts();
    }

    public boolean isExpertMode(){
        return expertmode;
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

    public String[] getSelectedServices(){
        return selectedServices.toArray(new String[selectedServices.size()]);
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
            model.removeElement(elem);
            selectedServices.remove(service);
            services.remove(service);
        }
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
        model.removeAllElements();

        for(String key: services.keySet()){
            BirthmarkSelection elem = services.get(key);
            if(elem.isVisible(isExpertMode())){
                model.addElement(elem);
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

    public static class Renderer extends JCheckBox implements ListCellRenderer{
        private static final long serialVersionUID = -324432943654654L;

        public Renderer(){
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean forcus){
            BirthmarkSelection elem = (BirthmarkSelection)value;
            setText(elem.getService().getDisplayType());
            setToolTipText(elem.getService().getDescription());
            setSelected(elem.isSelected());

            setBackground(isSelected ? SystemColor.textHighlight: Color.white);
            setForeground(isSelected ? SystemColor.textHighlightText: Color.black);

            return this;
        }
    };
}
