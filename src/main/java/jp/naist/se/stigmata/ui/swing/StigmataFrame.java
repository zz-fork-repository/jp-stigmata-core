package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElementClassNotFoundException;
import jp.naist.se.stigmata.BirthmarkExtractionException;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.Stigmata;
import jp.naist.se.stigmata.filter.FilteredComparisonResultSet;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.spi.ResultFormatSpi;


/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class StigmataFrame extends JFrame implements CurrentDirectoryHolder{
    private static final long serialVersionUID = 92345543665342134L;

    private static List<JFrame> frameList = new ArrayList<JFrame>();
    private JTabbedPane tabPane;
    private JMenuItem closeTabMenu;
    private JCheckBoxMenuItem expertmodeMenu;
    private Stigmata stigmata;
    private BirthmarkContext context;
    private ControlPane control;
    private File currentDirectory;
    private int extractCount = 0;
    private int compareCount = 0;
    private int compareDetail = 0;
    private int graphCount = 0;
    private int comparePair = 0;

    public StigmataFrame(){
        stigmata = Stigmata.getInstance();
        context = stigmata.createContext();

        initLayouts();
    }

    public StigmataFrame(Stigmata stigmata){
        this(stigmata, stigmata.createContext());
    }

    public StigmataFrame(Stigmata stigmata, BirthmarkContext context){
        this.stigmata = stigmata;
        this.context = context;

        initLayouts();
    }

    public File getCurrentDirectory(){
        return currentDirectory;
    }

    public void setCurrentDirectory(File directory){
        if(!directory.isDirectory()){
            JOptionPane.showMessageDialog(
                this, 
                Messages.getString("notdirectory.dialog.message", directory.getName()),
                Messages.getString("notdirectory.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        this.currentDirectory = directory;
    }

    public Stigmata getStigmata(){
        return stigmata;
    }

    public BirthmarkContext getContext(){
        return context;
    }

    public File getOpenFile(String[] exts, String desc){
        return findFile(true, exts, desc);
    }

    public File getSaveFile(String[] exts, String desc){
        return findFile(false, exts, desc);
    }

    public void addBirthmarkServiceListener(BirthmarkServiceListener listener){
        control.addBirthmarkServiceListener(listener);
    }

    public void removeBirthmarkServiceListener(BirthmarkServiceListener listener){
        control.removeBirthmarkServiceListener(listener);
    }

    public void saveAction(BirthmarkDataWritable writable){
        File file = getSaveFile(Messages.getStringArray("store.extensions"),
                                Messages.getString("store.description"));
        if(file != null){
            String name = file.getName();
            String ext = name.substring(name.lastIndexOf('.') + 1, name.length());

            ResultFormatSpi result = FormatManager.getInstance().getService(ext);
            if(result == null){
                result = FormatManager.getDefaultFormatService();
            }

            PrintWriter out = null;
            try{
                out = new PrintWriter(new FileWriter(file));
                writable.writeData(out, result);
            }catch(IOException e){
            }finally{
                if(out != null){
                    out.close();
                }
            }
        }
    }

    public void compareDetails(BirthmarkSet target1, BirthmarkSet target2, BirthmarkContext context){
        PairComparisonPane detail = new PairComparisonPane(
            this, new ComparisonPair(target1, target2, context)
        );
        compareDetail++;

        Utility.addNewTab("comparedetail", tabPane, detail,
            new Object[] { new Integer(compareDetail), },
            new Object[] {
                Utility.array2String(target1.getBirthmarkTypes()),
                target1.getClassName(),
                target2.getClassName(),
            }
        );
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void compareRoundRobin(String[] birthmarks, String[] targetX, String[] targetY, 
                                   BirthmarkContext context){
        try{
            BirthmarkSet[] x = stigmata.extract(birthmarks, targetX, context);
            BirthmarkSet[] y = stigmata.extract(birthmarks, targetY, context);

            RoundRobinComparisonResultPane compare = new RoundRobinComparisonResultPane(this, context, x, y);
            compareCount++;
            Utility.addNewTab(
                "compare", tabPane, compare,
                new Object[] { new Integer(compareCount), },
                new Object[] {
                    Utility.array2String(birthmarks),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } catch(Exception e){
            showExceptionMessage(e);
        }
    }

    public void compareRoundRobin(String[] birthmarks, String[] targetX, String[] targetY, 
                                   String[] filterNames, BirthmarkContext context){
        try{
            BirthmarkSet[] x = stigmata.extract(birthmarks, targetX, context);
            BirthmarkSet[] y = stigmata.extract(birthmarks, targetY, context);
            ComparisonPairFilterSet[] filters = context.getFilterManager().getFilterSets(filterNames);

            ComparisonResultSet resultset = stigmata.compare(x, y, context);
            FilteredComparisonResultSet fcrs = new FilteredComparisonResultSet(resultset, filters);
            compareCount++;
            Utility.addNewTab(
                "compare", tabPane, new PairComparisonResultSetPane(this, fcrs),
                new Object[] { new Integer(compareCount), },
                new Object[] {
                    Utility.array2String(birthmarks),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        } catch(Exception e){
            showExceptionMessage(e);
        }
    }

    public void compareGuessedPair(String[] birthmarks, String[] targetX, String[] targetY,
                                   BirthmarkContext context){
        try{
            BirthmarkSet[] x = stigmata.extract(birthmarks, targetX, context);
            BirthmarkSet[] y = stigmata.extract(birthmarks, targetY, context);
            comparePair++;

            ComparisonResultSet resultset = new CertainPairComparisonResultSet(x, y, context);
            Utility.addNewTab("comparepair", tabPane,
                new PairComparisonResultSetPane(this, resultset),
                new Object[] { new Integer(comparePair), },
                new Object[] {
                    Utility.array2String(birthmarks),
                    Utility.array2String(targetX),
                    Utility.array2String(targetY),
                }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        }catch(Exception e){
            showExceptionMessage(e);
        }
    }

    public void compareSpecifiedPair(String[] birthmarks, String[] targetX, String[] targetY,
                                     BirthmarkContext context){
        File file = getOpenFile(Messages.getStringArray("comparemapping.extension"),
                                Messages.getString("comparemapping.description"));

        if(file != null){
            Map<String, String> mapping = constructMapping(file);

            try{
                BirthmarkSet[] x = stigmata.extract(birthmarks, targetX, context);
                BirthmarkSet[] y = stigmata.extract(birthmarks, targetY, context);
                comparePair++;
                ComparisonResultSet resultset = new CertainPairComparisonResultSet(x, y, mapping, context);

                Utility.addNewTab(
                    "comparepair", tabPane,
                    new PairComparisonResultSetPane(this, resultset),
                    new Object[] { new Integer(comparePair), },
                    new Object[] {
                        Utility.array2String(birthmarks),
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
        comparePair++;
        Utility.addNewTab(
            "comparisonresultset", tabPane,
            new PairComparisonResultSetPane(this, resultset),
            new Object[] { new Integer(comparePair), }, null
        );
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void showSimilarityDistributionGraph(Map<Integer, Integer> distributions){
        final SimilarityGraphPane graph = new SimilarityGraphPane(distributions);
        JPanel graphPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        graphPanel.add(graph);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(graphPanel, BorderLayout.CENTER);

        JButton save = Utility.createButton("savegraph");
        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(save);
        south.add(Box.createHorizontalGlue());
        panel.add(south, BorderLayout.SOUTH);
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                storeGraphImage(graph);
            }
        });

        graphCount++;
        Utility.addNewTab("graph", tabPane, panel, new Object[] { new Integer(graphCount), }, null);
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void extract(String[] birthmarks, String[] targets, BirthmarkContext context){
        try{
            BirthmarkSet[] holders = stigmata.extract(birthmarks, targets, context);
            extractCount++;

            BirthmarkExtractionResultPane viewer = new BirthmarkExtractionResultPane(this, context, holders);
            Utility.addNewTab(
                "extract", tabPane, viewer,
                new Object[] { new Integer(extractCount), },
                new Object[] { Utility.array2String(birthmarks), Utility.array2String(targets), }
            );
            tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        }catch(Exception e){
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

    private void storeGraphImage(SimilarityGraphPane graph){
        String[] exts = graph.getSupportedFormats();
        File file = getSaveFile(
            exts, Messages.getString("savegraph.description")
        );
        try{
            graph.storeGraphImage(file);
        } catch(IOException e){
            JOptionPane.showMessageDialog(
                this,
                Messages.getString("error.io", e.getMessage()),
                Messages.getString("error.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private File findFile(boolean open, String[] exts, String desc){
        JFileChooser chooser = new JFileChooser(getCurrentDirectory());
        for(int i = 0; i < exts.length; i++){
            chooser.addChoosableFileFilter(
                new ExtensionFilter(exts[i], MessageFormat.format(desc, exts[i]))
            );
        }
        int returnValue = -1;
        if(open){
            returnValue = chooser.showOpenDialog(SwingUtilities.getRootPane(this));
        }
        else{
            returnValue = chooser.showSaveDialog(SwingUtilities.getRootPane(this));
        }
        if(returnValue == JFileChooser.APPROVE_OPTION){
            setCurrentDirectory(chooser.getCurrentDirectory());
            File file = chooser.getSelectedFile();
            if(!open){
                FileFilter filter = chooser.getFileFilter();
                if(filter instanceof ExtensionFilter){
                    ExtensionFilter ef = (ExtensionFilter)filter;
                    if(!filter.accept(file)){
                        String[] extensions = ef.getExtensions();
                        file = setExtension(file, extensions[0]);
                    }
                }
            }
            return file;
        }
        return null;
    }

    private File setExtension(File file, String ext){
        String name = file.getName();
        int index = name.lastIndexOf('.');
        String n = name;
        if(index > 0){
            n = n.substring(0, index);
        }
        name = n + '.' + ext;
        return new File(file.getParentFile(), name);
    }

    private void initLayouts(){
        setTitle(Messages.getString("stigmata.frame.title"));
        initComponents();

        Utility.addNewTab("control", tabPane, control = new ControlPane(this), null, null);
        control.inititalize();
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);

        currentDirectory = new File(".");

        setSize(900, 600);
        frameList.add(this);
    }

    private void initComponents(){
        tabPane = new JTabbedPane();

        add(tabPane, BorderLayout.CENTER);

        tabPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent arg0){
                String title = tabPane.getTitleAt(tabPane.getSelectedIndex());
                closeTabMenu.setEnabled(!title.equals(Messages.getString("control.tab.label")));
            }
        });

        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createHelpMenu());

        setJMenuBar(menubar);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent evt){
                formWindowClosed(evt);
            }
        });
    }

    private JMenu createFileMenu(){
        JMenu fileMenu = Utility.createJMenu("fileMenu");
        JMenuItem newFrameMenu = Utility.createJMenuItem("newframe");
        JMenuItem exportMenu = Utility.createJMenuItem("exportsetting");
        JMenuItem closeTabMenu = Utility.createJMenuItem("closetab");
        JMenuItem closeMenu = Utility.createJMenuItem("closeframe");
        JMenuItem exitMenu = Utility.createJMenuItem("exit");
        this.closeTabMenu = closeTabMenu;

        fileMenu.add(newFrameMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(exportMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeTabMenu);
        fileMenu.add(closeMenu);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenu);

        newFrameMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                newFrameMenuActionPerformed(evt);
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
                closeMenuActionPerformed(evt);
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
        JMenuItem about = Utility.createJMenuItem("about");
        JMenuItem license = Utility.createJMenuItem("license");
        JMenuItem help = Utility.createJMenuItem("helpmenu");
        JMenu laf = Utility.createJMenu("lookandfeel");
        expertmodeMenu = Utility.createJCheckBoxMenuItem("expertmenu");

        menu.add(about);
        menu.add(license);
        menu.add(help);
        menu.add(new JSeparator());
        menu.add(laf);
        menu.add(new JSeparator());
        menu.add(expertmodeMenu);

        about.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                aboutMenuActionPerformed();
            }
        });

        license.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                licenseMenuActionPerformed();
            }
        });
        expertmodeMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                expertMenuActionPerformed(((JCheckBoxMenuItem)e.getSource()).getState());
            }
        });

        final UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for(int i = 0; i < info.length; i++){
            final int index = i;
            JMenuItem item = new JMenuItem(info[i].getName());
            laf.add(item);
            item.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        UIManager.setLookAndFeel(info[index].getClassName());
                        SwingUtilities.updateComponentTreeUI(StigmataFrame.this);
                    }catch(Exception ee){
                    }
                }
            });
        }
        help.setEnabled(false);

        return menu;
    }

    public void setExpertMode(boolean expertmode){
        expertmodeMenu.setState(expertmode);
    }

    private void expertMenuActionPerformed(boolean status){
        control.setExpertMode(status);
    }

    private void aboutMenuActionPerformed(){
        Package p = getClass().getPackage();
        JPanel panel = new JPanel(new BorderLayout());
        JLabel logo = new JLabel(Utility.getIcon("stigmata.logo"));
        panel.add(logo, BorderLayout.NORTH);

        String aboutMessage = loadString("/resources/about.txt");
        aboutMessage = aboutMessage.replace("${implementation.version}", p.getImplementationVersion());
        aboutMessage = aboutMessage.replace("${implementation.vendor}",  p.getImplementationVendor());
        aboutMessage = aboutMessage.replace("${implementation.title}",   p.getImplementationTitle());

        JTextArea text = new JTextArea(aboutMessage);
        text.setEditable(false);
        text.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(text);
        panel.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this, panel, Messages.getString("about.dialog.title"),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void licenseMenuActionPerformed(){
        JTextArea area = new JTextArea();
        area.setText(loadString("/META-INF/license.txt"));
        Font f = area.getFont();
        area.setFont(new Font("Monospaced", f.getStyle(), f.getSize()));
        area.setEditable(false);
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(area);
        scroll.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(
            this, scroll, Messages.getString("license.dialog.title"),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showExceptionMessage(Exception e){
       if(e instanceof BirthmarkElementClassNotFoundException){
           showClassNotFoundMessage((BirthmarkElementClassNotFoundException)e);
           return;
       }
       JTextArea area = new JTextArea(20, 60);
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        e.printStackTrace(out);
        if(e instanceof BirthmarkExtractionException){
               out.println("Causes:");
               for(Throwable t: ((BirthmarkExtractionException)e).getCauses()){
                       t.printStackTrace(out);
               }
        }
        out.close();
        area.setText(writer.toString());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(Messages.getString("error.message.contactus")), BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this, panel, Messages.getString("error.dialog.title"),
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

    private String loadString(String loadFrom){
        try{
            String line;
            URL url = getClass().getResource(loadFrom);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            while((line = in.readLine()) != null){
                out.println(line);
            }
            out.close();
            in.close();

            return writer.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void formWindowClosed(WindowEvent evt){
        frameList.remove(this);
        if(frameList.size() == 0){
            System.exit(1);
        }
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

    private void closeMenuActionPerformed(ActionEvent evt){
        setVisible(false);
        dispose();

        frameList.remove(this);
        if(frameList.size() == 0){
            System.exit(1);
        }
    }

    private void newFrameMenuActionPerformed(ActionEvent evt){
        StigmataFrame frame = new StigmataFrame(stigmata, context);
        frame.setVisible(true);
    }
}
