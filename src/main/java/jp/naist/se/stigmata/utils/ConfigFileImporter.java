package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.birthmarks.BirthmarkService;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * configuration file parser.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ConfigFileImporter{
    private BirthmarkEnvironment environment;

    public ConfigFileImporter(BirthmarkEnvironment environment){
        this.environment = environment;
    }

    public ConfigFileImporter(){
        // generate environment.
    }

    public BirthmarkEnvironment parse(InputStream in) throws IOException{
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            Handler handler = new Handler(getEnvironment());
            parser.parse(in, handler);
            this.environment = handler.getEnvironment();

            return environment;
        }catch(ParserConfigurationException e){
            throw new IOException(e.getMessage());
        }catch(SAXException e){
            throw new IOException(e.getMessage());
        }
    }

    public BirthmarkEnvironment getEnvironment(){
        return environment;
    }

    private static enum Part{
        WELLKNOWN_CLASSES, PROPERTIES, CLASSPATH, SERVICES, FILTER_SET, FILTER_DEFINITION,
    }

    private static class Handler extends DefaultHandler{
        private BirthmarkEnvironment environment;
        private WellknownClassManager manager;
        private BirthmarkService service;
        private ComparisonPairFilterSet filter;
        private Part part;
        private int wellknownType = 0;
        private int patternType = 0;
        private String qname;
        private String key;
        private String filterType, filterCriterion, attributeName;
        private Map<String, String> filterAttributes = new HashMap<String, String>();

        public Handler(BirthmarkEnvironment environment){
            if(environment == null){
                environment = new BirthmarkEnvironment();
            }
            this.environment = environment;
            this.manager = environment.getWellknownClassManager();
        }

        public BirthmarkEnvironment getEnvironment(){
            return environment;
        }

        @Override
        public void startElement(String uri, String localName, String qname,
                Attributes attributes) throws SAXException{
            this.qname = qname;

            if(qname.equals("wellknown-classes")){
                part = Part.WELLKNOWN_CLASSES;
            }
            else if(qname.equals("property")){
                part = Part.PROPERTIES;
            }
            else if(qname.equals("classpath-list")){
                part = Part.CLASSPATH;
            }
            else if(qname.equals("birthmark-service")){
                part = Part.SERVICES;
                service = new BirthmarkService();
                service.setUserDefined(false);
            }
            else if(qname.equals("filterset-list")){
                part = Part.FILTER_SET;
            }

            if(part == Part.FILTER_SET){
                if(qname.equals("filterset")){
                    filter = new ComparisonPairFilterSet();
                }
                else if(qname.equals("filter")){
                    part = Part.FILTER_DEFINITION;
                    filterAttributes.clear();
                }
            }
            else if(part == Part.WELLKNOWN_CLASSES){
                if(qname.equals("exclude")){
                    wellknownType = WellknownClassJudgeRule.EXCLUDE_TYPE;
                }
                else if(qname.equals("package")){
                    wellknownType = WellknownClassJudgeRule.PACKAGE_TYPE;
                }
                else if(qname.equals("class-name")){
                    wellknownType = WellknownClassJudgeRule.CLASS_NAME_TYPE;
                }
                else if(qname.equals("fully-name")){
                    wellknownType = WellknownClassJudgeRule.FULLY_TYPE;
                }
                else if(qname.equals("suffix")){
                    patternType = WellknownClassJudgeRule.SUFFIX_TYPE;
                }
                else if(qname.equals("prefix")){
                    patternType = WellknownClassJudgeRule.PREFIX_TYPE;
                }
                else if(qname.equals("match")){
                    patternType = WellknownClassJudgeRule.MATCH_TYPE;
                }
            }
        }

        @Override
        public void characters(char[] data, int offset, int length)
                throws SAXException{
            String value = new String(data, offset, length).trim();

            if(value.length() > 0){
                if(part == Part.PROPERTIES){
                    if(qname.equals("name")){
                        key = value;
                    }
                    else if(qname.equals("value")){
                        environment.addProperty(key, value);
                    }
                }
                else if(part == Part.WELLKNOWN_CLASSES
                        && (qname.equals("suffix") || qname.equals("prefix") || qname
                                .equals("match"))){
                    manager.add(new WellknownClassJudgeRule(value,
                            wellknownType | patternType));
                }
                else if(part == Part.CLASSPATH && qname.equals("classpath")){
                    try{
                        environment.getClasspathContext().addClasspath(
                                new URL(value));
                    }catch(MalformedURLException e){
                        throw new SAXException(e);
                    }
                }
                else if(part == Part.SERVICES){
                    if(qname.equals("type"))
                        service.setType(value);
                    else if(qname.equals("display-name"))
                        service.setDisplayType(value);
                    else if(qname.equals("description"))
                        service.setDescription(value);
                    else if(qname.equals("extractor"))
                        service.setExtractorClassName(value);
                    else if(qname.equals("comparator"))
                        service.setComparatorClassName(value);
                }
                else if(part == Part.FILTER_SET){
                    if(qname.equals("name")){
                        filter.setName(value);
                    }
                    else if(qname.equals("match")){
                        if(value.equals("all")){
                            filter.setMatchAll();
                        }
                        else{
                            filter.setMatchAny();
                        }
                    }
                }
                else if(part == Part.FILTER_DEFINITION){
                    if(qname.equals("filter-type")){
                        filterType = value;
                    }
                    else if(qname.equals("criterion")){
                        filterCriterion = value;
                    }
                    else if(qname.equals("name")){
                        attributeName = value;
                    }
                    else{
                        filterAttributes.put(attributeName, value);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localname, String qname){
            if(part == Part.SERVICES && qname.equals("birthmark-service")){
                environment.addService(service);
                service = null;
            }
            else if(part == Part.FILTER_DEFINITION && qname.equals("filter")){
                ComparisonPairFilter f = environment.getFilterManager().buildFilter(
                    filterType, filterCriterion, filterAttributes
                );
                filter.addFilter(f);
                part = Part.FILTER_SET;
            }
            else if(part == Part.FILTER_SET && qname.equals("filterset")){
                environment.getFilterManager().addFilterSet(filter);
            }
        }
    }
}
