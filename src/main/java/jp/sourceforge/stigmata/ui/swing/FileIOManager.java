package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import jp.sourceforge.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FileIOManager{
    private Component parent;
    private File currentDirectory;
    private BirthmarkEnvironment env;

    public FileIOManager(Component parent, BirthmarkEnvironment env){
        this.parent = parent;
        this.env = env;
        if(env.getProperty(".current.directory") != null){
            currentDirectory = new File(env.getProperty(".current.directory"));
        }
        if(env.getProperty("startup.directory") != null){
            currentDirectory = new File(env.getProperty("startup.directory"));
        }
        if(currentDirectory == null && System.getProperty("execution.directory") != null){
            currentDirectory = new File(System.getProperty("execution.directory"));
        }
        if(currentDirectory == null){
            currentDirectory = new File(".");
        }
    }

    public File getCurrentDirectory(){
        return currentDirectory;
    }

    public void setCurrentDirectory(File directory) throws IllegalArgumentException{
        if(!directory.isDirectory()){
            throw new IllegalArgumentException(directory.getName() + " is not directory");
        }
        this.currentDirectory = directory;
        env.addProperty(".current.directory", directory.getAbsolutePath());
    }

    public File findFile(boolean open){
        return findFile(open, new String[0], "");
    }

    public File[] openFiles(FileFilter[] filters, boolean multi, boolean directory){
        JFileChooser chooser = new JFileChooser(getCurrentDirectory());
        if(filters != null){
            for(int i = 0; i < filters.length; i++){
                chooser.addChoosableFileFilter(filters[i]);
            }
        }
        chooser.setMultiSelectionEnabled(multi);
        if(directory){
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        else{
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        int returnValue = chooser.showOpenDialog(SwingUtilities.getRootPane(parent));
        if(returnValue == JFileChooser.APPROVE_OPTION){
            setCurrentDirectory(chooser.getCurrentDirectory());
            return chooser.getSelectedFiles();
        }
        return new File[0];
    }

    public File findFile(boolean open, String[] exts, String desc){
        JFileChooser chooser = new JFileChooser(getCurrentDirectory());
        if(exts != null){
            MessageFormat formatter = new MessageFormat(desc);
            for(int i = 0; i < exts.length; i++){
                chooser.addChoosableFileFilter(
                    new ExtensionFilter(exts[i], formatter.format(new Object[] { exts[i], }))
                );
            }
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
