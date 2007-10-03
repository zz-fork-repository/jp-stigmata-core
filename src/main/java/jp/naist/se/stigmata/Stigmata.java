package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.naist.se.stigmata.event.BirthmarkEngineListener;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.ui.swing.ExtensionFilter;
import jp.naist.se.stigmata.utils.ConfigFileExporter;
import jp.naist.se.stigmata.utils.ConfigFileImporter;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class Stigmata{
    /**
     * instance. singleton pattern.
     */
    private static Stigmata stigmata;
    private BirthmarkEnvironment defaultEnvironment;
    private List<BirthmarkEngineListener> listeners = new ArrayList<BirthmarkEngineListener>();

    /**
     * private constructor.
     */
    private Stigmata(){
        configuration();
    }

    /**
     * gets only instance of this class.
     */
    public static synchronized Stigmata getInstance(){
        if(stigmata == null){
            stigmata = new Stigmata();
        }
        return stigmata;
    }

    /**
     * creates a new birthmark context.
     */
    public BirthmarkContext createContext(){
        return new BirthmarkContext(createEnvironment());
    }

    /**
     * creates a new birthmark environment.
     */
    public BirthmarkEnvironment createEnvironment(){
        return new BirthmarkEnvironment(defaultEnvironment);
    }

    /**
     * creates a new birthmark engine.
     */
    public BirthmarkEngine createEngine(){
        return createEngine(createEnvironment());
    }

    /**
     * creates a new birthmark engine with given environment.
     */
    public BirthmarkEngine createEngine(BirthmarkEnvironment environment){
        BirthmarkEngine engine = new BirthmarkEngine(environment);
        for(BirthmarkEngineListener listener: listeners){
            engine.addBirthmarkEngineListener(listener);
        }
        return engine;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.remove(listener);
    }

    public void configuration(){
        configuration(null, false);
    }

    public void configuration(String filePath, boolean resetFlag){
        InputStream target = null;
        if(filePath != null){
            try{
                target = new FileInputStream(filePath);
            } catch(FileNotFoundException e){
                filePath = null;
            }
        }

        if(filePath == null){
            String currentDirectory = System.getProperty("execution.directory");
            if(currentDirectory == null){
                currentDirectory = System.getProperty("user.dir");
            }
            File file = new File(currentDirectory, "stigmata.xml");
            if(!file.exists()){
                file = new File(BirthmarkEnvironment.getStigmataHome(), "stigmata.xml");
                if(!file.exists()){
                    file = null;
                }
            }
            if(file != null){
                try {
                    target = new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    // never throwed this exception;
                    throw new InternalError(ex.getMessage());
                }
            }
        }
        if(target == null || resetFlag){
            target = getClass().getResourceAsStream("/resources/stigmata.xml");
            if(resetFlag){
                defaultEnvironment = null;
                BirthmarkEnvironment.resetSettings();
            }
        }
        initConfiguration(target);
    }

    private void initConfiguration(InputStream in){
        if(defaultEnvironment == null){
            defaultEnvironment = BirthmarkEnvironment.getDefaultEnvironment();
        }
        buildStigmataDirectory(BirthmarkEnvironment.getStigmataHome());

        defaultEnvironment.setClassLoader(buildClassLoader());
        try{
            ConfigFileImporter parser = new ConfigFileImporter(defaultEnvironment);
            parser.parse(in);
        } catch(IOException e){
            throw new ApplicationInitializationError(e);
        }
        for(Iterator<BirthmarkSpi> i = defaultEnvironment.lookupProviders(BirthmarkSpi.class); i.hasNext(); ){
            BirthmarkSpi service = i.next();
            defaultEnvironment.addService(service);
        }
        FormatManager.updateServices(defaultEnvironment);
        exportConfigFile(BirthmarkEnvironment.getStigmataHome(), "stigmata.xml");
    }

    private ClassLoader buildClassLoader(){
        File file = new File(BirthmarkEnvironment.getStigmataHome(), "plugins");
        File[] jarfiles = file.listFiles(new ExtensionFilter("jar"));
        if(jarfiles != null && jarfiles.length > 0){
            try{
                URL[] urls = new URL[jarfiles.length];
                for(int i = 0; i < jarfiles.length; i++){
                    urls[i] = jarfiles[i].toURI().toURL();
                }
                return new URLClassLoader(urls, getClass().getClassLoader());
            } catch(MalformedURLException e){
            }
        }
        return null;
    }

    private void buildStigmataDirectory(String homeDirectory){
        File file = new File(homeDirectory);
        if(file.exists() && file.isFile()){
            File dest = new File(file.getParent(), ".stigmata.back");
            file.renameTo(dest);
        }
        if(!file.exists()){
            file.mkdirs();
        }
        File pluginDir = new File(file, "plugins");
        if(!pluginDir.exists()){
            pluginDir.mkdirs();
        }
    }

    private void exportConfigFile(String parent, String fileName){
        try{
            File file = new File(parent, fileName);
            if(!file.exists()){
                ConfigFileExporter exporter = new ConfigFileExporter(defaultEnvironment);
                exporter.export(new PrintWriter(new FileWriter(file)));
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
