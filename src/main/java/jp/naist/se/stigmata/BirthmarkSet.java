package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class manages a set of birthmarks which extracted from a Java class file.
 *
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class BirthmarkSet implements Iterable<Birthmark>{
    /**
     * class name.
     */
    private String className;

    /**
     * location of class file is loaded from.
     */
    private URL location;

    /**
     * map for birthmarks.
     */
    private Map<String, Birthmark> birthmarks = new HashMap<String, Birthmark>();

    /**
     * constructor.
     */
    public BirthmarkSet(String className, URL location){
        this.className = className;
        this.location = location;
    }

    /**
     * return the sum of all element count of birthmarks this instance has.
     */
    public int getSumOfElementCount(){
        int count = 0;
        for(Iterator<String> i = birthmarkTypes(); i.hasNext();){
            Birthmark birthmark = getBirthmark(i.next());
            count += birthmark.getElementCount();
        }
        return count;
    }

    /**
     * return the number of birthmarks.
     */
    public int getBirthmarksCount(){
        return birthmarks.size();
    }

    /**
     * return the class name.
     */
    public String getClassName(){
        return className;
    }

    /**
     * return the location.
     */
    public URL getLocation(){
        return location;
    }

    /**
     * add given birthmark to this instance.
     */
    public void addBirthmark(Birthmark birthmark){
        birthmarks.put(birthmark.getType(), birthmark);
    }

    /**
     * return the given type of birthmark.
     */
    public Birthmark getBirthmark(String type){
        return birthmarks.get(type);
    }

    /**
     * return the all birthmarks this instance managed.
     */
    public Birthmark[] getBirthmarks(){
        Birthmark[] b = new Birthmark[getBirthmarksCount()];
        int index = 0;
        for(Iterator<String> i = birthmarkTypes(); i.hasNext();){
            b[index] = getBirthmark(i.next());
            index++;
        }
        return b;
    }

    public Iterator<Birthmark> iterator(){
        return birthmarks.values().iterator();
    }

    /**
     * return types of birthmarks.
     */
    public Iterator<String> birthmarkTypes(){
        return birthmarks.keySet().iterator();
    }

    /**
     * return types of birthmarks.
     */
    public synchronized String[] getBirthmarkTypes(){
        return birthmarks.keySet().toArray(new String[birthmarks.size()]);
    }
}
