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
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jp.naist.se.stigmata.reader.ClasspathContext;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ClasspathSettingsPane extends JPanel implements SettingsExportable{
    private static final long serialVersionUID = 320973463423634L;

    private StigmataFrame stigmata;
    private TargetSelectionPane classpath;
    private TargetSelectionPane bootClasspath;

    public ClasspathSettingsPane(StigmataFrame frame){
        this.stigmata = frame;

        initLayouts();
    }

    public void exportSettings(PrintWriter out) throws IOException{
        String[] cplist = classpath.getValues();
        if(cplist != null && cplist.length > 0){
            out.println("  <classpath-list>");
            for(int i = 0; i < cplist.length; i++){
                out.print("<classpath>");
                out.print(cplist[i]);
                out.println("</classpath>");
            }
            out.println("  </classpath-list>");
        }
    }

    public void updateClasspathContext(ClasspathContext context){
        String[] cplist = classpath.getValues();
        if(cplist != null && cplist.length >= 0){
            for(int i = 0; i < cplist.length; i++){
                try{
                    context.addClasspath(new File(cplist[i]).toURI().toURL());
                }catch(IOException ee){
                }
            }
        }
    }

    public void reset(){
        try{
            addClasspath(bootClasspath, System.getProperty("java.class.path"));
            addClasspath(bootClasspath, System.getProperty("sun.boot.class.path"));
        } catch(SecurityException e){
            e.printStackTrace();
        }
    }
    
    private void addClasspath(TargetSelectionPane target, String classpath){
        if(classpath != null){
            target.addValues(classpath.split(System.getProperty("path.separator")));
        }
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

    private void initLayouts(){
        JComponent panel = new JPanel(new GridLayout(1, 2));
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

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

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
    }
}