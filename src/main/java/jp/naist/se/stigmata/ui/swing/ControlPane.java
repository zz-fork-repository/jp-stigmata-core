package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.Stigmata;
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
    private TargetSelectionPane classpath;
    private TargetSelectionPane bootClasspath;
    private WellknownClassesSettingsPane wellknownClassses;
    private JTabbedPane controlTab;
    private JButton compareButton;
    private JButton extractButton;
    private JButton resetButton;
    private JComboBox comparisonMethods;

    public ControlPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initComponents();
        Utility.addNewTab("targets", controlTab, createControlPane());
        Utility.addNewTab("wellknown", controlTab, createWellknownClassPane());
        Utility.addNewTab("classpath", controlTab, createClasspathPane());

        reset();
    }

    public void reset(){
        birthmarks.reset();
        targetX.removeAllElements();
        targetY.removeAllElements();
        classpath.removeAllElements();
        bootClasspath.removeAllElements();

        addClasspath(bootClasspath, System.getProperty("java.class.path"));
        addClasspath(bootClasspath, System.getProperty("sun.boot.class.path"));

        wellknownClassses.reset();
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
                checkButtonEnabled();
            }
        };
        targetX.addDataChangeListener(dcl);
        targetY.addDataChangeListener(dcl);
        birthmarks.addDataChangeListener(dcl);

        return center;
    }

    private void checkButtonEnabled(){
        String[] valueX = targetX.getValues();
        String[] valueY = targetY.getValues();
        String[] targets = birthmarks.getSelectedServices();

        extractButton
                .setEnabled(((valueX != null && valueX.length > 0) || (valueY != null && valueY.length > 0))
                        && (targets != null && targets.length > 0));

        compareButton
                .setEnabled((valueX != null && valueX.length > 0)
                        && (valueY != null && valueY.length > 0)
                        && (targets != null && targets.length > 0));
    }

    private void addClasspath(TargetSelectionPane target, String classpath){
        if(classpath != null){
            target.addValues(classpath.split(System.getProperty("path.separator")));
        }
    }

    private JComponent createClasspathPane(){
        JComponent panel = new JPanel(new GridLayout(1, 2));
        JPanel center = new JPanel(new BorderLayout());
        classpath = new TargetSelectionPane(stigmata);
        bootClasspath = new TargetSelectionPane(stigmata);
        JComponent south = Box.createHorizontalBox();
        final JButton findButton = Utility.createButton("findclass");
        final JTextField text = new JTextField();
        final JLabel label = new JLabel();

        classpath.setBorder(new TitledBorder(Messages.getString("userclasspath.border")));
        classpath.addTargetExtensions(Messages.getStringArray("userclasspath.extensions"));
        classpath.setDescription(Messages.getString("userclasspath.description"));
        classpath.setDirectorySelectable(true);

        bootClasspath.setBorder(new TitledBorder(Messages.getString("bootclasspath.border")));
        bootClasspath.setEnabled(false);

        findButton.setEnabled(false);

        south.setBorder(new TitledBorder(Messages.getString("classpathchecker.border")));

        label.setIcon(Utility.getIcon("classpathchecker.default.icon"));
        label.setToolTipText(Messages.getString("classpathchecker.default.tooltip"));

        center.add(panel, BorderLayout.CENTER);
        center.add(south, BorderLayout.SOUTH);

        panel.add(classpath);
        panel.add(bootClasspath);

        south.add(Box.createHorizontalGlue());
        south.add(text);
        south.add(Box.createHorizontalGlue());
        south.add(findButton);
        south.add(Box.createHorizontalGlue());
        south.add(label);
        south.add(Box.createHorizontalGlue());

        text.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent arg0){
                String t = text.getText();
                findButton.setEnabled(t.trim().length() > 0);
            }

            public void insertUpdate(DocumentEvent arg0){
                String t = text.getText();
                findButton.setEnabled(t.trim().length() > 0);
            }

            public void removeUpdate(DocumentEvent arg0){
                String t = text.getText();
                findButton.setEnabled(t.trim().length() > 0);
            }
        });
        ActionListener action = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                boolean flag = findClass(text.getText().trim());
                if(flag){
                    label.setIcon(Utility.getIcon("classpathchecker.found.icon"));
                    label.setToolTipText(Messages.getString("classpathchecker.found.tooltip"));
                }
                else{
                    label.setIcon(Utility.getIcon("classpathchecker.notfound.icon"));
                    label.setToolTipText(Messages.getString("classpathchecker.notfound.tooltip"));
                }
            }
        };
        findButton.addActionListener(action);
        text.addActionListener(action);

        return center;
    }

    private boolean findClass(String className){
        try{
            ClasspathContext b = stigmata.getStigmata().createContext().getBytecodeContext();
            ClasspathContext bytecode = new ClasspathContext(b);
            String[] path = classpath.getValues();
            for(String cp: path){
                bytecode.addClasspath(new File(cp).toURI().toURL());
            }
            return bytecode.findClass(className) != null;
        }catch(Exception e){
        }
        return false;
    }

    private void extractButtonActionPerformed(ActionEvent e){
        BirthmarkContext context = initAction();
        String[] fileX = targetX.getValues();
        String[] fileY = targetY.getValues();
        Set<String> targets = new HashSet<String>();
        if(fileX != null && fileX.length > 0){
            for(String file: fileX)
                targets.add(file);
        }
        if(fileY != null && fileY.length > 0){
            for(String file: fileY)
                targets.add(file);
        }

        stigmata.extract(birthmarks.getSelectedServices(), targets.toArray(new String[targets
                .size()]), context);
    }

    private void compareRoundRobin(){
        BirthmarkContext context = initAction();

        stigmata.compareRoundRobin(birthmarks.getSelectedServices(), targetX.getValues(), targetY
                .getValues(), context);
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
        Stigmata s = stigmata.getStigmata();
        BirthmarkContext context = s.createContext();
        ClasspathContext bytecode = context.getBytecodeContext();
        WellknownClassManager manager = context.getWellknownClassManager();

        String[] cplist = classpath.getValues();
        if(cplist != null && cplist.length >= 0){
            for(int i = 0; i < cplist.length; i++){
                try{
                    bytecode.addClasspath(new File(cplist[i]).toURI().toURL());
                }catch(IOException ee){
                }
            }
        }

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
            }
        });
    }
}
