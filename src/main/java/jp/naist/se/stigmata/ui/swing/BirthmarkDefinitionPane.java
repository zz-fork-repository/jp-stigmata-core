package jp.naist.se.stigmata.ui.swing;

/*
 * $Id: BirthmarkSelectionPane.java 24 2007-01-30 15:08:43Z tama3 $
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;
import javax.swing.Box;
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

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.birthmarks.BirthmarkService;
import jp.naist.se.stigmata.spi.BirthmarkComparatorSpi;
import jp.naist.se.stigmata.spi.BirthmarkExtractorSpi;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 24 $ $Date: 2007-01-31 00:08:43 +0900 (Wed, 31 Jan 2007) $
 */
public class BirthmarkDefinitionPane extends JPanel{
    private static final long serialVersionUID = 3932637653297802978L;

    private StigmataFrame stigmata;
    private JList serviceList;
    private DefaultListModel model;
    private InformationPane information;
    private JButton newService;
    private JButton removeService;
    private Map<String, BirthmarkSpi> services = new HashMap<String, BirthmarkSpi>();
    private List<BirthmarkSpi> addedService = new ArrayList<BirthmarkSpi>();

    public BirthmarkDefinitionPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initData();

        updateView();
    }

    public void reset(){
        BirthmarkContext context = stigmata.getContext();
        for(BirthmarkSpi service: addedService){
            context.removeService(service.getType());
            model.removeElement(service.getDisplayType());
        }
    }

    private void initData(){
        information.initData();
        model.addElement(Messages.getString("newservice.definition.label"));

        for(BirthmarkSpi service: stigmata.getContext().findServices()){
            model.addElement(service.getDisplayType());
            services.put(service.getDisplayType(), service);
        }
    }

    private void initLayouts(){
        JPanel panel = new JPanel(new BorderLayout());
        serviceList = new JList(model = new DefaultListModel());
        JScrollPane scroll = new JScrollPane(serviceList);

        scroll.setBorder(new TitledBorder(Messages.getString("servicelist.border")));
        serviceList.setToolTipText(Messages.getString("servicelist.tooltip"));

        panel.add(information = new InformationPane(stigmata, this), BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newService = Utility.createButton("newservice");
        removeService = Utility.createButton("removeservice");
        buttonPanel.add(newService);
        buttonPanel.add(removeService);

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

    private BirthmarkSpi getService(String displayType){
        return services.get(displayType);
    }

    private void removeService(){
        int index = serviceList.getSelectedIndex();
        if(index > 0){
            String label = (String)model.getElementAt(index);
            BirthmarkSpi service = services.get(label);
            if(service != null && service.isUserDefined()){
                stigmata.getContext().removeService(service.getType());
                model.remove(index);
            }
        }
        stigmata.updateService();
        updateView();
    }

    private void addNewService(){
        BirthmarkService service = information.createService();
        stigmata.getContext().addService(service);
        services.put(service.getDisplayType(), service);
        model.addElement(service.getDisplayType());
        addedService.add(service);

        stigmata.updateService();
        updateView();
    }

    private void listValueChanged(ListSelectionEvent e){
        int index = serviceList.getSelectedIndex();
        if(index > 0){
            String label = (String)model.getElementAt(index);
            if(label != null){
                information.setService(services.get(label));
            }
        }
        else if(index == 0){
            information.clear();
        }
        updateView();
    }

    private void updateView(){
        int index = serviceList.getSelectedIndex();
        ListModel model = serviceList.getModel();
        BirthmarkSpi service = null;
        if(index > 0){
            service = services.get(model.getElementAt(index)); 
        }
        newService.setEnabled(
            (index <= 0 || service.isUserDefined()) && 
            information.isAvailableService()
        );
        removeService.setEnabled(index > 0 && service.isUserDefined());
        information.setEnabled(index <= 0 || service.isUserDefined());
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

        public void setEnabled(boolean flag){
            super.setEnabled(flag);

            type.setEnabled(flag);
            displayType.setEnabled(flag);
            description.setEnabled(flag);
            extractor.setEnabled(flag);
            comparator.setEnabled(flag);
        }

        public BirthmarkService createService(){
            BirthmarkService service = new BirthmarkService(stigmata.getContext());
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
            BirthmarkContext context = stigmata.getContext();

            boolean flag = newType.length() > 0
                    && displayType.getText().length() > 0
                    && extractorClass.length() > 0
                    && comparatorClass.length() > 0;

            // check inputed type is free
            flag = flag && context.getService(newType) == null;
            // check display type is free
            flag = flag && thisPane.getService(displayType.getText()) == null;

            // check extractor/comparator classes are available
            try{
                flag = flag
                    && context.getBytecodeContext().find(extractorClass) != null
                    && context.getBytecodeContext().find(comparatorClass) != null;
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
            for(Iterator<BirthmarkComparatorSpi> i = ServiceRegistry.lookupProviders(BirthmarkComparatorSpi.class); i.hasNext();){
                BirthmarkComparatorSpi service = i.next();
                comparator.addItem(service.getComparatorClassName());
            }
            extractor.addItem("");
            for(Iterator<BirthmarkExtractorSpi> i = ServiceRegistry.lookupProviders(BirthmarkExtractorSpi.class); i.hasNext();){
                BirthmarkExtractorSpi service = i.next();
                extractor.addItem(service.getExtractorClassName());
            }
        }

        private void selectComboBoxItem(JComboBox box, String item){
            box.getModel().setSelectedItem(item);
        }

        private void initLayouts(){
            setLayout(new BorderLayout());
            Box box = Box.createVerticalBox();
            Box box1 = Box.createHorizontalBox();
            box1.add(type = new JTextField());
            box1.add(displayType = new JTextField());

            Box box2 = Box.createHorizontalBox();
            box2.add(extractor = new JComboBox());

            Box box3 = Box.createHorizontalBox();
            box3.add(comparator = new JComboBox());

            Box box4 = Box.createHorizontalBox();
            box4.add(description = new JTextArea());

            Box box5 = Box.createHorizontalBox();
            box5.add(Box.createHorizontalGlue());
            box5.add(expert = new JCheckBox(Messages
                    .getString("define.expert.label")));
            box5.add(Box.createHorizontalGlue());
            box5.add(userDefined = new JCheckBox(Messages
                    .getString("define.userdef.label")));
            box5.add(Box.createHorizontalGlue());

            box.add(box1);
            box.add(box2);
            box.add(box3);
            box.add(box5);
            add(box, BorderLayout.NORTH);
            add(box4, BorderLayout.CENTER);

            Utility.decorateJComponent(type, "define.type");
            Utility.decorateJComponent(displayType, "define.displaytype");
            Utility.decorateJComponent(description, "define.description");
            Utility.decorateJComponent(extractor, "define.extractor");
            Utility.decorateJComponent(comparator, "define.comparator");
            Utility.decorateJComponent(expert, "define.expert");
            Utility.decorateJComponent(userDefined, "define.userdef");

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
        }
    }
}
