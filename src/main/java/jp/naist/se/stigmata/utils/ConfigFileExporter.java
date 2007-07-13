package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.birthmarks.BirthmarkService;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Export birthmark context to xml file.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ConfigFileExporter{
    private BirthmarkEnvironment context;

    public ConfigFileExporter(BirthmarkEnvironment context){
        this.context = context;
    }

    public void export(BirthmarkEnvironment context, PrintWriter out) throws IOException{
        new ConfigFileExporter(context).export(out);
    }

    public void export(PrintWriter out) throws IOException{
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<stigmata>");

        exportServices(out);
        exportFilters(out);
        exportWellknownClasses(out);
        exportClasspath(out);
        exportProperties(out);

        out.println("</stigmata>");
        out.flush();
    }

    private void exportProperties(PrintWriter out) throws IOException{
        out.println("  <properties>");
        for(Iterator<String> i = context.propertyKeys(); i.hasNext(); ){
            String key = i.next();
            String value = context.getProperty(key);
            out.println("    <property>");
            out.printf("      <name>%s</name>%n", key);
            out.printf("      <value>%s</value>%n", value);
            out.println("    </property>");
        }
        out.println("  </properties>");
    }

    private void exportClasspath(PrintWriter out) throws IOException{
        out.println("  <classpath-list>");
        for(URL location: context.getClasspathContext()){
            out.printf("    <classpath>%s</classpath>%n", location.toString());
        }
        out.println("  </classpath-list>");
    }

    private void exportWellknownClasses(PrintWriter out) throws IOException{
        out.println("  <wellknown-classes>");
        for(WellknownClassJudgeRule rule: context.getWellknownClassManager().getSections()){
            int partType = rule.getMatchPartType();
            int match = rule.getMatchType();
            String value = rule.getName();
            String tag;
            String matchtag;
            if(partType == WellknownClassJudgeRule.CLASS_NAME_TYPE)   tag = "class-name";
            else if(partType == WellknownClassJudgeRule.EXCLUDE_TYPE) tag = "exclude";
            else if(partType == WellknownClassJudgeRule.FULLY_TYPE)   tag = "fully-name";
            else if(partType == WellknownClassJudgeRule.PACKAGE_TYPE) tag = "package";
            else throw new InternalError("unknown part type: " + partType);

            if(match == WellknownClassJudgeRule.MATCH_TYPE)       matchtag = "match";
            else if(match == WellknownClassJudgeRule.PREFIX_TYPE) matchtag = "prefix";
            else if(match == WellknownClassJudgeRule.SUFFIX_TYPE) matchtag = "suffix";
            else throw new InternalError("unknown match type: " + match);

            out.printf("    <%s><%s>%s</%s></%s>%n", tag, matchtag, value, matchtag, tag);
        }
        out.println("  </wellknown-classes>");
    }

    private void exportFilters(PrintWriter out) throws IOException{
        out.println("  <filterset-list>");
        for(ComparisonPairFilterSet filterset: context.getFilterManager().getFilterSets()){
            out.println("    <filterset>");
            out.printf("      <name>%s</name>%n", filterset.getName());
            out.printf("      <match>%s</match>%n", filterset.isMatchAll()? "all": "any");
            out.println("      <filter-list>");
            for(ComparisonPairFilter filter: filterset){
                out.println("        <filter>");
                out.printf("          <filter-type>%s</filter-type>%n", filter.getService().getFilterName());
                out.printf("          <criterion>%s</criterion>%n", filter.getCriterion());
                try{
                    Map props = BeanUtils.describe(filter);
                    props.remove("service");
                    props.remove("class");
                    props.remove("criterion");
                    props.remove("acceptableCriteria");
                    out.println("          <attributes>");
                    for(Object object: props.entrySet()){
                        Map.Entry entry = (Map.Entry)object;
                        out.println("            <attribute>");
                        out.printf("              <name>%s</name>%n", String.valueOf(entry.getKey()));
                        out.printf("              <value>%s</value>%n", String.valueOf(entry.getValue()));
                        out.println("            </attribute>");
                    }
                    out.println("          </attributes>");
                } catch(Exception e){
                    e.printStackTrace();
                }
                out.println("        </filter>");
            }
            out.println("      </filter-list>");
            out.println("    </filterset>");
        }
        out.println("  </filterset-list>");
    }

    private void exportServices(PrintWriter out) throws IOException{
        out.println("  <birthmark-services>");
        for(BirthmarkSpi service: context.getServices()){
            if(service.isExpert() && service instanceof BirthmarkService){
                out.println("    <birthmark-service>");
                out.printf("      <type>%s</type>%n", service.getType());
                out.printf("      <display-name>%s</display-name>%n", service.getDisplayType());
                out.printf("      <description>%s</description>%n", service.getDescription());
                out.printf("      <extractor>%s</extractor>%n", service.getExtractorClassName());
                out.printf("      <comparator>%s</comparator>%n", service.getComparatorClassName());
                out.println("    </birthmark-service>");
            }
        }
        out.println("  </birthmark-services>");
    }
}
