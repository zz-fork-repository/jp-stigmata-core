package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.BirthmarkStoreException;
import jp.naist.se.stigmata.BirthmarkStoreTarget;
import jp.naist.se.stigmata.ComparisonMethod;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.ExtractionUnit;
import jp.naist.se.stigmata.Stigmata;
import jp.naist.se.stigmata.printer.xml.ExtractionResultSetXmlPrinter;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.ui.swing.ExtensionFilter;
import jp.naist.se.stigmata.utils.MultipleIterator;

/**
 * This class manages extracted birthmarks as xml file.
 * This instance do not use {@link ExtractionTarget <code>ExtractionTarget</code>}.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class XmlFileExtractionResultSet extends AbstractExtractionResultSet{
    private boolean addmode = true;
    private File baseDirectory;
    private Map<ExtractionTarget, XmlFile> files = new HashMap<ExtractionTarget, XmlFile>();

    public XmlFileExtractionResultSet(BirthmarkContext context){
        super(context);
    }

    public XmlFileExtractionResultSet(BirthmarkContext context, boolean tableType){
        super(context, tableType);
    }

    public XmlFileExtractionResultSet(File directory){
        super(Stigmata.getInstance().createContext());

        addmode = false;
        baseDirectory = directory;

        BirthmarkContext context = getContext();
        context.setStoreTarget(BirthmarkStoreTarget.XMLFILE);
        if(files.containsKey(ExtractionTarget.TARGET_X) 
            && files.containsKey(ExtractionTarget.TARGET_Y)){
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_XY);
        }
        else{
            context.setComparisonMethod(ComparisonMethod.ROUND_ROBIN_SAME_PAIR);
        }

        for(File file: baseDirectory.listFiles(new ExtensionFilter("xml"))){
            String fileName = file.getName();
            String name = fileName.substring(0, fileName.lastIndexOf('.'));
            ExtractionTarget et = ExtractionTarget.valueOf(name);
            if(et != null){
                files.put(et, new XmlFile(file, context, false));
            }
        }
    }

    @Override
    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException{
        if(addmode){
            if(target == ExtractionTarget.TARGET_BOTH){
                throw new IllegalArgumentException("unknown target: " + target);
            }
            XmlFile xml = files.get(target);
            if(xml == null){
                xml = new XmlFile(new File(getBaseDirectory(), target.name() + ".xml"), getContext());
                files.put(target, xml);
            }
            xml.addBirthmarkSet(set);
        }
        else{
            throw new BirthmarkStoreException("destination is already closed.");
        }
    }

    @Override
    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        closeAllStream();

        XmlFile xml = files.get(target);
        Iterator<BirthmarkSet> iterator;
        if(xml != null){
            iterator = xml.birthmarkSets();
        }
        else if(target == ExtractionTarget.TARGET_BOTH){
            MultipleIterator<BirthmarkSet> mi = new MultipleIterator<BirthmarkSet>();
            boolean addflag = false;
            if(files.containsKey(ExtractionTarget.TARGET_X)){
                addflag = true;
                mi.add(files.get(ExtractionTarget.TARGET_X).birthmarkSets());
            }
            if(files.containsKey(ExtractionTarget.TARGET_Y)){
                addflag = true;
                mi.add(files.get(ExtractionTarget.TARGET_Y).birthmarkSets());
            }
            if(!addflag && files.containsKey(ExtractionTarget.TARGET_XY)){
                mi.add(files.get(ExtractionTarget.TARGET_XY).birthmarkSets());
            }
            iterator = mi;
        }
        else{
            iterator = null;
        }

        return iterator;
    }

    @Override
    public int getBirthmarkSetSize(ExtractionTarget target){
        int size = 0;
        XmlFile xml = files.get(target);
        if(xml != null){
            size = xml.getBirthmarkSetSize();
        }
        else if(target == ExtractionTarget.TARGET_BOTH){
            if(files.containsKey(ExtractionTarget.TARGET_X)){
                size += files.get(ExtractionTarget.TARGET_X).getBirthmarkSetSize();
            }
            if(files.containsKey(ExtractionTarget.TARGET_Y)){
                size += files.get(ExtractionTarget.TARGET_Y).getBirthmarkSetSize();
            }
            if(size == 0 && files.containsKey(ExtractionTarget.TARGET_XY)){
                size += files.get(ExtractionTarget.TARGET_XY).getBirthmarkSetSize();
            }
        }
        return size;
    }

    public BirthmarkStoreTarget getStoreTarget(){
        return BirthmarkStoreTarget.XMLFILE;
    }

    @Override
    public void removeAllBirthmarkSets(ExtractionTarget target){
        XmlFile xml = files.get(target);
        if(xml != null){
            xml.removeAllBirthmarkSets();
        }
        else if(target == ExtractionTarget.TARGET_BOTH){
            for(XmlFile file: files.values()){
                file.removeAllBirthmarkSets();
            }
        }
    }

    @Override
    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        XmlFile xml = files.get(target);
        if(xml != null){
            xml.removeBirthmarkSet(set);
        }
        else if(target == ExtractionTarget.TARGET_BOTH){
            throw new IllegalArgumentException("unknown target: " + target);
        }
    }

    private File getBaseDirectory(){
        if(baseDirectory == null){
            baseDirectory = new File(BirthmarkEnvironment.getStigmataHome(), "extracted_birthmarks/" + generateId());
            if(!baseDirectory.exists()){
                baseDirectory.mkdirs();
            }
        }
        return baseDirectory;
    }

    private void closeAllStream(){
        if(addmode){
            addmode = false;
            for(XmlFile file: files.values()){
                file.closeStream();
            }
        }
    }

    /**
     * Iterator class for reading birthmark xml file by StAX.
     * 
     * @author Haruaki Tamada
     * @version $Revision$ $Date$
     */
    private static class BirthmarkSetStAXIterator implements Iterator<BirthmarkSet>{
        private XMLEventReader reader = null;
        private BirthmarkSet nextObject;
        private List<URL> validItems;
        private BirthmarkEnvironment env;
        private BirthmarkContext context;

        public BirthmarkSetStAXIterator(File file, List<URL> validItems, BirthmarkContext context){
            try{
                XMLInputFactory factory = XMLInputFactory.newInstance();
                BufferedReader in = new BufferedReader(new FileReader(file));
                reader = factory.createXMLEventReader(in);
            } catch(FileNotFoundException e){
            } catch(XMLStreamException e){
            }
            this.validItems = validItems;
            this.context = context;
            this.env = context.getEnvironment();
            try{
                nextObject = findNext();
            } catch(XMLStreamException e){
                e.printStackTrace();
            }
        }

        public boolean hasNext(){
            boolean flag = nextObject != null;
            if(!flag){
                try{
                    reader.close();
                } catch(XMLStreamException e){
                    e.printStackTrace();
                }
            }
            return flag;
        }

        public BirthmarkSet next(){
            BirthmarkSet next = nextObject;
            try{
                nextObject = findNext();
            } catch(XMLStreamException e){
                e.printStackTrace();
            }
            return next;
        }

        public void remove(){
        }

        private BirthmarkSet findNext() throws XMLStreamException{
            BirthmarkSet nextObject = null;
            do{
                nextObject = findNextImpl();
            } while(nextObject != null && validItems != null && !validItems.contains(nextObject.getLocation()));
            return nextObject;
        }

        private BirthmarkSet findNextImpl() throws XMLStreamException{
            String className = null;
            BirthmarkSet bs = null;
            Birthmark birthmark = null;
            BirthmarkSpi service = null;
                
            while(reader.hasNext()){
                XMLEvent event = reader.peek();
                if(event.isStartElement()){
                    StartElement se = event.asStartElement();
                    if(se.getName().getLocalPart().equals("unit")){
                        ExtractionUnit unit = ExtractionUnit.valueOf(reader.getElementText());
                        if(unit != null){
                            context.setExtractionUnit(unit);
                        }                        
                    }
                    if(se.getName().getLocalPart().equals("birthmark-type")){
                        String type = reader.getElementText();
                        if(env.getService(type) != null){
                            context.addBirthmarkType(type);
                        }                        
                    }
                    else if(se.getName().getLocalPart().equals("name")){
                        className = reader.getElementText();
                    }
                    else if(se.getName().getLocalPart().equals("location")){
                        String location = reader.getElementText();
                        if(className == null || location == null){
                            throw new XMLStreamException("incompatible with definition");
                        }
                        try{
                            URL url = new URL(location);
                            bs = new BirthmarkSet(className, url);
                        } catch(MalformedURLException e){
                            e.printStackTrace();
                        }
                    }
                    else if(se.getName().getLocalPart().equals("element")){
                        if(service != null){
                            BirthmarkElement be = service.buildBirthmarkElement(reader.getElementText());
                            birthmark.addElement(be);
                        }
                    }
                    else if(se.getName().getLocalPart().equals("birthmark")){
                        String type = se.getAttributeByName(new QName("type")).getValue();
                        service = env.getService(type);
                        if(service != null){
                            birthmark = service.buildBirthmark();
                            bs.addBirthmark(birthmark);
                        }
                        else{
                            birthmark = null;
                        }
                    }
                }
                else if(event.isEndElement()){
                    EndElement ee = event.asEndElement();
                    if(ee.getName().getLocalPart().equals("extracted-birthmark")){
                        reader.nextEvent();
                        break;
                    }
                }
                reader.nextEvent();
            }
            return bs;
        }
    }

    /**
     * This class represents a xml file about XmlFileExtractionResultSet.
     * 
     * @author Haruaki Tamada
     * @version $Revision$ $Date$
     */
    private static class XmlFile{
        private ExtractionResultSetXmlPrinter formatter;
        private BirthmarkContext context;
        private List<URL> addList = new ArrayList<URL>();
        private int size;
        private File file;
        private PrintWriter out;

        public XmlFile(File file, BirthmarkContext context){
            this.file = file;
            this.context = context;
        }

        public XmlFile(File file, BirthmarkContext context, boolean addflag){
            this.file = file;
            this.context = context;
            if(!addflag){
                addList = null;
            }
        }

        public void addBirthmarkSet(BirthmarkSet bs) throws BirthmarkStoreException{
            if(formatter == null){
                try{
                    out = new PrintWriter(new FileWriter(file));
                    formatter = new ExtractionResultSetXmlPrinter();
                    formatter.printHeader(out);
                    out.printf("    <unit>%s</unit>%n", context.getExtractionUnit());
                    out.printf("    <birthmark-types>%n");
                    for(String type: context.getBirthmarkTypes()){
                        out.printf("      <birthmark-type>%s</birthmark-type>%n", type);
                    }
                    out.printf("    </birthmark-types>%n");
                    
                }catch(IOException e){
                }
            }
            if(out == null || formatter == null){
                throw new BirthmarkStoreException("destination is closed on some reason");
            }
            size++;
            addList.add(bs.getLocation());
            formatter.printBirthmarkSet(out, bs);
        }

        public Iterator<BirthmarkSet> birthmarkSets(){
            return new BirthmarkSetStAXIterator(file, addList, context);
        }

        public void closeStream(){
            if(formatter != null){
                formatter.printFooter(out);
                out.close();
                out = null;
                formatter = null;
            }
        }

        public int getBirthmarkSetSize(){
            if(size == 0){
                int s = 0;
                for(Iterator<BirthmarkSet> i = birthmarkSets(); i.hasNext(); ){
                    i.next();
                    s++;
                }
                size = s;
            }
            return size;
        }

        public void removeAllBirthmarkSets(){
            file.delete();
            size = 0;
            addList.clear();            
        }

        public void removeBirthmarkSet(BirthmarkSet set){
            boolean removeFlag = addList.remove(set.getLocation());
            if(removeFlag){
                size--;
            }
        }
    }
}
