package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * File filter by file extension.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ExtensionFilter extends FileFilter{
    private List<String> extensions = new ArrayList<String>();

    private String description = null;

    public ExtensionFilter(){
    }

    public ExtensionFilter(String[] exts){
        this(exts, null);
    }

    public ExtensionFilter(String[] exts, String description){
        if(exts != null){
            for(String ext: exts){
                addExtension(ext);
            }
        }
        setDescription(description);
    }

    public void addExtension(String ext){
        extensions.add(ext);
    }

    public boolean accept(File f){
        String fileName = f.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        boolean flag = false;
        for(String ext: extensions){
            if(ext.equals(extension)){
                flag = true;
                break;
            }
        }
        if(extensions.size() == 0){
            flag = true;
        }

        return flag || f.isDirectory();
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
