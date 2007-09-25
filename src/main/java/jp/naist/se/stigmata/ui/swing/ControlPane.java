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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ComparisonMethod;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.Stigmata;
import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.utils.ConfigFileExporter;
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
    private PopupButton comparePopup;
    private JComboBox unitBox;
    private Map<String, String> unitLabels = new HashMap<String, String>();
    private boolean expertmode = false;

    public ControlPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
    }

    void inititalize(){
        definition = new BirthmarkDefinitionPane(stigmata);
        birthmarks = new BirthmarkSelectionCheckSetPane(stigmata);
        properties = new PropertyEditPane(stigmata);
        JComponent control = createControlPane();
        wellknownClassses = new WellknownClassesSettingsPane(stigmata);
        classpath = new ClasspathSettingsPane(stigmata);
        filters = new FilterManagementPane(stigmata);
        initComponents();

        Utility.addNewTab("targets", controlTab, control);
        Utility.addNewTab("wellknown", controlTab, wellknownClassses);
        Utility.addNewTab("classpath", controlTab, classpath);
        Utility.addNewTab("property", controlTab, properties);
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

        int definitionTabIndex = controlTab.indexOfTab(Messages.getString("definition.tab.label"));
        if(definitionTabIndex >= 0){
            controlTab.removeTabAt(definitionTabIndex);
        }
        int filterTabIndex = controlTab.indexOfTab(Messages.getString("filter.tab.label"));
        if(filterTabIndex >= 0){
            controlTab.removeTabAt(filterTabIndex);
        }
    }

    public void exportSettings(){
        File file = stigmata.getSaveFile(
            Messages.getStringArray("export.extensions"), 
            Messages.getString("export.description")
        );

        if(file != null){
            BirthmarkEnvironment environment = generateEnvironment();
            if(!file.getName().endsWith(".xml")){
                file = new File(file.getParent(), file.getName() + ".xml");
            }

            ConfigFileExporter bce = new ConfigFileExporter(environment);
            try{
                PrintWriter out = new PrintWriter(new FileWriter(file));
                bce.export(out);
                out.close();
            } catch(IOException e){
                JOptionPane.showMessageDialog(
                    stigmata,
                    Messages.getString("error.io", e.getMessage()),
                    Messages.getString("error.dialog.title"),
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public void setExpertMode(boolean expertmode){
        this.expertmode = expertmode;
        birthmarks.setExpertMode(expertmode);
        stigmata.setExpertMode(expertmode);

        if(expertmode){
            Utility.addNewTab("definition", controlTab, definition);
            Utility.addNewTab("filter", controlTab, filters);
        }
        else{
            removeTabByName(Messages.getString("definition.tab.label"));
            removeTabByName(Messages.getString("filter.tab.label"));
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
        String[] targets = birthmarks.getSelectedServiceTypes();

        extractButton.setEnabled(
            ((valueX != null && valueX.length > 0) || (valueY != null && valueY.length > 0))
            && (targets != null && targets.length > 0)
        );

        comparePopup.setEnabled(
            (valueX != null && valueX.length > 0) &&
            (valueY != null && valueY.length > 0) &&
            (targets != null && targets.length > 0)
        );
    }

    private void extractButtonActionPerformed(ActionEvent e){
        BirthmarkContext context = generateContext();

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

        stigmata.extract(targets.toArray(new String[targets.size()]), context);
    }

    private void compareRoundRobinWithFiltering(){
        BirthmarkContext context = generateContext();
        context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_XY);
        FilterSelectionPane pane = new FilterSelectionPane(
            context.getEnvironment().getFilterManager()
        );
        int returnValue = JOptionPane.showConfirmDialog(
            stigmata, pane, Messages.getString("filterselection.dialog.title"),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if(returnValue == JOptionPane.OK_OPTION){
            String[] filterSetList = pane.getSelectedFilters();
            context.setFilterTypes(filterSetList);

            String[] fileX = targetX.getValues();
            String[] fileY = targetY.getValues();
            stigmata.compareRoundRobinFilter(fileX, fileY, context);
        }
    }

    private void compareRoundRobin(){
        BirthmarkContext context = generateContext();
        context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_XY);
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();

        stigmata.compareRoundRobin(fileX, fileY, context);
    }

    private void compareSpecifiedPair(){
        BirthmarkContext context = generateContext();
        context.setComparisonMethod(ComparisonMethod.SPECIFIED_PAIR);
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();
        stigmata.compareSpecifiedPair(fileX, fileY, context);
    }

    private void compareGuessedPair(){
        BirthmarkContext context = generateContext();
        context.setComparisonMethod(ComparisonMethod.GUESSED_PAIR);
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();

        stigmata.compareGuessedPair(fileX, fileY, context);
    }

    private BirthmarkContext generateContext(){
        BirthmarkContext context = new BirthmarkContext(generateEnvironment());
        context.setBirthmarkTypes(birthmarks.getSelectedServiceTypes());
        context.setExtractionUnit(parseExtractionUnit());
        
        return context;
    }

    private BirthmarkEnvironment generateEnvironment(){
        BirthmarkEnvironment environment = Stigmata.getInstance().createEnvironment();
        ClasspathContext bytecode = environment.getClasspathContext();
        WellknownClassManager manager = environment.getWellknownClassManager();
        ComparisonPairFilterManager filterManager = environment.getFilterManager();

        // environment.setExtractionUnit(parseExtractionUnit());
        classpath.updateClasspathContext(bytecode);
        wellknownClassses.setWellknownClasses(manager);
        filters.updateFilterManager(filterManager);
        definition.updateEnvironment(environment);
        properties.updateEnvironment(environment);

        return environment;
    }

    private ExtractionUnit parseExtractionUnit(){
        String label = (String)unitBox.getSelectedItem();
        String key = unitLabels.get(label);
        ExtractionUnit unit = ExtractionUnit.CLASS;

        if(key.equals("unit.archive")){
            unit = ExtractionUnit.ARCHIVE;
        }
        else if(key.equals("unit.package")){
            unit = ExtractionUnit.PACKAGE;
        }
        else{
            unit = ExtractionUnit.CLASS;
        }
        return unit;
    }

    private void initComponents(){
        controlTab = new JTabbedPane();
        resetButton = Utility.createButton("reset");
        extractButton = Utility.createButton("extract");
        compareButton = Utility.createButton("roundrobin");
        comparePopup = new PopupButton(compareButton);
        unitBox = new JComboBox();

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(resetButton);
        south.add(Box.createHorizontalGlue());
        south.add(extractButton);
        south.add(Box.createHorizontalGlue());
        south.add(comparePopup);
        south.add(Box.createHorizontalGlue());
        south.add(unitBox);
        south.add(Box.createHorizontalGlue());

        setLayout(new BorderLayout());
        add(south, BorderLayout.SOUTH);
        add(controlTab, BorderLayout.CENTER);

        extractButton.setEnabled(false);
        comparePopup.setEnabled(false);

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

        ActionListener compareListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String command = e.getActionCommand();

                if(command.equals("roundrobin")){
                    compareRoundRobin();
                }
                else if(command.equals("guessedpair")){
                    compareGuessedPair();
                }
                else if(command.equals("specifiedpair")){
                    compareSpecifiedPair();
                }
                else if(command.equals("roundrobin.filtering")){
                    compareRoundRobinWithFiltering();
                }
            }
        };
        compareButton.addActionListener(compareListener);

        String[] comparisonMethods = Messages.getStringArray("comparison.methods");
        for(int i = 1; i < comparisonMethods.length; i++){
            JMenuItem item = Utility.createJMenuItem(comparisonMethods[i]);
            comparePopup.addMenuItem(item);
            item.addActionListener(compareListener);
        }

        String[] extractionUnits = Messages.getStringArray("extraction.units");
        for(int i = 0; i < extractionUnits.length; i++){
            String label = Messages.getString(extractionUnits[i]);
            unitLabels.put(label, extractionUnits[i]);
            unitBox.addItem(label);
        }
    }
}
