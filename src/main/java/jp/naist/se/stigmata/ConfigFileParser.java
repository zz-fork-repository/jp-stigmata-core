package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jp.naist.se.stigmata.utils.WellknownClassManager;
import jp.naist.se.stigmata.utils.WellknownClassSection;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * configuration file parser.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class ConfigFileParser extends DefaultHandler{
    private BirthmarkContext context;

    private WellknownClassManager manager;

    private boolean wellknownPart = false;

    private boolean propertyPart = false;

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
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException{
        qname = qName;

        if(qName.equals("wellknown-classes")){
            wellknownPart = true;
        }
        else if(qName.equals("property")){
            wellknownPart = false;
            propertyPart = true;
        }
        else if(qName.equals("exclude")){
            wellknownType = WellknownClassSection.EXCLUDE_TYPE;
        }
        else if(qName.equals("package")){
            wellknownType = WellknownClassSection.PACKAGE_TYPE;
        }
        else if(qName.equals("class-name")){
            wellknownType = WellknownClassSection.CLASS_NAME_TYPE;
        }
        else if(qName.equals("fully-name")){
            wellknownType = WellknownClassSection.FULLY_TYPE;
        }
        else if(qName.equals("suffix")){
            patternType = WellknownClassSection.SUFFIX_TYPE;
        }
        else if(qName.equals("prefix")){
            patternType = WellknownClassSection.PREFIX_TYPE;
        }
        else if(qName.equals("match")){
            patternType = WellknownClassSection.MATCH_TYPE;
        }
    }

    @Override
    public void characters(char[] data, int offset, int length) throws SAXException{
        String value = new String(data, offset, length).trim();
        if(value.length() > 0){
            if(qname.equals("name") && propertyPart){
                key = new String(data, offset, length).trim();
            }
            else if(qname.equals("value") && propertyPart){
                context.addProperty(key, new String(data, offset, length).trim());
            }
            else if(wellknownPart
                    && (qname.equals("suffix") || qname.equals("prefix") || qname.equals("match"))){
                manager.add(new WellknownClassSection(new String(data, offset, length),
                        wellknownType | patternType));
            }
        }
    }
}
