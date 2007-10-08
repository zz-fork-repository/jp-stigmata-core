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
import java.util.Iterator;
import java.util.List;

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
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.printer.xml.ExtractionResultSetXmlPrinter;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * This class manages extracted birthmarks as xml file.
 * This instance do not use {@link ExtractionTarget <code>ExtractionTarget</code>}.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class XmlFileExtractionResultSet extends AbstractExtractionResultSet{
    private List<URL> addList = new ArrayList<URL>();
    private boolean addmode = true;
    private int size;
    private ExtractionResultSetXmlPrinter formatter;
    private File targetFile;
    private PrintWriter out;

    public XmlFileExtractionResultSet(BirthmarkContext context, boolean tableType){
        super(context, tableType);

        createTargetFile();
    }

    public XmlFileExtractionResultSet(BirthmarkContext context){
        super(context);

        createTargetFile();
    }

    public BirthmarkStoreTarget getStoreTarget(){
        return BirthmarkStoreTarget.XMLFILE;
    }

    @Override
    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException{
        if(addmode){
            if(formatter == null){
                try{
                    out = new PrintWriter(new FileWriter(targetFile));
                	formatter = new ExtractionResultSetXmlPrinter();
                    formatter.printHeader(out);
                }catch(IOException e){
                }
            }
            if(out == null || formatter == null){
                throw new BirthmarkStoreException("destination is closed on some reason");
            }
            size++;
            addList.add(set.getLocation());
            formatter.printBirthmarkSet(out, set);
        }
        else{
            throw new BirthmarkStoreException("destination is already closed.");
        }
    }

    @Override
    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        checkMode();

        return new BirthmarkSetStAXIterator(targetFile, addList, getEnvironment());
    }

    @Override
    public int getBirthmarkSetSize(ExtractionTarget target){
        return size;
    }

    @Override
    public void removeAllBirthmarkSets(ExtractionTarget target){
        targetFile.delete();
        size = 0;
        addList.clear();
    }

    @Override
    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        boolean removeFlag = addList.remove(set.getLocation());
        if(removeFlag){
            size--;
        }
    }

    private void createTargetFile(){
        targetFile = new File(BirthmarkEnvironment.getStigmataHome(), "extracted_birthmarks/" + generateId() + ".xml");
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
    }

    private synchronized void checkMode(){
        if(addmode){
            addmode = false;
            formatter.printFooter(out);
            out.close();
            out = null;
            formatter = null;
        }
    }

    private static class BirthmarkSetStAXIterator implements Iterator<BirthmarkSet>{
        private XMLEventReader reader = null;
        private BirthmarkSet nextObject;
        private List<URL> validItems;
        private BirthmarkEnvironment env;

        public BirthmarkSetStAXIterator(File file, List<URL> validItems, BirthmarkEnvironment env){
            try{
                XMLInputFactory factory = XMLInputFactory.newInstance();
                BufferedReader in = new BufferedReader(new FileReader(file));
                reader = factory.createXMLEventReader(in);
            } catch(FileNotFoundException e){
            } catch(XMLStreamException e){
            }
            this.validItems = validItems;
            this.env = env;
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
            String className = null;
            BirthmarkSet bs = null;
            Birthmark birthmark = null;
            BirthmarkSpi service = null;
                
            while(reader.hasNext()){
                XMLEvent event = reader.peek();
                if(event.isStartElement()){
                    StartElement se = event.asStartElement();
                    if(se.getName().getLocalPart().equals("name")) className = reader.getElementText();
                    else if(se.getName().getLocalPart().equals("location")){
                        String location = reader.getElementText();
                        if(className == null || location == null){
                            throw new XMLStreamException("incompatible with definition");
                        }
                        try{
                            URL url = new URL(location);
                            if(!validItems.contains(url)){
                                while(reader.hasNext()){
                                    XMLEvent xmlevent = reader.nextTag();
                                    if(xmlevent.isEndElement() &&
                                        xmlevent.asEndElement().getName().getLocalPart().equals("extracted-birthmark")){
                                        break;
                                    }
                                }
                                continue;
                            }
                            bs = new BirthmarkSet(className, url);
                        } catch(MalformedURLException e){
                            e.printStackTrace();
                        }
                    }
                    else if(se.getName().getLocalPart().equals("element")){
                        BirthmarkElement be = service.buildBirthmarkElement(reader.getElementText());
                        birthmark.addElement(be);
                    }
                    else if(se.getName().getLocalPart().equals("birthmark")){
                        String type = se.getAttributeByName(new QName("type")).getValue();
                        service = env.getService(type);
                        birthmark = service.buildBirthmark();
                        bs.addBirthmark(birthmark);
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
}
