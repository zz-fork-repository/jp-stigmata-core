package jp.naist.se.stigmata.reader;

/*
 * $Id$
 */

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ClasspathContext implements Iterable<URL>{
    private static ClasspathContext DEFAULT_CONTEXT = new ClasspathContext();

    private ClasspathContext parent;
    private List<URL> classpath = new ArrayList<URL>();
    private ClassLoader loader = null;

    private ClasspathContext(){
    }

    public ClasspathContext(ClasspathContext parent){
        this.parent = parent;
    }

    public static final ClasspathContext getDefaultContext(){
        return DEFAULT_CONTEXT;
    }

    public synchronized void addClasspath(URL url){
        classpath.add(url);
        loader = null;
    }

    public Iterator<URL> iterator(){
        return classpath.iterator();
    }

    public Iterator<URL> classpath(){
        if(parent == null){
            return iterator();
        }
        else{
            final Iterator<URL> parentIterator = parent.classpath();
            final Iterator<URL> thisIterator = iterator();
            return new Iterator<URL>(){
                public boolean hasNext(){
                    boolean next = parentIterator.hasNext();
                    if(!next){
                        next = thisIterator.hasNext();
                    }
                    return next;
                }
                public URL next(){
                    URL nextObject = null;
                    if(parentIterator.hasNext()){
                        nextObject = parentIterator.next();
                    }
                    else{
                        nextObject = thisIterator.next();
                    }
                    return nextObject;
                }
                public void remove(){
                }
            };
        }
    }

    public synchronized ClassLoader createClassLoader(){
        if(loader == null){
            List<URL> list = new ArrayList<URL>();
            for(Iterator<URL> i = classpath(); i.hasNext(); ){
                list.add(i.next());
            }

            loader = new URLClassLoader(list.toArray(new URL[list.size()]), getClass().getClassLoader());
        }
        return loader;
    }

    public ClassFileEntry find(String className) throws ClassNotFoundException{
        ClassLoader loader = createClassLoader();

        URL resource = loader.getResource(className.replace('.', '/') + ".class");
        if(resource != null){
            return new ClassFileEntry(className, resource);
        }
        return null;
    }

    public Class<?> findClass(String className) throws ClassNotFoundException{
        ClassLoader loader = createClassLoader();

        return loader.loadClass(className);
    }
}
