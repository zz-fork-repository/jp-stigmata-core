package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.naist.se.stigmata.birthmarks.BirthmarkService;
import jp.naist.se.stigmata.utils.WellknownClassManager;
import jp.naist.se.stigmata.utils.WellknownClassJudgeRule;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * configuration file parser.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class ConfigFileParser extends DefaultHandler{
    private enum Part{ WELLKNOWN_CLASSES, PROPERTIES, CLASSPATH, SERVICES, }

    private BirthmarkContext context;
    private WellknownClassManager manager;
    private BirthmarkService service;
    private Part part;
    private int wellknownType = 0;
    private int patternType = 0;
    private String qname;
    private String key;

    public ConfigFileParser(BirthmarkContext context){
        this.context = context;
        manager = context.getWellknownClassManager();
    }

    public void parse(InputStream in) throws IOException{
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(in, this);
        }
        catch(ParserConfigurationException e){
            throw new IOException(e.getMessage());
        }
        catch(SAXException e){
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException{
        qname = qName;

        if(qName.equals("wellknown-classes")){
            part = Part.WELLKNOWN_CLASSES;
        }
        else if(qName.equals("property")){
            part = Part.PROPERTIES;
        }
        else if(qName.equals("classpath-list")){
            part = Part.CLASSPATH;
        }
        else if(qName.equals("service")){
            part = Part.SERVICES;
            service = new BirthmarkService();
            service.setUserDefined(false);
        }
        else if(qName.equals("exclude")){
            wellknownType = WellknownClassJudgeRule.EXCLUDE_TYPE;
        }
        else if(qName.equals("package")){
            wellknownType = WellknownClassJudgeRule.PACKAGE_TYPE;
        }
        else if(qName.equals("class-name")){
            wellknownType = WellknownClassJudgeRule.CLASS_NAME_TYPE;
        }
        else if(qName.equals("fully-name")){
            wellknownType = WellknownClassJudgeRule.FULLY_TYPE;
        }
        else if(qName.equals("suffix")){
            patternType = WellknownClassJudgeRule.SUFFIX_TYPE;
        }
        else if(qName.equals("prefix")){
            patternType = WellknownClassJudgeRule.PREFIX_TYPE;
        }
        else if(qName.equals("match")){
            patternType = WellknownClassJudgeRule.MATCH_TYPE;
        }
    }

    @Override
    public void characters(char[] data, int offset, int length) throws SAXException{
        String value = new String(data, offset, length).trim();
        if(value.length() > 0){
            if(qname.equals("name") && part == Part.PROPERTIES){
                key = value;
            }
            else if(qname.equals("value") && part == Part.PROPERTIES){
                context.addProperty(key, value);
            }
            else if(part == Part.WELLKNOWN_CLASSES &&
                    (qname.equals("suffix") || qname.equals("prefix") || qname.equals("match"))){
                manager.add(new WellknownClassJudgeRule(value, wellknownType | patternType));
            }
            else if(part == Part.CLASSPATH && qname.equals("classpath")){
                try{
                    context.getBytecodeContext().addClasspath(new URL(value));
                } catch(MalformedURLException e){
                    throw new SAXException(e);
                }
            }
            else if(part == Part.SERVICES){
                if(qname.equals("type"))              service.setType(value);
                else if(qname.equals("display-name")) service.setDisplayType(value);
                else if(qname.equals("description"))  service.setDescription(value);
                else if(qname.equals("extractor"))    service.setExtractorClassName(value);
                else if(qname.equals("comparator"))   service.setComparatorClassName(value);
            }
        }
    }

    @Override
    public void endElement(String uri, String localname, String qname){
        if(part == Part.SERVICES && qname.equals("service")){
            context.addService(service);
            service = null;
        }
    }
}
