package jp.naist.se.stigmata.format.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.util.Iterator;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.format.AbstractBirthmarkExtractionResultFormat;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionListXmlFormat extends AbstractBirthmarkExtractionResultFormat{
    public void printResult(PrintWriter out, ExtractionResultSet ers){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark>");
        out.println("  <extracted-birthmarks>");
        for(Iterator<BirthmarkSet> i = ers.birthmarkSets(ExtractionTarget.TARGET_BOTH); i.hasNext(); ){
            printBirthmarkSet(out, i.next());
        }
        out.println("  </extracted-birthmarks>");
        out.println("</birthmark>");
        out.flush();
    }

    protected void printBirthmarkSet(PrintWriter out, BirthmarkSet set){
        out.println("    <extracted-birthmark>");
        out.printf("      <class-name>%s</class-name>%n", escapeToXmlString(set.getName()));
        out.printf("      <location>%s</location>%n", escapeToXmlString(set.getLocation()));
        for(Iterator<String> i = set.birthmarkTypes(); i.hasNext(); ){
            String type = i.next();
            Birthmark birthmark = set.getBirthmark(type);
            out.printf("      <birthmark type=\"%s\" count=\"%d\">%n",
                       birthmark.getType(), birthmark.getElementCount());
            for(Iterator<BirthmarkElement> elements = birthmark.iterator(); elements.hasNext(); ){
                out.printf("        <element>%s</element>%n",
                           escapeToXmlString(String.valueOf(elements.next())));
            }
            out.println("      </birthmark>");
        }
        out.println("    </extracted-birthmark>");
    }

    public String escapeToXmlString(Object o){
        if(o != null){
            return escapeToXmlString(o.toString());
        }
        return null;
    }

    public String escapeToXmlString(String string){
        string = string.replaceAll("&",  "&amp;");
        string = string.replaceAll("\"", "&quot;");
        string = string.replaceAll("<",  "&lt;");
        string = string.replaceAll(">",  "&gt;");

        return string;
    }
}
