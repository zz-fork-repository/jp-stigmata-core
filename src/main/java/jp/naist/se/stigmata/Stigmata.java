package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.reader.ClassFileArchive;
import jp.naist.se.stigmata.reader.ClassFileEntry;
import jp.naist.se.stigmata.reader.DefaultClassFileArchive;
import jp.naist.se.stigmata.reader.JarClassFileArchive;
import jp.naist.se.stigmata.reader.WarClassFileArchive;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class Stigmata{
    private static final Stigmata instance = new Stigmata();

    private BirthmarkContext defaultContext = BirthmarkContext.getDefaultContext();

    private Stigmata(){
        for(Iterator<BirthmarkSpi> i = ServiceRegistry.lookupProviders(BirthmarkSpi.class); i.hasNext(); ){
            BirthmarkSpi service = i.next();
            defaultContext.addService(service);
        }
    }

    public static Stigmata getInstance(){
        return instance;
    }

    BirthmarkContext getDefaultContext(){
        return this.defaultContext;
    }

    public BirthmarkContext createContext(){
        return new BirthmarkContext(getDefaultContext());
    }

    public BirthmarkSet[] extract(String[] birthmarks, String[] files) throws IOException{
        return extract(birthmarks, files, createContext());
    }

    public BirthmarkSet[] extract(String[] birthmarks, String[] files,
            BirthmarkContext context) throws IOException{
        List<ClassFileArchive> archives = new ArrayList<ClassFileArchive>();
        ClasspathContext bytecode = context.getBytecodeContext();

        for(int i = 0; i < files.length; i++){
            if(files[i].endsWith(".class")){
                archives.add(new DefaultClassFileArchive(files[i]));
            }
            else if(files[i].endsWith(".jar") || files[i].endsWith(".zip")){
                archives.add(new JarClassFileArchive(files[i]));
                bytecode.addClasspath(new File(files[i]).toURI().toURL());
            }
            else if(files[i].endsWith(".war")){
                archives.add(new WarClassFileArchive(files[i]));
            }
        }
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();

        for(ClassFileArchive archive: archives){
            for(Iterator<ClassFileEntry> entries = archive.entries(); entries.hasNext(); ){
                ClassFileEntry entry = entries.next();
                BirthmarkSet holder = new BirthmarkSet(entry.getClassName(), entry.getLocation());
                extractBirthmark(birthmarks, holder.getLocation().openStream(), holder, context);
                list.add(holder);
            }
        }

        return list.toArray(new BirthmarkSet[list.size()]);
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders) throws IOException{
        return compare(holders, createContext());
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders, BirthmarkContext context) throws IOException{
        ComparisonResultSet result = new RoundRobinComparisonResultSet(holders, context, true);

        return result;
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders1, BirthmarkSet[] holders2) throws IOException{
        return compare(holders1, holders2, createContext());
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders1, BirthmarkSet[] holders2, BirthmarkContext context) throws IOException{
        ComparisonResultSet result = new RoundRobinComparisonResultSet(holders1, holders2, context);

        return result;
    }

    public double compare(BirthmarkSet h1, BirthmarkSet h2){
        return compare(h1, h2, createContext());
    }

    public double compare(BirthmarkSet h1, BirthmarkSet h2, BirthmarkContext context){
        List<Double> list = new ArrayList<Double>();
        int count = 0;
        for(Iterator<String> i = h1.birthmarkTypes(); i.hasNext(); ){
            String type = i.next();
            Birthmark b1 = h1.getBirthmark(type);
            Birthmark b2 = h2.getBirthmark(type);

            double similarity = Double.NaN;
            if(b1 != null && b2 != null){
                BirthmarkSpi spi = context.getService(type);
                BirthmarkComparator comparator = spi.getComparator();

                similarity = comparator.compare(b1, b2);
                count++;
            }
            list.add(similarity);
        }

        double similarity = 0d;
        for(Double d: list){
            if(d.doubleValue() != Double.NaN){
                similarity += d.doubleValue();
            }
        }
        return similarity / count;
    }

    private BirthmarkSet extractBirthmark(String[] birthmarks, InputStream in,
            BirthmarkSet holder, BirthmarkContext context) throws IOException{
        byte[] data = inputStreamToByteArray(in);

        return extractBirthmark(birthmarks, data, holder, context);
    }

    private BirthmarkSet extractBirthmark(String[] birthmarks, byte[] bytecode,
            BirthmarkSet holder, BirthmarkContext context) throws IOException{
        for(String birthmark: birthmarks){
            BirthmarkSpi spi = context.getService(birthmark);
            if(spi != null){
                BirthmarkExtractor extractor = spi.getExtractor();
                holder.addBirthmark(
                    extractor.extract(new ByteArrayInputStream(bytecode), context)
                );
            }
        }

        return holder;
    }

    private byte[] inputStreamToByteArray(InputStream in) throws IOException{
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int read;
        byte[] dataBuffer = new byte[512];
        while((read = in.read(dataBuffer, 0, dataBuffer.length)) != -1){
            bout.write(dataBuffer, 0, read);
        }
        byte[] data = bout.toByteArray();

        bout.close();
        return data;
    }
}