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
        out.println("<stigmata>");
        out.println("  <services>");
        for(BirthmarkSpi spi: spilist){
            out.println("    <service>");
            out.print("      <name>");
            out.print(spi.getType());
            out.println("</name>");

            out.print("      <description>");
            out.print(spi.getDefaultDescription());
            out.println("</description>");

            out.print("      <class-name>");
            out.print(spi.getClass().getName());
            out.println("</class-name>");
            out.println("    </service>");
        }
        out.println("  </services>");
        out.println("</stigmata>");

        out.flush();
    }
}
