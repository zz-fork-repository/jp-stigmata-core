package jp.naist.se.stigmata.ui.swing;

/*
 * $Id: ExtensionFilter.java 76 2006-09-08 17:59:27Z harua-t $
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * File filter by file extension.
 * 
 * @author Haruaki TAMADA
 * @version $Revision: 76 $ $Date: 2006-09-09 02:59:27 +0900 (Sat, 09 Sep 2006) $
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
