package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.filter.FilteredComparisonResultSet;
import jp.naist.se.stigmata.reader.ClassFileArchive;
import jp.naist.se.stigmata.reader.ClassFileEntry;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.reader.DefaultClassFileArchive;
import jp.naist.se.stigmata.reader.JarClassFileArchive;
import jp.naist.se.stigmata.reader.WarClassFileArchive;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.utils.ConfigFileImporter;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Birthmarking engine.
 *
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class Stigmata{
    private static final Stigmata instance = new Stigmata();

    private BirthmarkContext defaultContext = BirthmarkContext.getDefaultContext();
    private boolean configDone = false;

    private Stigmata(){
    }

    public static Stigmata getInstance(){
        return instance;
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

    /**
     * create a new {@link BirthmarkContext <code>BirthmarkContext</code>}.
     */
    public BirthmarkContext createContext(){
        if(!configDone){
            configuration();
        }
        return new BirthmarkContext();
    }

    public BirthmarkSet[] extract(String[] birthmarks, String[] files) throws BirthmarkExtractionException{
        if(!configDone){
            configuration();
        }
        return extract(birthmarks, files, createContext());
    }

    public BirthmarkSet[] extract(String[] birthmarks, String[] files,
                                  BirthmarkContext context) throws BirthmarkExtractionException{
        if(!configDone){
            configuration();
        }
        try{
            return extractImpl(birthmarks, files, context);
        } catch(IOException e){
               throw new BirthmarkExtractionException(e);
        }
    }

    private BirthmarkSet[] extractImpl(String[] birthmarks, String[] files, BirthmarkContext context) throws IOException, BirthmarkExtractionException{
        List<ClassFileArchive> archives = new ArrayList<ClassFileArchive>();
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();
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
        if(!configDone){
            configuration();
        }
        return compare(holders, createContext());
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders, BirthmarkContext context) throws IOException{
        if(!configDone){
            configuration();
        }
        ComparisonResultSet result = new RoundRobinComparisonResultSet(holders, context, true);

        return result;
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders1, BirthmarkSet[] holders2) throws IOException{
        if(!configDone){
            configuration();
        }
        return compare(holders1, holders2, createContext());
    }

    public ComparisonResultSet compare(BirthmarkSet[] holders1, BirthmarkSet[] holders2, BirthmarkContext context) throws IOException{
        if(!configDone){
            configuration();
        }
        ComparisonResultSet result = new RoundRobinComparisonResultSet(holders1, holders2, context);

        return result;
    }

    public ComparisonResultSet filter(ComparisonResultSet resultset, String[] filters){
        return filter(resultset, filters, createContext());
    }

    public ComparisonResultSet filter(ComparisonResultSet resultset, String[] filters, BirthmarkContext context){
        if(filters != null){
            List<ComparisonPairFilterSet> filterList = new ArrayList<ComparisonPairFilterSet>();
            ComparisonPairFilterManager manager = context.getFilterManager();
            for(int i = 0; i < filters.length; i++){
                ComparisonPairFilterSet fset = manager.getFilterSet(filters[i]);
                if(fset != null){
                    filterList.add(fset);
                }
                else{
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.warning(filters[i] + ": filter not found");
                }
            }

            return filter(resultset, filterList.toArray(new ComparisonPairFilterSet[filterList.size()]));
        }
        return resultset;
    }

    public ComparisonResultSet filter(ComparisonResultSet resultset, ComparisonPairFilterSet[] filters){
        return filter(resultset, filters, createContext());
    }

    public ComparisonResultSet filter(ComparisonResultSet resultset, ComparisonPairFilterSet[] filters, BirthmarkContext context){
        FilteredComparisonResultSet filterResultSet = new FilteredComparisonResultSet(resultset);
        
        return filterResultSet;
    }

    public double compare(BirthmarkSet h1, BirthmarkSet h2){
        if(!configDone){
            configuration();
        }
        return compare(h1, h2, createContext());
    }

    public double compare(BirthmarkSet h1, BirthmarkSet h2, BirthmarkContext context){
        if(!configDone){
            configuration();
        }

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
            BirthmarkSet holder, BirthmarkContext context) throws BirthmarkExtractionException, IOException{
        byte[] data = inputStreamToByteArray(in);

        return extractBirthmark(birthmarks, data, holder, context);
    }

    private BirthmarkSet extractBirthmark(String[] birthmarks, byte[] bytecode, BirthmarkSet holder,
                                          BirthmarkContext context) throws BirthmarkExtractionException, IOException{
        for(String birthmark: birthmarks){
            BirthmarkSpi spi = context.getService(birthmark);
            if(spi != null){
                BirthmarkExtractor extractor = spi.getExtractor();
                try{
                    Map props = BeanUtils.describe(extractor);
                    props.remove("class");
                    props.remove("provider");
                    for(Object keyObject: props.keySet()){
                        String key = "extractor." + spi.getType() + "." + String.valueOf(keyObject);
                        if(context.getProperty(key) != null){
                            BeanUtils.setProperty(extractor, (String)keyObject, context.getProperty(key));
                        }
                    }
                } catch(Exception e){
                }
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

    private void initConfiguration(InputStream in){
        try {
            ConfigFileImporter parser = new ConfigFileImporter(defaultContext);
            parser.parse(in);
        } catch(IOException e){
            throw new ApplicationInitializationError(e);
        }
        for(Iterator<BirthmarkSpi> i = ServiceRegistry.lookupProviders(BirthmarkSpi.class); i.hasNext(); ){
            BirthmarkSpi service = i.next();
            defaultContext.addService(service);
        }
        configDone = true;
    }
}
