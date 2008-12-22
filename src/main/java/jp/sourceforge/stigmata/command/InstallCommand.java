package jp.sourceforge.stigmata.command;

/*
 * $Id$
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.Stigmata;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class InstallCommand extends AbstractStigmataCommand{
    public boolean isAvailableArguments(String[] args){
        return args.length > 0;
    }

    @Override
    public String getCommandString(){
        return "install";
    }

    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        File pluginsDir = new File(BirthmarkEnvironment.getStigmataHome(), "plugins");
        BirthmarkEnvironment env = context.getEnvironment();

        for(int i = 0; i < args.length; i++){
            File pluginSource = new File(args[i]);
            File pluginDest = new File(pluginsDir, pluginSource.getName());

            if(!pluginSource.getName().endsWith(".jar")){
                throw new IllegalArgumentException("plugin is allowed only jar archive: " + args[i]);
            }
            if(pluginDest.exists()){
                String override = env.getProperty("override.exists.plugin");
                if(override != null &&
                   (override.equalsIgnoreCase("true") || override.equalsIgnoreCase("yes"))){
                    pluginDest.delete();
                }
                else{
                    File backupFile = new File(pluginDest.getParent(), pluginDest.getName() + ".back");
                    if(backupFile.exists()) backupFile.delete();
                    pluginDest.renameTo(backupFile);
                }
            }

            byte[] data = new byte[256];
            int read;

            try{
                InputStream in = new FileInputStream(pluginSource);
                OutputStream out = new FileOutputStream(pluginDest);

                while((read = in.read(data)) != -1){
                    out.write(data, 0, read);
                }
                in.close();
                out.close();
            } catch(IOException e){
            }
        }
    }
}
