package jp.naist.se.stigmata.result.history;

/*
 * $Id$
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkStoreTarget;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class ExtractedBirthmarkHistoryManager{
    private static Map<BirthmarkStoreTarget, Class<? extends ExtractedBirthmarkHistory>> TARGETS = new HashMap<BirthmarkStoreTarget, Class<? extends ExtractedBirthmarkHistory>>();
    private BirthmarkEnvironment env;

    static{
        TARGETS.put(BirthmarkStoreTarget.XMLFILE, XmlFileExtractedBirthmarkHistory.class);
        TARGETS.put(BirthmarkStoreTarget.RDB, RDBExtractedBirthmarkHistory.class);
    }

    public ExtractedBirthmarkHistoryManager(BirthmarkEnvironment env){
        this.env = env;    
    }

    public ExtractedBirthmarkHistory getHistory(String id){
        int index = id.indexOf(":");
        String type = id.substring(0, index);
        BirthmarkStoreTarget bst = BirthmarkStoreTarget.valueOf(type);
        String path = id.substring(index + 1);

        try{
            Class<? extends ExtractedBirthmarkHistory> historyClass = TARGETS.get(bst);
            Constructor<? extends ExtractedBirthmarkHistory> constructor = historyClass.getConstructor(String.class);
            return constructor.newInstance(path);
        } catch(IllegalArgumentException e){
        } catch(InstantiationException e){
        } catch(IllegalAccessException e){
        } catch(InvocationTargetException e){
        } catch(SecurityException e){
        } catch(NoSuchMethodException e){
        }
        return null;
    }

    public synchronized String[] getHistoryIds(){
        List<String> values = new ArrayList<String>();
        addValuesFromProperty(values);
        addValuesFromSystemFile(values);

        char separator = File.separatorChar;
        values.add(
            "XMLFILE:" + BirthmarkEnvironment.getStigmataHome()
            + separator + "extracted_birthmarks"
        );
        return values.toArray(new String[values.size()]);
    }

    private void addValuesFromSystemFile(List<String> values){
        File file = new File(BirthmarkEnvironment.getStigmataHome(), "storelocations.txt");
        if(file.exists()){
            try{
                BufferedReader in = new BufferedReader(new FileReader(file));
                String line;
                while((line = in.readLine()) != null){
                    values.add(line);
                }
            } catch(IOException e){
            }
        }
    }

    private void addValuesFromProperty(List<String> values){
        String path = env.getProperty("extracted.birthmark.store.locations");
        if(path != null){
            addValuesFromProperty(values);
            String[] paths = path.split(", *");
            for(String p: paths){
                values.add(p);
            }
        }
    }
}
