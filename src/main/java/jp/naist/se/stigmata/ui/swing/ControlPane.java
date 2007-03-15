package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.utils.WellknownClassManager;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ControlPane extends JPanel{
    private static final long serialVersionUID = 983547612346543645L;

    private StigmataFrame stigmata;
    private BirthmarkSelectablePane birthmarks;
    private TargetSelectionPane targetX;
    private TargetSelectionPane targetY;
    private ClasspathSettingsPane classpath;
    private WellknownClassesSettingsPane wellknownClassses;
    private BirthmarkDefinitionPane definition;
    private PropertyEditPane properties;
    private FilterManagementPane filters;
    private JTabbedPane controlTab;
    private JButton compareButton;
    private JButton extractButton;
    private JButton resetButton;
    private JComboBox comparisonMethods;
    private boolean expertmode = false;

    public ControlPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
    }

    void inititalize(){
        initComponents();
        definition = new BirthmarkDefinitionPane(stigmata);
        properties = new PropertyEditPane(stigmata);
        JComponent control = createControlPane();
        wellknownClassses = new WellknownClassesSettingsPane(stigmata);
        classpath = new ClasspathSettingsPane(stigmata);
        filters = new FilterManagementPane(stigmata);

        Utility.addNewTab("targets", controlTab, control);
        Utility.addNewTab("filter",  controlTab, filters);
        Utility.addNewTab("wellknown", controlTab, wellknownClassses);
        Utility.addNewTab("classpath", controlTab, classpath);
        reset();
    }

    public void addBirthmarkServiceListener(BirthmarkServiceListener listener){
        definition.addBirthmarkServiceListener(listener);
    }

    public void removeBirthmarkServiceListener(BirthmarkServiceListener listener){
        definition.removeBirthmarkServiceListener(listener);
    }

    public void reset(){
        this.expertmode = false;
        definition.reset();
        birthmarks.reset();
        stigmata.setExpertMode(false);
        targetX.removeAllElements();
        targetY.removeAllElements();

        classpath.reset();
        wellknownClassses.reset();
        filters.reset();
        updateEnable();

        int index1 = controlTab.indexOfTab(Messages.getString("definition.tab.label"));
        if(index1 >= 0){
            controlTab.removeTabAt(index1);
        }
        int index2 = controlTab.indexOfTab(Messages.getString("property.tab.label"));
        if(index2 >= 0){
            controlTab.removeTabAt(index2);
        }
    }

    public void exportSettings(){
        File file = stigmata.getSaveFile(Messages.getStringArray("export.extensions"),
            Messages.getString("export.description"));
        if(file != null){
            if(!file.getName().endsWith(".xml")){
                file = new File(file.getParent(), file.getName() + ".xml");
            }
            try{
                PrintWriter out = new PrintWriter(new FileWriter(file));
                out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                out.println("<stigmata>");
                definition.exportSettings(out);
                filters.exportSettings(out);
                wellknownClassses.exportSettings(out);
                classpath.exportSettings(out);
                properties.exportSettings(out);
                out.println("</stigmata>");
                out.close();
            } catch(IOException e){
            }
        }
    }

    public void setExpertMode(boolean expertmode){
        this.expertmode = expertmode;
        birthmarks.setExpertMode(expertmode);
        stigmata.setExpertMode(expertmode);

        if(expertmode){
            Utility.addNewTab("definition", controlTab, definition);
            Utility.addNewTab("property", controlTab, properties);
        }
        else{
            removeTabByName(Messages.getString("definition.tab.label"));
            removeTabByName(Messages.getString("property.tab.label"));
        }
        updateEnable();
    }

    public boolean isExpertMode(){
        return expertmode;
    }

    private void removeTabByName(String tabname){
        int index = controlTab.indexOfTab(tabname);
        if(index >= 0){
            controlTab.removeTabAt(index);
        }
    }

    private JComponent createControlPane(){
        JComponent mainPane = new Box(BoxLayout.X_AXIS);
        JPanel center = new JPanel(new BorderLayout());
        birthmarks = new BirthmarkSelectionCheckSetPane(stigmata);
        targetX = new TargetSelectionPane(stigmata);
        targetY = new TargetSelectionPane(stigmata);

        birthmarks.setBorder(new TitledBorder(Messages.getString("birthmarkspane.border")));

        targetX.addTargetExtensions(Messages.getStringArray("targets.extensions"));
        targetX.setDescription(Messages.getString("targets.description"));
        targetX.setBorder(new TitledBorder(Messages.getString("targetx.border")));

        targetY.addTargetExtensions(Messages.getStringArray("targets.extensions"));
        targetY.setDescription(Messages.getString("targets.description"));
        targetY.setBorder(new TitledBorder(Messages.getString("targety.border")));

        center.add(mainPane, BorderLayout.CENTER);
        center.add(birthmarks, BorderLayout.SOUTH);

        mainPane.add(Box.createHorizontalGlue());
        mainPane.add(targetX);
        mainPane.add(Box.createHorizontalGlue());
        mainPane.add(targetY);
        mainPane.add(Box.createHorizontalGlue());

        DataChangeListener dcl = new DataChangeListener(){
            public void valueChanged(Object source){
                updateEnable();
            }
        };
        targetX.addDataChangeListener(dcl);
        targetY.addDataChangeListener(dcl);
        birthmarks.addDataChangeListener(dcl);

        return center;
    }

    private void updateEnable(){
        String[] valueX = targetX.getValues();
        String[] valueY = targetY.getValues();
        String[] targets = birthmarks.getSelectedServices();

        extractButton.setEnabled(((valueX != null && valueX.length > 0)
                                  || (valueY != null && valueY.length > 0))
                                 && (targets != null && targets.length > 0));

        compareButton.setEnabled((valueX != null && valueX.length > 0)
                                 && (valueY != null && valueY.length > 0)
                                 && (targets != null && targets.length > 0));
    }

    private void extractButtonActionPerformed(ActionEvent e){
        BirthmarkContext context = initAction();
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();
        Set<String> targets = new HashSet<String>();
        if(fileX != null && fileX.length > 0){
            for(String file: fileX){
                targets.add(file);
            }
        }
        if(fileY != null && fileY.length > 0){
            for(String file: fileY){
                targets.add(file);
            }
        }

        stigmata.extract(birthmarks.getSelectedServices(),
                         targets.toArray(new String[targets.size()]), context);
    }

    private void compareRoundRobinWithFiltering(){
        BirthmarkContext context = initAction();
        String[] filterSetList = filters.getSelectedFilterNames();

        stigmata.compareRoundRobin(birthmarks.getSelectedServices(), targetX.getValues(), 
                                   targetY.getValues(), filterSetList, context);
    }

    private void compareRoundRobin(){
        BirthmarkContext context = initAction();

        stigmata.compareRoundRobin(birthmarks.getSelectedServices(),
                                   targetX.getValues(), targetY.getValues(), context);
    }

    private void compareSpecifiedPair(){
        BirthmarkContext context = initAction();
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();
        stigmata.compareSpecifiedPair(birthmarks.getSelectedServices(), fileX, fileY, context);
    }

    private void compareGuessedPair(){
        BirthmarkContext context = initAction();
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();

        stigmata.compareGuessedPair(birthmarks.getSelectedServices(), fileX, fileY, context);
    }

    private BirthmarkContext initAction(){
        BirthmarkContext context = stigmata.getStigmata().createContext();
        // BirthmarkContext context2 = stigmata.getContext();
        ClasspathContext bytecode = context.getBytecodeContext();
        WellknownClassManager manager = context.getWellknownClassManager();

        classpath.updateClasspathContext(bytecode);
        wellknownClassses.setWellknownClasses(manager);
        definition.updateContext(context);
        properties.updateContext(context);
        filters.updateFilterManager(context.getFilterManager());

        return context;
    }

    private void initComponents(){
        controlTab = new JTabbedPane();
        resetButton = Utility.createButton("reset");
        extractButton = Utility.createButton("extract");
        compareButton = Utility.createButton("compare");
        comparisonMethods = new JComboBox();

        String[] items = Messages.getStringArray("comparison.methods");
        for(int i = 0; i < items.length; i++){
            comparisonMethods.addItem(items[i]);
        }

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(resetButton);
        south.add(Box.createHorizontalGlue());
        south.add(extractButton);
        south.add(Box.createHorizontalGlue());
        south.add(compareButton);
        south.add(Box.createHorizontalGlue());
        south.add(comparisonMethods);
        south.add(Box.createHorizontalGlue());

        setLayout(new BorderLayout());
        add(south, BorderLayout.SOUTH);
        add(controlTab, BorderLayout.CENTER);

        extractButton.setEnabled(false);
        compareButton.setEnabled(false);

        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reset();
            }
        });

        extractButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                extractButtonActionPerformed(e);
            }
        });

        compareButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String item = (String)comparisonMethods.getSelectedItem();
                if(item.equals(Messages.getString("roundrobin.label"))){
                    compareRoundRobin();
                }
                else if(item.equals(Messages.getString("guessedpair.label"))){
                    compareGuessedPair();
                }
                else if(item.equals(Messages.getString("specifiedpair.label"))){
                    compareSpecifiedPair();
                }
                else if(item.equals(Messages.getString("roundrobin.filtering.label"))){
                    compareRoundRobinWithFiltering();
                }
            }
        });
    }
}
