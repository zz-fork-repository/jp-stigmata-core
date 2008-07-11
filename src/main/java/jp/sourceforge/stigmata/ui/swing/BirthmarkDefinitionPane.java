package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.birthmarks.BirthmarkService;
import jp.sourceforge.stigmata.spi.BirthmarkComparatorSpi;
import jp.sourceforge.stigmata.spi.BirthmarkExtractorSpi;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class BirthmarkDefinitionPane extends JPanel{
    private static final long serialVersionUID = 3932637653297802978L;

    private StigmataFrame stigmata;
    private DefaultListModel model;
    private InformationPane information;
    private JList serviceList;
    private JButton newService;
    private JButton removeService;
    private List<BirthmarkSpi> addedService = new ArrayList<BirthmarkSpi>();
    private List<BirthmarkServiceListener> listeners = new ArrayList<BirthmarkServiceListener>();

    public BirthmarkDefinitionPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initData();

        updateView();
    }

    public void addBirthmarkServiceListener(BirthmarkServiceListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkServiceListener(BirthmarkServiceListener listener){
        listeners.remove(listener);
    }

    public void reset(){
        for(BirthmarkSpi service: addedService){
            model.removeElement(service);
        }
    }

    public void updateEnvironment(BirthmarkEnvironment environment){
        for(BirthmarkSpi service: addedService){
            if(environment.getService(service.getType()) == null){
                if(service instanceof BirthmarkService){
                    ((BirthmarkService)service).setBirthmarkEnvironment(environment);
                }
                environment.addService(service);
            }
        }
    }

    private void initData(){
        information.initData();
        model.addElement(stigmata.getMessages().get("newservice.definition.label"));

        for(BirthmarkSpi service: stigmata.getEnvironment().findServices()){
            model.addElement(service);
        }
    }

    private void initLayouts(){
        Messages messages = stigmata.getMessages();
        JPanel panel = new JPanel(new BorderLayout());
        serviceList = new JList(model = new DefaultListModel());
        serviceList.setCellRenderer(new BirthmarkServiceListCellRenderer(new Dimension(250, 20), 60));
        JScrollPane scroll = new JScrollPane(serviceList);

        scroll.setBorder(new TitledBorder(messages.get("servicelist.border")));
        serviceList.setToolTipText(messages.get("servicelist.tooltip"));

        panel.add(scroll, BorderLayout.WEST);
        panel.add(information = new InformationPane(stigmata, this), BorderLayout.CENTER);

        Box buttonPanel = Box.createHorizontalBox();
        newService = GUIUtility.createButton(messages, "newservice");
        removeService = GUIUtility.createButton(messages, "removeservice");
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(newService);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(removeService);
        buttonPanel.add(Box.createHorizontalGlue());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        removeService.setEnabled(false);

        serviceList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                listValueChanged(e);
            }
        });

        newService.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addNewService();
            }
        });

        removeService.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeService();
            }
        });
    }

    private void removeService(){
        int index = serviceList.getSelectedIndex();
        if(index > 0){
            BirthmarkSpi service = (BirthmarkSpi)model.getElementAt(index);
            if(service != null && service.isUserDefined()){
                model.remove(index);
                for(BirthmarkServiceListener listener: listeners){
                    listener.serviceRemoved(service);
                }
            }
        }
        stigmata.setNeedToSaveSettings(true);
        updateView();
    }

    private void addNewService(){
        BirthmarkService service = information.createService();
        model.addElement(service);
        addedService.add(service);

        for(BirthmarkServiceListener listener: listeners){
            listener.serviceAdded(service);
        }
        stigmata.setNeedToSaveSettings(true);
        updateView();
    }

    private void listValueChanged(ListSelectionEvent e){
        int index = serviceList.getSelectedIndex();
        if(index > 0){
            BirthmarkSpi service = (BirthmarkSpi)model.getElementAt(index);
            if(service != null){
                information.setService(service);
            }
        }
        else if(index == 0){
            information.clear();
        }
        updateView();
    }

    /**
     * remove: enabled when selected index is greater than 0 and selected service is defined by user.
     * new service: enabled when selected index is less equals than 0 or selected service is defined by user and information pane is show available service.
     *
     */
    private void updateView(){
        int index = serviceList.getSelectedIndex();
        ListModel model = serviceList.getModel();
        BirthmarkSpi service = null;
        if(index > 0){
            service = (BirthmarkSpi)model.getElementAt(index); 
        }
        newService.setEnabled(
            (index <= 0 || service.isUserDefined()) && 
            information.isAvailableService() &&
            isAvailableServiceName(information.getType())
        );
        removeService.setEnabled(index > 0 && service.isUserDefined());
        information.setEnabled(index <= 0 || service.isUserDefined());
    }

    private boolean isAvailableServiceName(String name){
        for(BirthmarkSpi service: addedService){
            if(service.getType().equals(name)){
                return false;
            }
        }
        return true;
    }

    private static class InformationPane extends JPanel{
        private static final long serialVersionUID = 37906542932362L;

        private StigmataFrame stigmata;
        private BirthmarkDefinitionPane thisPane;
        private JTextField type;
        private JTextField displayType;
        private JTextArea description;
        private JComboBox extractor;
        private JComboBox comparator;
        private JCheckBox expert;
        private JCheckBox userDefined;

        public InformationPane(StigmataFrame stigmata, BirthmarkDefinitionPane thisPane){
            this.stigmata = stigmata;
            this.thisPane = thisPane;
            initLayouts();
        }

        public String getType(){
            return type.getText();
        }

        public void setEnabled(boolean flag){
            super.setEnabled(flag);

            type.setEnabled(flag);
            displayType.setEnabled(flag);
            description.setEnabled(flag);
            extractor.setEnabled(flag);
            comparator.setEnabled(flag);
        }

        public BirthmarkService createService(){
            BirthmarkService service = new BirthmarkService();
            service.setType(type.getText());
            service.setDisplayType(displayType.getText());
            service.setDescription(description.getText());
            service.setExtractorClassName(extractor.getSelectedItem().toString());
            service.setComparatorClassName(comparator.getSelectedItem().toString());
            service.setUserDefined(true);

            return service;
        }

        public void clear(){
            type.setText("");
            displayType.setText("");
            description.setText("");
            extractor.getModel().setSelectedItem(null);
            comparator.getModel().setSelectedItem(null);
            userDefined.setSelected(true);
            expert.setSelected(true);
        }

        public boolean isAvailableService(){
            String newType = type.getText();
            Object selectedExtractor = extractor.getSelectedItem();
            String extractorClass = "";;
            if(selectedExtractor != null){
                extractorClass = selectedExtractor.toString();
            }
            Object selectedComparator = comparator.getSelectedItem();
            String comparatorClass = "";
            if(selectedComparator != null){
                comparatorClass = selectedComparator.toString();
            }
            BirthmarkEnvironment environment = stigmata.getEnvironment();

            boolean flag = newType.length() > 0
                    && displayType.getText().length() > 0
                    && extractorClass.length() > 0
                    && comparatorClass.length() > 0;

            // check inputed type is free
            flag = flag && environment.getService(newType) == null;

            // check extractor/comparator classes are available
            try{
                flag = flag
                    && environment.getClasspathContext().find(extractorClass) != null
                    && environment.getClasspathContext().find(comparatorClass) != null;
            } catch(ClassNotFoundException e){
                flag = false;
            }

            return flag;
        }

        public void setService(BirthmarkSpi service){
            type.setText(service.getType());
            displayType.setText(service.getDisplayType());
            description.setText(service.getDefaultDescription());
            selectComboBoxItem(extractor, service.getExtractorClassName());
            selectComboBoxItem(comparator, service.getComparatorClassName());
            userDefined.setSelected(service.isUserDefined());
            expert.setSelected(service.isExpert());

            setEnabled(service.isUserDefined());
        }

        public void initData(){
            comparator.addItem("");
            for(Iterator<BirthmarkComparatorSpi> i = stigmata.getEnvironment().lookupProviders(BirthmarkComparatorSpi.class); i.hasNext();){
                BirthmarkComparatorSpi service = i.next();
                comparator.addItem(service.getComparatorClassName());
            }
            extractor.addItem("");
            for(Iterator<BirthmarkExtractorSpi> i = stigmata.getEnvironment().lookupProviders(BirthmarkExtractorSpi.class); i.hasNext();){
                BirthmarkExtractorSpi service = i.next();
                extractor.addItem(service.getExtractorClassName());
            }
        }

        private void selectComboBoxItem(JComboBox box, String item){
            box.getModel().setSelectedItem(item);
        }

        private void initLayouts(){
            Messages messages = stigmata.getMessages();
            type = new JTextField();
            displayType = new JTextField();
            extractor = new JComboBox();
            comparator = new JComboBox();
            expert = new JCheckBox(messages.get("define.expert.label"));
            userDefined = new JCheckBox(messages.get("define.userdef.label"));
            description = new JTextArea();
            JScrollPane scroll = new JScrollPane(description);
            type.setColumns(10);
            displayType.setColumns(20);
            description.setColumns(40);
            description.setRows(10);

            Box box1 = Box.createHorizontalBox();
            box1.add(type);
            box1.add(displayType);

            Box box2 = Box.createHorizontalBox();
            box2.add(Box.createHorizontalGlue());
            box2.add(expert);
            box2.add(Box.createHorizontalGlue());
            box2.add(userDefined);
            box2.add(Box.createHorizontalGlue());

            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.add(box1);
            panel.add(extractor);
            panel.add(comparator);

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(panel);
            add(box2);
            add(scroll);
            add(Box.createVerticalGlue());

            GUIUtility.decorateJComponent(messages, type, "define.type");
            GUIUtility.decorateJComponent(messages, displayType, "define.displaytype");
            GUIUtility.decorateJComponent(messages, scroll, "define.description");
            GUIUtility.decorateJComponent(messages, extractor, "define.extractor");
            GUIUtility.decorateJComponent(messages, comparator, "define.comparator");
            GUIUtility.decorateJComponent(messages, expert, "define.expert");
            GUIUtility.decorateJComponent(messages, userDefined, "define.userdef");

            userDefined.setEnabled(false);
            expert.setEnabled(false);
            userDefined.setSelected(true);
            expert.setSelected(true);

            extractor.setEditable(true);
            comparator.setEditable(true);

            DocumentListener listener = new DocumentListener(){
                public void insertUpdate(DocumentEvent e){
                    thisPane.updateView();
                }

                public void removeUpdate(DocumentEvent e){
                    thisPane.updateView();
                }

                public void changedUpdate(DocumentEvent e){
                    thisPane.updateView();
                }
            };

            type.getDocument().addDocumentListener(listener);
            displayType.getDocument().addDocumentListener(listener);
            description.getDocument().addDocumentListener(listener);
            ItemListener itemListener = new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    thisPane.updateView();
                }
            };
            comparator.addItemListener(itemListener);
            extractor.addItemListener(itemListener);
            ActionListener actionListener = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    thisPane.updateView();
                }
            };
            comparator.getEditor().addActionListener(actionListener);
            extractor.getEditor().addActionListener(actionListener);
        }
    }
}
