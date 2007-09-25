package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import jp.naist.se.stigmata.birthmarks.extractors.BirthmarkExtractorFactory;
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
import jp.naist.se.stigmata.result.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.result.ExtractionResultSetFactory;
import jp.naist.se.stigmata.result.RoundRobinComparisonResultSet;

/**
 * core engine for extracting/comparing/filtering birthmarks.
 * 
 * This class is not thread safe.
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
    private BirthmarkExtractorFactory factory;

    /**
     * constructor.
     */
    public BirthmarkEngine(BirthmarkEnvironment env){
        this.environment = env;
        factory = new BirthmarkExtractorFactory(env);
    }

    /**
     * returns an environment of this object.
     */
    public BirthmarkEnvironment getEnvironment(){
        return environment;
    }

    public void addBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.add(listener);
    }

    public void removeBirthmarkEngineListener(BirthmarkEngineListener listener){
        listeners.remove(listener);
    }

    /**
     * filters comparison of birthmarks from target files.
     * @see #extract(String[], BirthmarkContext)
     * @see #compare(String[], BirthmarkContext)
     * @see #filter(ComparisonResultSet)
     * @see BirthmarkContext#getFilterTypes()
     */
    public synchronized ComparisonResultSet filter(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(target, context);
        crs = filter(crs);

        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    /**
     * filters comparison of birthmarks from given two targets targetx, and targetY
     * @see #extract(String[], String[], BirthmarkContext)
     * @see #compare(String[], String[], BirthmarkContext)
     * @see #filter(ComparisonResultSet)
     * @see BirthmarkContext#getFilterTypes()
     */
    public synchronized ComparisonResultSet filter(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(targetX, targetY, context);
        crs = filter(crs);
        
        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    /**
     * filters comparison of birthmarks from given extraction result set.
     * @see #compare(ExtractionResultSet)
     * @see #filter(ComparisonResultSet)
     * @see ExtractionResultSet#getContext()
     * @see BirthmarkContext#getFilterTypes()
     */
    public synchronized ComparisonResultSet filter(ExtractionResultSet er) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        ComparisonResultSet crs = compare(er);
        crs = filter(crs);
        
        operationDone(OperationType.FILTER_BIRTHMARKS);

        return crs;
    }

    /**
     * filters comparison of birthmarks.
     * @see ExtractionResultSet#getContext()
     * @see BirthmarkContext#getFilterTypes()
     */
    public synchronized ComparisonResultSet filter(ComparisonResultSet crs) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.FILTER_BIRTHMARKS);

        String[] filterTypes = crs.getContext().getFilterTypes();

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

    /**
     * compares two given birthmarks and returns comparison pair.
     */
    public synchronized ComparisonPair compareDetails(BirthmarkSet bs1, BirthmarkSet bs2, BirthmarkContext context) throws BirthmarkComparisonFailedException{
        return new ComparisonPair(bs1, bs2, context);
    }

    public synchronized ComparisonResultSet compare(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);

        ExtractionResultSet er = extract(target, context);
        ComparisonResultSet crs = compare(er);

        operationDone(OperationType.COMPARE_BIRTHMARKS);

        return crs;
    }

    public synchronized ComparisonResultSet compare(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);

        ExtractionResultSet er = extract(targetX, targetY, context);
        ComparisonResultSet crs = compare(er);

        operationDone(OperationType.COMPARE_BIRTHMARKS);

        return crs;
    }

    public synchronized ComparisonResultSet compare(ExtractionResultSet er) throws BirthmarkExtractionFailedException, BirthmarkComparisonFailedException{
        operationStart(OperationType.COMPARE_BIRTHMARKS);
        BirthmarkContext context = er.getContext();

        ComparisonResultSet crs = null;
        switch(context.getComparisonMethod()){
        case ROUND_ROBIN_SAME_PAIR:
            crs = new RoundRobinComparisonResultSet(er, true);
            break;
        case ROUND_ROBIN_WITHOUT_SAME_PAIR:
            crs = new RoundRobinComparisonResultSet(er, false);
            break;
        case ROUND_ROBIN_XY:
            crs = new RoundRobinComparisonResultSet(er, true);
        case GUESSED_PAIR:
            crs = new CertainPairComparisonResultSet(er);
            break;
        case SPECIFIED_PAIR:
            crs = new CertainPairComparisonResultSet(er, context.getNameMappings());
            break;
        }

        operationDone(OperationType.COMPARE_BIRTHMARKS);
        return crs;
    }

    public synchronized ExtractionResultSet extract(String[] target, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        operationStart(OperationType.EXTRACT_BIRTHMARKS);
        ExtractionResultSet er = extract(target, null, context);
        operationDone(OperationType.EXTRACT_BIRTHMARKS);
        return er;
    }

    public synchronized ExtractionResultSet extract(String[] targetX, String[] targetY, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        operationStart(OperationType.EXTRACT_BIRTHMARKS);
        ExtractionResultSet er = ExtractionResultSetFactory.getInstance().createResultSet(context);

        try{
            switch(context.getComparisonMethod()){
            case ROUND_ROBIN_SAME_PAIR:
            case ROUND_ROBIN_WITHOUT_SAME_PAIR:
                er.setTableType(false);
                String[] targetXY = mergeTarget(targetX, targetY);
                extractImpl(targetXY, er, ExtractionTarget.TARGET_XY);
                break;
            case GUESSED_PAIR:
            case SPECIFIED_PAIR:
            case ROUND_ROBIN_XY:
            default:
                if(targetX == null || targetY == null){
                    throw new BirthmarkExtractionFailedException("targetX or targetY is null");
                }
                er.setTableType(true);
                extractImpl(targetX, er, ExtractionTarget.TARGET_X);
                extractImpl(targetY, er, ExtractionTarget.TARGET_Y);
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

    private BirthmarkSet[] extractImpl(String[] target, ExtractionResultSet er, ExtractionTarget et) throws BirthmarkExtractionFailedException, IOException{
        ClassFileArchive[] archives = createArchives(target, environment);
        BirthmarkContext context = er.getContext();
        ExtractionUnit unit = context.getExtractionUnit();

        BirthmarkSet[] extractResult = null;
        if(unit == ExtractionUnit.CLASS){
            extractFromClass(archives, er, et);
        }
        else if(unit == ExtractionUnit.PACKAGE){
            extractFromPackage(archives, er, et);
        }
        else if(unit == ExtractionUnit.ARCHIVE){
            extractFromProduct(archives, er, et);
        }

        return extractResult;
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

    private void extractFromPackage(ClassFileArchive[] archives, ExtractionResultSet er, ExtractionTarget et) throws IOException, BirthmarkExtractionFailedException{
        Map<String, BirthmarkSet> map = new HashMap<String, BirthmarkSet>();
        BirthmarkContext context = er.getContext();

        for(ClassFileArchive archive: archives){
            for(ClassFileEntry entry: archive){
                try{
                    String name = entry.getClassName();
                    String packageName = parsePackageName(name);
                    BirthmarkSet bs = map.get(packageName);
                    if(bs == null){
                        bs = new BirthmarkSet(packageName, archive.getLocation());
                        map.put(packageName, bs);
                    }

                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    for(String birthmarkType: context.getBirthmarkTypes()){
                        try{
                            BirthmarkExtractor extractor = factory.getExtractor(birthmarkType);
                            if(extractor.isAcceptable(ExtractionUnit.PACKAGE)){
                                Birthmark b = bs.getBirthmark(extractor.getProvider().getType());
                                if(b == null){
                                    b = extractor.createBirthmark();
                                    bs.addBirthmark(b);
                                }
                                extractor.extract(b, new ByteArrayInputStream(data), er.getEnvironment());
                            }
                        } catch(ExtractorNotFoundException e){
                            warnings.addMessage(e, birthmarkType);
                        }
                    }
                } catch(IOException e){
                    warnings.addMessage(e, archive.getName());
                }
            }
        }
        for(BirthmarkSet bs: map.values()){
            er.addBirthmarkSet(et, bs);
        }
    }

    private String parsePackageName(String name){
        String n = name.replace('/', '.');
        int index = n.lastIndexOf('.');
        if(index > 0){
            n = n.substring(0, index - 1);
        }

        return n;
    }

    private void extractFromClass(ClassFileArchive[] archives, ExtractionResultSet er,
            ExtractionTarget et) throws IOException, BirthmarkExtractionFailedException{
        BirthmarkContext context = er.getContext();

        for(ClassFileArchive archive: archives){
            for(ClassFileEntry entry: archive){
                try{
                    BirthmarkSet birthmarkset = new BirthmarkSet(entry.getClassName(), entry.getLocation());
                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    
                    for(String birthmarkType: context.getBirthmarkTypes()){
                        try{
                            BirthmarkExtractor extractor = factory.getExtractor(birthmarkType);
                            if(extractor.isAcceptable(ExtractionUnit.CLASS)){
                                Birthmark b = extractor.extract(new ByteArrayInputStream(data), er.getEnvironment());
                                birthmarkset.addBirthmark(b);
                            }
                        } catch(ExtractorNotFoundException e){
                            warnings.addMessage(e, birthmarkType);
                        }
                    }
                    er.addBirthmarkSet(et, birthmarkset);
                } catch(IOException e){
                    warnings.addMessage(e, entry.getClassName());
                }
            }
        }
    }

    private void extractFromProduct(ClassFileArchive[] archives, ExtractionResultSet er, ExtractionTarget et) throws IOException, BirthmarkExtractionFailedException{
        BirthmarkContext context = er.getContext();

        for(ClassFileArchive archive: archives){
            BirthmarkSet birthmarkset = new BirthmarkSet(archive.getName(), archive.getLocation());

            for(ClassFileEntry entry: archive){
                try{
                    byte[] data = inputStreamToByteArray(entry.getLocation().openStream());
                    for(String birthmarkType: context.getBirthmarkTypes()){
                        try{
                            BirthmarkExtractor extractor = factory.getExtractor(birthmarkType);
                            if(extractor.isAcceptable(ExtractionUnit.ARCHIVE)){
                                Birthmark b = birthmarkset.getBirthmark(birthmarkType);
                                if(b == null){
                                    b = extractor.createBirthmark();
                                    birthmarkset.addBirthmark(b);
                                }
                                extractor.extract(b, new ByteArrayInputStream(data), er.getEnvironment());
                            }
                        } catch(ExtractorNotFoundException e){
                            warnings.addMessage(e, birthmarkType);
                        } 
                    }
                } catch(IOException e){
                    warnings.addMessage(e, entry.getClassName());
                }
            }
            if(birthmarkset.getBirthmarksCount() != 0){
                er.addBirthmarkSet(et, birthmarkset);
            }
        }
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
