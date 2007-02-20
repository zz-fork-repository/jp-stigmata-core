package jp.naist.se.stigmata.format.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.util.Iterator;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.format.AbstractBirthmarkExtractionResultFormat;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionListXmlFormat extends AbstractBirthmarkExtractionResultFormat{
    public void printResult(PrintWriter out, BirthmarkSet[] holders){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<stigmata>");
        out.println("  <extracted-birthmarks>");
        for(int i = 0; i < holders.length; i++){
            printBirthmarkHolder(out, holders[i]);
        }
        out.println("  </extracted-birthmarks>");
        out.println("</stigmata>");
    }

    protected void printBirthmarkHolder(PrintWriter out, BirthmarkSet holder){
        out.println("    <extracted-birthmark>");
        out.print("      <class-name>");
        out.print(escapeToXmlString(holder.getClassName()));
        out.println("</class-name>");
        out.print("      <location>");
        out.print(escapeToXmlString(holder.getLocation()));
        out.println("</location>");
        for(Iterator<String> i = holder.birthmarkTypes(); i.hasNext(); ){
            String type = i.next();
            Birthmark birthmark = holder.getBirthmark(type);
            out.print("      <birthmark type=\"");
            out.print(birthmark.getType());
            out.print("\" count=\"");
            out.print(birthmark.getElementCount());
            out.println("\">");
            for(Iterator<BirthmarkElement> elements = birthmark.iterator(); elements.hasNext(); ){
                out.print("        <element>");
                out.print(escapeToXmlString(elements.next().toString()));
                out.println("</element>");
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
