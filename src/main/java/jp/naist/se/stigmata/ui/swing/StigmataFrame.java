package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElementClassNotFoundException;
import jp.naist.se.stigmata.BirthmarkEngine;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkExtractionFailedException;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.Stigmata;
import jp.naist.se.stigmata.event.BirthmarkEngineAdapter;
import jp.naist.se.stigmata.event.BirthmarkEngineEvent;
import jp.naist.se.stigmata.event.WarningMessages;
import jp.naist.se.stigmata.result.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.ui.swing.actions.AboutAction;
import jp.naist.se.stigmata.ui.swing.actions.LicenseAction;
import jp.naist.se.stigmata.ui.swing.graph.SimilarityDistributionGraphPane;
import jp.naist.se.stigmata.ui.swing.mds.MDSGraphPanel;
import jp.naist.se.stigmata.ui.swing.tab.EditableTabbedPane;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class StigmataFrame extends JFrame{
    private static final long serialVersionUID = 92345543665342134L;

    private JTabbedPane tabPane;
    private JMenuItem closeTabMenu;
    private JCheckBoxMenuItem expertmodeMenu;
    private Stigmata stigmata;
    private BirthmarkEnvironment environment;
    private ControlPane control;
    private FileIOManager fileio;
    private Map<String, Integer> countmap = new HashMap<String, Integer>();

    public StigmataFrame(){
        this(Stigmata.getInstance());
    }

    public StigmataFrame(Stigmata stigmata){
        this(stigmata, BirthmarkEnvironment.getDefaultEnvironment());
    }

    public StigmataFrame(Stigmata stigmata, BirthmarkEnvironment environment){
        this.stigmata = stigmata;
        this.environment = environment;
        this.fileio = new FileIOManager(this);
        Image iconImage = Utility.getImage("stigmata.icon");
        if(iconImage != null){
            setIconImage(iconImage);
        }

        stigmata.addBirthmarkEngineListener(new BirthmarkEngineAdapter(){
            @Override
            public void operationDone(BirthmarkEngineEvent e){
                showWarnings(e.getMessage());
            }
        });

        initLayouts();
    }

    public Stigmata getStigmata(){
        return stigmata;
    }

    public BirthmarkEnvironment getEnvironment(){
        return environment;
    }

    public File getCurrentDirectory(){
        return fileio.getCurrentDirectory();
    }

    public void setCurrentDirectory(File file){
        try{
            fileio.setCurrentDirectory(file);
        } catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(
                this,
                Messages.getString("notdirectory.dialog.message", file.getName()),
                Messages.getString("notdirectory.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
        } catch(Exception e){
            showExceptionMessage(e);
        }
    }

    public File[] openFiles(FileFilter[] filters, boolean multipleSelectable, boolean directorySelectable){
        return fileio.openFiles(filters, multipleSelectable, directorySelectable);
    }

    /**
     * Find file to open it.
     */
    public File getOpenFile(String[] exts, String desc){
        return fileio.findFile(true, exts, desc);
    }

    /**
     * Find file for storing data to it.
     * Extension of found file is correct as selected extension.
     */
    public File getSaveFile(String[] exts, String desc){
        return fileio.findFile(false, exts, desc);
    }

    public void addBirthmarkServiceListener(BirthmarkServiceListener listener){
        control.addBirthmarkServiceListener(listener);
    }

    public void removeBirthmarkServiceListener(BirthmarkServiceListener listener){
        control.removeBirthmarkServiceListener(listener);
    }

    public void compareDetails(BirthmarkSet target1, BirthmarkSet target2, BirthmarkContext context){
        PairComparisonPane detail = new PairComparisonPane(
            this, new ComparisonPair(target1, target2, context)
        );
        int compareDetail = getNextCount("compare_detail");

        Utility.addNewTab("comparedetail", tabPane, detail,
            new Object[] { new Integer(compareDetail), },
            new Object[] {
                Utility.array2String(target1.getBirthmarkTypes()),
                target1.getName(),
                target2.getName(),
            }
        );
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void compareRoundRobin(String[] targetX, String[] targetY, 
            BirthmarkContext context){
        try{
            BirthmarkEngine engine = getStigmata().createEngine(context.getEnvironment());
            ExtractionResultSet ers = engine.extract(targetX, targetY, context);

            RoundRobinComparisonResultPane compare = new RoundRobinComparisonResultPane(this, ers);
            int compareCount = getNextCount("compare");
            Utility.addNewTab(
                "compare", tabPane, compare,
                new Object[] { new Integer(compareCount), },
                new Object[] {
                    Utility.array2String(context.getBirthmarkTypes()),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } catch(Throwable e){
            showExceptionMessage(e);
        }
    }

    public void compareRoundRobinFilter(String[] targetX, String[] targetY, 
            BirthmarkContext context){
        try{
            BirthmarkEngine engine = getStigmata().createEngine(context.getEnvironment());

            ExtractionResultSet ers = engine.extract(targetX, targetY, context);
            ComparisonResultSet resultset = engine.compare(ers);
            if(context.hasFilter()){
                resultset = engine.filter(resultset);
            }
            int compareCount = getNextCount("compare");
            Utility.addNewTab(
                "compare", tabPane, new PairComparisonResultSetPane(this, resultset),
                new Object[] { new Integer(compareCount), },
                new Object[] {
                    Utility.array2String(context.getBirthmarkTypes()),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } catch(Throwable e){
            showExceptionMessage(e);
        }
    }

    public void compareGuessedPair(String[] targetX, String[] targetY, BirthmarkContext context){
        try{
            BirthmarkEngine engine = getStigmata().createEngine(context.getEnvironment());
            ExtractionResultSet extraction = engine.extract(targetX, targetY, context);
            int comparePair = getNextCount("compare_pair");

            ComparisonResultSet resultset = new CertainPairComparisonResultSet(extraction);
            Utility.addNewTab("comparepair", tabPane,
                new PairComparisonResultSetPane(this, resultset),
                new Object[] { new Integer(comparePair), },
                new Object[] {
                    Utility.array2String(context.getBirthmarkTypes()),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        }catch(Throwable e){
            showExceptionMessage(e);
        }
    }

    public void compareSpecifiedPair(String[] targetX, String[] targetY, BirthmarkContext context){
        File file = getOpenFile(Messages.getStringArray("comparemapping.extension"),
                                Messages.getString("comparemapping.description"));

        if(file != null){
            Map<String, String> mapping = constructMapping(file);

            try{
                BirthmarkEngine engine = getStigmata().createEngine(context.getEnvironment());
                context.setNameMappings(mapping);
                ComparisonResultSet crs = engine.compare(targetX, targetY, context);
                int comparePair = getNextCount("compare_pair");

                Utility.addNewTab(
                    "comparepair", tabPane,
                    new PairComparisonResultSetPane(this, crs),
                    new Object[] { new Integer(comparePair), },
                    new Object[] {
                        Utility.array2String(context.getBirthmarkTypes()),
                        Utility.array2String(targetX),
                        Utility.array2String(targetY),
                    }
                );
                tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
            }catch(Exception e){
                showExceptionMessage(e);
            }
        }
    }

    public void showComparisonResultSet(ComparisonResultSet resultset){
        int comparePair = getNextCount("compare_pair");
        Utility.addNewTab(
            "comparisonresultset", tabPane,
            new PairComparisonResultSetPane(this, resultset),
            new Object[] { new Integer(comparePair), }, null
        );
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void showMDSGraph(BirthmarkSet[] set, BirthmarkContext context){
        try{
            MDSGraphPanel panel = new MDSGraphPanel(this, set, context);
            int mappingGraphCount = getNextCount("mds_graph");
            Utility.addNewTab("mappinggraph", tabPane, panel, new Object[] { new Integer(mappingGraphCount), }, null);
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } catch(Exception e){
            showExceptionMessage(e);
        }
    }

    public void showSimilarityDistributionGraph(Map<Integer, Integer> distributions){
        SimilarityDistributionGraphPane graph = new SimilarityDistributionGraphPane(this, distributions);

        int similarityGraphCount = getNextCount("similarity_graph");
        Utility.addNewTab("similaritygraph", tabPane, graph, new Object[] { new Integer(similarityGraphCount), }, null);
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void showExtractionResult(ExtractionResultSet ers){
        int extractCount = getNextCount("extract");
        BirthmarkExtractionResultPane viewer = new BirthmarkExtractionResultPane(this, ers);
        Utility.addNewTab(
            "extract", tabPane, viewer,
            new Object[] { new Integer(extractCount), },
            new Object[] { Utility.array2String(ers.getBirthmarkTypes()), }
        );
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        
    }

    public void extract(String[] targets, BirthmarkContext context){
        try{
            BirthmarkEngine engine = getStigmata().createEngine(context.getEnvironment());
            ExtractionResultSet ers = engine.extract(targets, context);
            showExtractionResult(ers);
        }catch(Throwable e){
            showExceptionMessage(e);
        }
    }

    /**
     * csv file to Map.
     */
    public Map<String, String> constructMapping(File file){
        Map<String, String> mapping = new HashMap<String, String>();
        BufferedReader in = null;
        try{
            in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null){
                String[] tokens = line.split(", *");
                if(tokens.length >= 2){
                    mapping.put(tokens[0], tokens[1]);
                }
            }

        }catch(Exception e){
            showExceptionMessage(e);
        }finally{
            if(in != null){
                try{
                    in.close();
                }catch(IOException e){
                }
            }
        }
        return mapping;
    }

    private void initLayouts(){
        setTitle(Messages.getString("stigmata.frame.title"));
        initComponents();

        Utility.addNewTab("control", tabPane, control = new ControlPane(this), null, null);
        control.inititalize();
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);

        setSize(900, 600);
    }

    private void showWarnings(WarningMessages warnings){
        if(warnings.getWarningCount() > 0){
            StringBuilder sb = new StringBuilder("<html><body><dl>");
            for(Iterator<Exception> i = warnings.exceptions(); i.hasNext(); ){
                Exception e = i.next();
                sb.append("<dt>").append(e.getClass().getName()).append("</dt>");
                sb.append("<dd>").append(e.getMessage()).append("</dd>");
                sb.append("<dd>").append(warnings.getString(e)).append("</dd>");
            }
            sb.append("</dl></body></html>");

            JOptionPane.showMessageDialog(
                this, new String(sb), Messages.getString("warning.dialog.title"),
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void initComponents(){
        setDefaultUI();
        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createHelpMenu());

        setJMenuBar(menubar);

        tabPane = new EditableTabbedPane(this);
        add(tabPane, BorderLayout.CENTER);

        tabPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent arg0){
                String title = tabPane.getTitleAt(tabPane.getSelectedIndex());
                closeTabMenu.setEnabled(!title.equals(Messages.getString("control.tab.label")));
            }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JMenu createFileMenu(){
        JMenu fileMenu = Utility.createJMenu("fileMenu");
        JMenuItem newFrameMenu = Utility.createJMenuItem("newframe");
        JMenuItem saveMenu = Utility.createJMenuItem("savesetting");
        JMenuItem exportMenu = Utility.createJMenuItem("exportsetting");
        JMenuItem closeTabMenu = Utility.createJMenuItem("closetab");
        JMenuItem closeMenu = Utility.createJMenuItem("closeframe");
        JMenuItem exitMenu = Utility.createJMenuItem("exit");
        this.closeTabMenu = closeTabMenu;

        fileMenu.add(newFrameMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(saveMenu);
        fileMenu.add(exportMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeTabMenu);
        fileMenu.add(closeMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenu);

        newFrameMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                StigmataFrame frame = new StigmataFrame(stigmata, environment);
                frame.setVisible(true);
            }
        });
        saveMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                control.saveSettings(new File(System.getProperty("user.home"), ".stigmata/stigmata.xml"));
            }
        });

        exportMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                control.exportSettings();
            }
        });

        closeTabMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                closeTabMenuActionPerformed();
            }
        });

        closeMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                setVisible(false);
                dispose();
            }
        });

        exitMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                System.exit(0);
            }
        });
        return fileMenu;
    }

    private JMenu createHelpMenu(){
        JMenu menu = Utility.createJMenu("helpmenu");
        JMenuItem about = Utility.createJMenuItem("about", new AboutAction(this));
        JMenuItem license = Utility.createJMenuItem("license", new LicenseAction(this));
        JMenuItem help = Utility.createJMenuItem("helpmenu");
        expertmodeMenu = Utility.createJCheckBoxMenuItem("expertmenu");

        menu.add(about);
        menu.add(license);
        menu.add(help);
        menu.add(new JSeparator());
        menu.add(createLookAndFeelMenu());
        menu.add(new JSeparator());
        menu.add(expertmodeMenu);

        expertmodeMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                expertMenuActionPerformed(((JCheckBoxMenuItem)e.getSource()).getState());
            }
        });
        help.setEnabled(false);

        return menu;
    }

    private JMenu createLookAndFeelMenu(){
        JMenu laf = Utility.createJMenu("lookandfeel");
        ButtonGroup bg = new ButtonGroup();
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        LookAndFeel lookfeel = UIManager.getLookAndFeel();

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String command = e.getActionCommand();
                    UIManager.setLookAndFeel(command);
                    SwingUtilities.updateComponentTreeUI(StigmataFrame.this);
                } catch(Exception ee){
                }
            }
        };
        for(int i = 0; i < info.length; i++){
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(info[i].getName());
            item.setActionCommand(info[i].getClassName());
            item.addActionListener(listener);
            bg.add(item);
            laf.add(item);

            if(info[i].getClassName().equals(lookfeel.getClass().getName())){
                item.setState(true);
            }
        }

        return laf;
    }

    public void setExpertMode(boolean expertmode){
        expertmodeMenu.setState(expertmode);
    }

    private void expertMenuActionPerformed(boolean status){
        control.setExpertMode(status);
    }

    private void showExceptionMessage(Throwable e){
        if(e instanceof BirthmarkElementClassNotFoundException){
            showClassNotFoundMessage((BirthmarkElementClassNotFoundException)e);
        }
        else if(e instanceof OutOfMemoryError){
            showOutOfMemoryError();
        }
        else{
            JTextArea area = new JTextArea(20, 60);
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            e.printStackTrace(out);
            if(e instanceof BirthmarkExtractionFailedException){
                out.println("Causes:");
                for(Throwable t: ((BirthmarkExtractionFailedException)e).getCauses()){
                    t.printStackTrace(out);
                }
            }
            out.close();
            area.setText(writer.toString());
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel("<html><body><p>" + Messages.getString("error.message.contactus") + "</p></body></html>"), BorderLayout.NORTH);
            panel.add(new JScrollPane(area), BorderLayout.CENTER);

            JOptionPane.showMessageDialog(
                this, panel, Messages.getString("error.dialog.title"),
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void showOutOfMemoryError(){
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body><p>");
        sb.append(Messages.getString("error.message.outofmemory"));
        sb.append("</p></body></html>");
        JOptionPane.showMessageDialog(
            this, new String(sb), Messages.getString("error.dialog.title"),
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showClassNotFoundMessage(BirthmarkElementClassNotFoundException e){
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body><p>");
        sb.append(Messages.getString("error.message.classpath"));
        sb.append("</p><ul>");
        for(String name: e.getClassNames()){
            sb.append("<li>").append(name).append("</li>");
        }
        sb.append("</ul></body></html>");
        JOptionPane.showMessageDialog(
            this, new String(sb), Messages.getString("error.dialog.title"),
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void closeTabMenuActionPerformed(){
        int index = tabPane.getSelectedIndex();
        if(index == 0){
            JOptionPane.showMessageDialog(
                this, Messages.getString("cannotclosecontroltab.dialog.message"),
                Messages.getString("cannotclosecontroltab.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
        }
        else{
            tabPane.removeTabAt(index);
        }
    }

    private int getNextCount(String label){
        Integer i = countmap.get(label);
        if(i == null){
            i = new Integer(0);
        }
        i = i + 1;
        countmap.put(label, i);
        return i;
    }

    private void setDefaultUI(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
        }
    }
}
