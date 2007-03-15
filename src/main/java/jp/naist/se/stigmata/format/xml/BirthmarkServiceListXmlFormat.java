package jp.naist.se.stigmata.format.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.format.AbstractBirthmarkServiceListFormat;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkServiceListXmlFormat extends AbstractBirthmarkServiceListFormat{
    public void printResult(PrintWriter out, BirthmarkSpi[] spilist){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark>");
        out.println("  <birthmark-services>");
        for(BirthmarkSpi spi: spilist){
            out.println("    <birthmark-service>");
            out.print("      <type>");
            out.print(spi.getType());
            out.println("</type>");
            out.print("      <display-type>");
            out.print(spi.getDisplayType());
            out.println("</display-type>");

            out.print("      <description>");
            out.print(spi.getDescription());
            out.println("</description>");

            out.print("      <class-name>");
            out.print(spi.getClass().getName());
            out.println("</class-name>");
            out.println("    </birthmark-service>");
        }
        out.println("  </birthmark-services>");
        out.println("</birthmark>");

        out.flush();
    }
}
