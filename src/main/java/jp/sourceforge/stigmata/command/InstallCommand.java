package jp.sourceforge.stigmata.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.Stigmata;
import jp.sourceforge.stigmata.utils.Utility;

/**
 * 
 * @author Haruaki Tamada
 */
public class InstallCommand extends AbstractStigmataCommand{
    @Override
    public boolean isAvailableArguments(String[] args){
        return args.length > 0;
    }

    @Override
    public String getCommandString(){
        return "install";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        File pluginsDir = new File(BirthmarkEnvironment.getStigmataHome(), "plugins");
        BirthmarkEnvironment env = context.getEnvironment();

        for(int i = 0; i < args.length; i++){
            File pluginSource = new File(args[i]);
            File pluginDest = new File(pluginsDir, pluginSource.getName());

            if(!Utility.isStigmataPluginJarFile(pluginSource)){
                throw new IllegalArgumentException(pluginSource + ": not stigmata plugin file.");
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
