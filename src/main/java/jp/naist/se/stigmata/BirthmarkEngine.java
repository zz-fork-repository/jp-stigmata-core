package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import jp.naist.se.stigmata.event.BirthmarkEngineEvent;
import jp.naist.se.stigmata.event.BirthmarkEngineListener;
import jp.naist.se.stigmata.event.OperationStage;
import jp.naist.se.stigmata.event.OperationType;
import jp.naist.se.stigmata.event.WarningMessages;
import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.filter.FilteredComparisonResultSet;
import jp.naist.se.stigmata.reader.ClassFileArchive;
import jp.naist.se.stigmata.reader.ClassFileEntry;
import jp.naist.se.stigmata.reader.ClasspathContext;
import jp.naist.se.stigmata.reader.DefaultClassFileArchive;
import jp.naist.se.stigmata.reader.JarClassFileArchive;
import jp.naist.se.stigmata.reader.WarClassFileArchive;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class BirthmarkEngine{
    private BirthmarkEnvironment environment;
    private List<BirthmarkEngineListener> listeners = new ArrayList<BirthmarkEngineListener>();
    private Stack<WarningMessages> stack = new Stack<WarningMessages>();
    private WarningMessages warnings;
    private OperationType latestOperationType;
    private OperationType targetType;

    public BirthmarkEngine(){
        this(BirthmarkEnvironment.getDefaultEnvironment());
    }

    public BirthmarkEngine(BirthmarkEnvironment env){
        this.environment = env;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.remove(listener);
    }

    public ComparisonResultSet filter(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(target, context);
        crs = filter(crs, context);
        
        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    public ComparisonResultSet filter(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(targetX, targetY, context);
        crs = filter(crs, context);
        
        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    public ComparisonResultSet filter(ExtractionResult er, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(er, context);
        crs = filter(crs, context);
        
        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    public ComparisonResultSet filter(ComparisonResultSet crs, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        String[] filterTypes = context.getFilterTypes();

        if(filterTypes != null){
            List<ComparisonPairFilterSet> filterList = new ArrayList<ComparisonPairFilterSet>();
            ComparisonPairFilterManager manager = environment.getFilterManager();
            for(int i = 0; i < filterTypes.length; i++){
                ComparisonPairFilterSet fset = manager.getFilterSet(filterTypes[i]);
                if(fset != null){
                    filterList.add(fset);
                }
                else{
                    warnings.addMessage(new FilterNotFoundException("filter not found"), filterTypes[i]);
                }
            }
            ComparisonPairFilterSet[] cpfs = filterList.toArray(new ComparisonPairFilterSet[filterList.size()]);

            crs = new FilteredComparisonResultSet(crs, cpfs);
        }
        
        operationDone(OperationType.FILTER_BIRTHMARKS);
        return crs;
    }

    public ComparisonResultSet compare(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);

        ExtractionResult er = extract(target, context);
        ComparisonResultSet crs = compare(er, context);

        operationDone(OperationType.COMPARE_BIRTHMARKS);

        return crs;
    }

    public ComparisonResultSet compare(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);

        ExtractionResult er = extract(targetX, targetY, context);
        ComparisonResultSet crs = compare(er, context);

        operationDone(OperationType.COMPARE_BIRTHMARKS);

        return crs;
    }

    public ComparisonResultSet compare(ExtractionResult er, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);

        ComparisonResultSet crs = null;
        switch(context.getComparisonMethod()){
        case ROUND_ROBIN:
            crs = new RoundRobinComparisonResultSet(
                er.getBirthmarkSetXY(), environment, true
            );
            break;
        case ROUND_ROBIN_XY:
            crs = new RoundRobinComparisonResultSet(
                er.getBirthmarkSetX(), er.getBirthmarkSetY(), environment
            );
        case GUESSED_PAIR:
            crs = new CertainPairComparisonResultSet(
                er.getBirthmarkSetX(), er.getBirthmarkSetY(), environment
            );
            break;
        case SPECIFIED_PAIR:
            crs = new CertainPairComparisonResultSet(
                er.getBirthmarkSetX(), er.getBirthmarkSetY(),
                context.getNameMappings(), environment
            );
            break;
        }

        operationDone(OperationType.COMPARE_BIRTHMARKS);
        return crs;
    }

    public ExtractionResult extract(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        operationStart(OperationType.EXTRACT_BIRTHMARKS);
        ExtractionResult er = extract(target, null, context);
        operationDone(OperationType.EXTRACT_BIRTHMARKS);
        return er;
    }

    public ExtractionResult extract(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        operationStart(OperationType.EXTRACT_BIRTHMARKS);
        ExtractionResult er = new ExtractionResult();

        try{
            switch(context.getComparisonMethod()){
            case ROUND_ROBIN:
                String[] targetXY = mergeTarget(targetX, targetY);
                BirthmarkSet[] s = extractImpl(targetXY, context);
                er.setBirthmarkSetXY(s);
                break;
            case GUESSED_PAIR:
            case SPECIFIED_PAIR:
            case ROUND_ROBIN_XY:
            default:
                if(targetX == null || targetY == null){
                    throw new BirthmarkExtractionFailedException("targetX or targetY is null");
                }
                BirthmarkSet[] extractResultX = extractImpl(targetX, context);
                BirthmarkSet[] extractResultY = extractImpl(targetY, context);

                er.setBirthmarkSetX(extractResultX);
                er.setBirthmarkSetY(extractResultY);
                break;
            }
            return er;
        } catch(IOException e){
            throw new BirthmarkExtractionFailedException(e);
        } finally{
            operationDone(OperationType.EXTRACT_BIRTHMARKS);
        }
    }

    private String[] mergeTarget(String[] t1, String[] t2){
        List<String> list = new ArrayList<String>();
        addToList(list, t1);
        addToList(list, t2);

        return list.toArray(new String[list.size()]);
    }

    private void addToList(List<String> list, String[] target){
        if(target != null){
            for(String s: target){
                list.add(s);
            }
        }
    }

    private BirthmarkSet[] extractImpl(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException, IOException{
        ClassFileArchive[] archives = createArchives(target, environment);
        BirthmarkExtractor[] extractors = createExtractors(context.getExtractionTypes(), environment);
        ExtractionUnit unit = context.getExtractionUnit();

        BirthmarkSet[] extractResult = null;
        if(unit == ExtractionUnit.CLASS){
            extractResult = extractFromClass(archives, extractors, environment);
        }
        else if(unit == ExtractionUnit.PACKAGE){
            extractResult = extractFromPackage(archives, extractors, environment);
        }
        else if(unit == ExtractionUnit.ARCHIVE){
            extractResult = extractFromProduct(archives, extractors, environment);
        }

        return extractResult;
    }

    private BirthmarkExtractor[] createExtractors(String[] birthmarkTypes, BirthmarkEnvironment environment){
        List<BirthmarkExtractor> list = new ArrayList<BirthmarkExtractor>();
        for(String type: birthmarkTypes){
            BirthmarkExtractor extractor = createExtractor(type, environment);
            list.add(extractor);
        }
        return list.toArray(new BirthmarkExtractor[list.size()]);
    }

    private BirthmarkExtractor createExtractor(String birthmarkType, BirthmarkEnvironment environment){
        BirthmarkSpi spi = environment.getService(birthmarkType);
        BirthmarkExtractor extractor = null;
        if(spi != null){
            extractor = spi.getExtractor();
            try{
                if(extractor != null){
                    Map props = BeanUtils.describe(extractor);
                    props.remove("class");
                    props.remove("provider");
                    for(Object keyObject: props.keySet()){
                        String key = "extractor." + spi.getType() + "." + String.valueOf(keyObject);
                        if(environment.getProperty(key) != null){
                            BeanUtils.setProperty(
                                extractor, (String)keyObject, environment.getProperty(key)
                            );
                        }
                    }
                }
            } catch(InvocationTargetException e){
                throw new InternalError(e.getMessage());
            } catch(NoSuchMethodException e){
                throw new InternalError(e.getMessage());
            } catch(IllegalAccessException e){
                throw new InternalError(e.getMessage());
            }
        }
        if(extractor == null){
            warnings.addMessage(new ExtractorNotFoundException("extractor not found"), birthmarkType);
        }

        return null;
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

    private BirthmarkSet[] extractFromPackage(ClassFileArchive[] archives, BirthmarkExtractor[] extractors, BirthmarkEnvironment context) throws IOException, BirthmarkExtractionFailedException{
        Map<String, BirthmarkSet> list = new HashMap<String, BirthmarkSet>();

        for(ClassFileArchive archive: archives){
            for(ClassFileEntry entry: archive){
                try{
                    String name = entry.getClassName();
                    String packageName = parsePackageName(name);
                    BirthmarkSet bs = list.get(packageName);
                    if(bs == null){
                        bs = new BirthmarkSet(packageName, archive.getLocation());
                        list.put(packageName, bs);
                    }

                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    for(BirthmarkExtractor extractor: extractors){
                        if(extractor.isAcceptable(ExtractionUnit.PACKAGE)){
                            Birthmark b = bs.getBirthmark(extractor.getProvider().getType());
                            if(b == null){
                                b = extractor.createBirthmark();
                                bs.addBirthmark(b);
                            }
                            extractor.extract(b, new ByteArrayInputStream(data), context);
                        }
                    }
                } catch(IOException e){
                    warnings.addMessage(e, archive.getName());
                }
            }
        }

        return list.values().toArray(new BirthmarkSet[list.size()]);
    }

    private String parsePackageName(String name){
        String n = name.replace('/', '.');
        int index = n.lastIndexOf('.');
        if(index > 0){
            n = n.substring(0, index - 1);
        }

        return n;
    }

    private BirthmarkSet[] extractFromClass(ClassFileArchive[] archives, BirthmarkExtractor[] extractors, BirthmarkEnvironment context) throws IOException, BirthmarkExtractionFailedException{
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();

        for(ClassFileArchive archive: archives){
            for(ClassFileEntry entry: archive){
                try{
                    BirthmarkSet birthmarkset = new BirthmarkSet(entry.getClassName(), entry.getLocation());
                    list.add(birthmarkset);
                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    for(BirthmarkExtractor extractor: extractors){
                        if(extractor.isAcceptable(ExtractionUnit.CLASS)){
                            Birthmark b = extractor.extract(new ByteArrayInputStream(data), context);
                            birthmarkset.addBirthmark(b);
                        }
                    }
                } catch(IOException e){
                    warnings.addMessage(e, entry.getClassName());
                }
            }
        }
        return list.toArray(new BirthmarkSet[list.size()]);
    }

    private BirthmarkSet[] extractFromProduct(ClassFileArchive[] archives, BirthmarkExtractor[] extractors, BirthmarkEnvironment context) throws IOException, BirthmarkExtractionFailedException{
        List<BirthmarkSet> list = new ArrayList<BirthmarkSet>();

        for(ClassFileArchive archive: archives){
            BirthmarkSet birthmarkset = new BirthmarkSet(archive.getName(), archive.getLocation());
            list.add(birthmarkset);

            for(ClassFileEntry entry: archive){
                try{
                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    for(BirthmarkExtractor extractor: extractors){
                        if(extractor.isAcceptable(ExtractionUnit.ARCHIVE)){
                            Birthmark b = birthmarkset.getBirthmark(extractor.getProvider().getType());
                            if(b == null){
                                b = extractor.createBirthmark();
                                birthmarkset.addBirthmark(b);
                            }
                            extractor.extract(b, new ByteArrayInputStream(data), context);
                        }
                    }
                } catch(IOException e){
                    warnings.addMessage(e, entry.getClassName());
                }
            }
        }
        for(Iterator<BirthmarkSet> i = list.iterator(); i.hasNext(); ){
            BirthmarkSet set = i.next();
            if(set.getBirthmarksCount() == 0){
                i.remove();
            }
        }

        return list.toArray(new BirthmarkSet[list.size()]);
    }

    private ClassFileArchive[] createArchives(String[] files, BirthmarkEnvironment environment) throws IOException, MalformedURLException{
        ClasspathContext bytecode = environment.getClasspathContext();
        List<ClassFileArchive> archives = new ArrayList<ClassFileArchive>();
        for(int i = 0; i < files.length; i++){
            try{
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
            } catch(IOException e){
                warnings.addMessage(e, files[i]);
            }
        }
        return archives.toArray(new ClassFileArchive[archives.size()]);
    }

    private void operationStart(OperationType type){
        if(warnings == null){
            warnings = new WarningMessages(type);
            fireEvent(new BirthmarkEngineEvent(OperationStage.OPERATION_START, type, warnings));
            latestOperationType = type;
            targetType = type;
        }
        stack.push(warnings);
        /*
         * call subOperationStart method only once when some operation is occured.
         * Ex. extraction, comparison, filtering
         */
        if(latestOperationType != type){
            fireEvent(new BirthmarkEngineEvent(OperationStage.SUB_OPERATION_START, type, warnings));
            latestOperationType = type;
        }
    }

    private void operationDone(OperationType type){
        if(latestOperationType != type && targetType != type){
            fireEvent(new BirthmarkEngineEvent(OperationStage.SUB_OPERATION_DONE, type, warnings));
            latestOperationType = type;
        }
        stack.pop();
        if(stack.size() == 0){
            fireEvent(new BirthmarkEngineEvent(OperationStage.OPERATION_DONE, type, warnings));
            warnings = null;
            latestOperationType = null;
        }
    }

    private void fireEvent(BirthmarkEngineEvent e){
        for(BirthmarkEngineListener listener: listeners){
            switch(e.getStage()){
            case OPERATION_START:
                listener.operationStart(e);
                break;
            case SUB_OPERATION_START:
                listener.subOperationStart(e);
                break;
            case SUB_OPERATION_DONE:
                listener.subOperationDone(e);
                break;
            case OPERATION_DONE:
                listener.operationDone(e);
                break;
            default:
                throw new InternalError("unknown stage: " + e.getStage());
            }
        }
    }
}
