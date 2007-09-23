package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.event.BirthmarkEngineListener;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.ConfigFileImporter;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class Stigmata{
    private static Stigmata stigmata;
    private BirthmarkEnvironment defaultEnvironment;
    private List<BirthmarkEngineListener> listeners = new ArrayList<BirthmarkEngineListener>();

    private Stigmata(){
        configuration();
    }

    public static synchronized Stigmata getInstance(){
        if(stigmata == null){
            stigmata = new Stigmata();
        }
        return stigmata;
    }

    public BirthmarkContext createContext(){
        return new BirthmarkContext(createEnvironment());
    }

    public BirthmarkEnvironment createEnvironment(){
        return new BirthmarkEnvironment(defaultEnvironment);
    }

    public BirthmarkEngine createEngine(){
        BirthmarkEngine engine = new BirthmarkEngine(createEnvironment());
        return engine;
    }

    public BirthmarkEngine createEngine(BirthmarkEnvironment environment){
        BirthmarkEngine engine = new BirthmarkEngine(environment);
        return engine;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.remove(listener);
    }

    public void configuration(){
        configuration(null);
    }

    public void configuration(String filePath){
        InputStream target = null;
        if(filePath != null){
            try{
                target = new FileInputStream(filePath);
            } catch(FileNotFoundException e){
                filePath = null;
            }
        }

        if(filePath == null){
            File file = new File("stigmata.xml");
            if(!file.exists()){
                file = new File(System.getProperty("user.home"), ".stigmata.xml");
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
        if(target == null){
            target = getClass().getResourceAsStream("/resources/stigmata.xml");
        }
        initConfiguration(target);
    }

    private void initConfiguration(InputStream in){
        if(defaultEnvironment == null){
            defaultEnvironment = BirthmarkEnvironment.getDefaultEnvironment();
        }
        try {
            ConfigFileImporter parser = new ConfigFileImporter(defaultEnvironment);
            parser.parse(in);
        } catch(IOException e){
            throw new ApplicationInitializationError(e);
        }
        for(Iterator<BirthmarkSpi> i = ServiceRegistry.lookupProviders(BirthmarkSpi.class); i.hasNext(); ){
            BirthmarkSpi service = i.next();
            defaultEnvironment.addService(service);
        }
    }
}
