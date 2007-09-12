package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.event.BirthmarkEngineListener;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.ConfigFileImporter;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class Stigmata2{
    private static Stigmata2 stigmata = new Stigmata2();
    private BirthmarkEnvironment defaultEnvironment;
    private BirthmarkEngine engine;

    private Stigmata2(){
        configuration();
        engine = new BirthmarkEngine(defaultEnvironment);
    }

    public static Stigmata2 getInstance(){
        return stigmata;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        engine.addBirthmarkEngineListener(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        engine.removeBirthmarkEngineListener(listener);
    }

    public ComparisonResultSet compare(String[] files, BirthmarkContext context) throws BirthmarkException{
        return engine.compare(files, context);
    }

    public ComparisonResultSet compare(String[] fileX, String[] fileY, BirthmarkContext context) throws BirthmarkException{
        return engine.compare(fileX, fileY, context);
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
