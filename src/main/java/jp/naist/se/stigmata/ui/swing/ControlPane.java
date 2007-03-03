package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private BirthmarkSelectionPane birthmarks;
    private TargetSelectionPane targetX;
    private TargetSelectionPane targetY;
    private ClasspathSettingsPane classpath;
    private WellknownClassesSettingsPane wellknownClassses;
    private BirthmarkDefinitionPane definition;
    private JTabbedPane controlTab;
    private JButton compareButton;
    private JButton extractButton;
    private JButton resetButton;
    private JComboBox comparisonMethods;
    private boolean geekmode = false;

    public ControlPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initComponents();
        Utility.addNewTab("targets", controlTab, createControlPane());
        Utility.addNewTab("wellknown", controlTab, createWellknownClassPane());
        Utility.addNewTab("classpath", controlTab, classpath = new ClasspathSettingsPane(stigmata));
        definition = new BirthmarkDefinitionPane(stigmata);

        reset();
    }

    public void updateService(){
        birthmarks.updateService();
        updateEnable();
    }

    public void reset(){
        this.geekmode = false;
        definition.reset();
        birthmarks.reset();
        stigmata.setGeekMode(false);
        targetX.removeAllElements();
        targetY.removeAllElements();

        classpath.reset();
        wellknownClassses.reset();
        updateEnable();

        int index = controlTab.indexOfTab(Messages.getString("definition.tab.label"));
        if(index >= 0){
            controlTab.removeTabAt(index);
        }
    }

    public void setGeekMode(boolean geekmode){
        this.geekmode = geekmode;
        birthmarks.setGeekMode(geekmode);
        stigmata.setGeekMode(geekmode);

        if(geekmode){
            Utility.addNewTab("definition", controlTab, definition);
        }
        else{
            int index = controlTab.indexOfTab(Messages.getString("definition.tab.label"));
            if(index >= 0){
                controlTab.removeTabAt(index);
            }
        }
        updateEnable();
    }

    public boolean isGeekMode(){
        return geekmode;
    }

    private JComponent createWellknownClassPane(){
        BirthmarkContext b = stigmata.getStigmata().createContext();
        wellknownClassses = new WellknownClassesSettingsPane(b.getWellknownClassManager());
        return wellknownClassses;
    }

    private JComponent createControlPane(){
        JComponent mainPane = new Box(BoxLayout.X_AXIS);
        JPanel center = new JPanel(new BorderLayout());
        BirthmarkSelectionPane birthmarks = new BirthmarkSelectionPane(stigmata);
        targetX = new TargetSelectionPane(stigmata);
        targetY = new TargetSelectionPane(stigmata);

        birthmarks.setBorder(new TitledBorder(Messages.getString("birthmarkspane.border")));
        this.birthmarks = birthmarks;

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
        BirthmarkContext context = stigmata.getContext();
        ClasspathContext bytecode = context.getBytecodeContext();
        WellknownClassManager manager = context.getWellknownClassManager();

        classpath.updateClasspathContext(bytecode);
        wellknownClassses.setWellknownClasses(manager);

        return context;
    }

    private void initComponents(){
        JPanel south = new JPanel();
        controlTab = new JTabbedPane();
        resetButton = Utility.createButton("reset");
        extractButton = Utility.createButton("extract");
        compareButton = Utility.createButton("compare");
        comparisonMethods = new JComboBox();

        String[] items = Messages.getStringArray("comparison.methods");
        for(int i = 0; i < items.length; i++){
            comparisonMethods.addItem(items[i]);
        }

        setLayout(new BorderLayout());
        south.add(resetButton);
        south.add(extractButton);
        south.add(compareButton);
        south.add(comparisonMethods);

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
                    // compareRoundRobinWithFiltering();
                }
            }
        });
    }
}
