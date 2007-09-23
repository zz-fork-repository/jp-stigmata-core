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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jp.naist.se.stigmata.Stigmata;
import jp.naist.se.stigmata.reader.ClasspathContext;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ClasspathSettingsPane extends JPanel{
    private static final long serialVersionUID = 320973463423634L;

    private StigmataFrame stigmata;
    private TargetSelectionPane classpath;
    private TargetSelectionPane bootClasspath;

    public ClasspathSettingsPane(StigmataFrame frame){
        this.stigmata = frame;

        initLayouts();
    }

    public void updateClasspathContext(ClasspathContext context){
        String[] cplist = classpath.getValues();
        if(cplist != null && cplist.length >= 0){
            for(int i = 0; i < cplist.length; i++){
                try{
                    URL url = null;
                    try{
                        url = new URL(cplist[i]);
                    } catch(MalformedURLException e){
                        url = new File(cplist[i]).toURI().toURL();
                    }
                    context.addClasspath(url);
                }catch(IOException ee){
                }
            }
        }
    }

    public void reset(){
        classpath.removeAllElements();
        bootClasspath.removeAllElements();
        try{
            ClasspathContext context = stigmata.getEnvironment().getClasspathContext();
            for(Iterator<URL> i = context.classpath(); i.hasNext(); ){
                classpath.addValue(i.next().toString());
            }

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
            ClasspathContext b = Stigmata.getInstance().createEnvironment().getClasspathContext();
            ClasspathContext bytecode = new ClasspathContext(b);
            String[] path = classpath.getValues();
            for(String cp: path){
                bytecode.addClasspath(new File(cp).toURI().toURL());
            }
            return bytecode.findClass(className) != null;
        }catch(ClassNotFoundException e){
        }catch(MalformedURLException e){
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
                String t = text.getText().trim();
                boolean flag = findClass(t);
                String message = Messages.getString("classpathchecker.found.tooltip");
                if(flag){
                    label.setIcon(Utility.getIcon("classpathchecker.found.icon"));
                }
                else{
                    label.setIcon(Utility.getIcon("classpathchecker.notfound.icon"));
                    message = Messages.getString("classpathchecker.notfound.tooltip");
                }
                label.setToolTipText(message);
                String dm = String.format(
                    "<html><body><dl><dt>%s</dt><dd>%s</dd></body></html>", t, message
                );
                JOptionPane.showMessageDialog(
                    stigmata, dm, Messages.getString("classpathchecker.dialog.title"),
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        };
        findButton.addActionListener(action);
        text.addActionListener(action);
    }
}