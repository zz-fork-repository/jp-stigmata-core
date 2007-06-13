package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FileIOManager{
    private Component parent;
    private File currentDirectory;

    public FileIOManager(Component parent){
        this.parent = parent;
        currentDirectory = new File(".");
    }

    public File getCurrentDirectory(){
        return currentDirectory;
    }

    public void setCurrentDirectory(File directory){
        if(!directory.isDirectory()){
            JOptionPane.showMessageDialog(
                parent, 
                Messages.getString("notdirectory.dialog.message", directory.getName()),
                Messages.getString("notdirectory.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        this.currentDirectory = directory;
    }

    public File findFile(boolean open, String[] exts, String desc){
        JFileChooser chooser = new JFileChooser(getCurrentDirectory());
        for(int i = 0; i < exts.length; i++){
            chooser.addChoosableFileFilter(
                new ExtensionFilter(exts[i], MessageFormat.format(desc, exts[i]))
            );
        }
        int returnValue = -1;
        if(open){
            returnValue = chooser.showOpenDialog(SwingUtilities.getRootPane(parent));
        }
        else{
            returnValue = chooser.showSaveDialog(SwingUtilities.getRootPane(parent));
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
}
