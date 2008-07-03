package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkSet;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ClassNameObfuscator{
    private Map<String, String> nameMapping = new HashMap<String, String>();

    public void outputNameMappings(File file) throws IOException{
        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(file));
            for(String oldName: nameMapping.keySet()){
                String newName = nameMapping.get(oldName);
                out.print(oldName);
                out.print(",");
                out.println(newName);
            }

        }finally{
            if(out != null){
                out.close();
            }
        }
    }

    public BirthmarkSet obfuscateClassName(BirthmarkSet orig){
        String newName = nameMapping.get(orig.getName());
        if(newName == null){
            newName = String.format("C%04d", new Object[] { new Integer(nameMapping.size() + 1), });
            nameMapping.put(orig.getName(), newName);
        }

        BirthmarkSet newSet = new BirthmarkSet(newName, orig.getLocation());
        for(Birthmark birthmark: orig){
            newSet.addBirthmark(birthmark);
        }
        return newSet;
    }

}
