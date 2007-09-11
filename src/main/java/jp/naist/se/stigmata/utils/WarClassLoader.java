package jp.naist.se.stigmata.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class WarClassLoader extends URLClassLoader{
    public WarClassLoader(URL[] urls, ClassLoader parent,
            URLStreamHandlerFactory factory){
        super(urls, parent, factory);
    }

    public WarClassLoader(URL[] urls, ClassLoader parent){
        super(urls, parent);
    }

    public WarClassLoader(URL[] urls){
        super(urls);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        Class<?> clazz = null;
        try{
            clazz = super.findClass(name);
        } catch(Throwable e){
            String path = "WEB-INF/classes/" + name.replace('.', '/') + ".class";
            for(URL url: getURLs()){
                if(url.toString().endsWith(".war")){
                    try{
                        URL newurl = new URL("jar:" + url + "!/" + path);
                        InputStream in = newurl.openStream();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] data = new byte[256];
                        int read = 0;
                        while((read = in.read(data, 0, data.length)) != -1){
                            out.write(data, 0, read);
                        }
                        byte[] classdata = out.toByteArray();
                        in.close();
                        out.close();
                    
                        return defineClass(name, classdata, 0, classdata.length);
                    } catch(IOException exp){
                    }
                }
            }
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }
}
