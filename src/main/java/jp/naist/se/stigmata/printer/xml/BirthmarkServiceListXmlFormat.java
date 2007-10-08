package jp.naist.se.stigmata.printer.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.printer.AbstractBirthmarkServiceListFormat;
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
        out.println("<birthmark-result-set>");
        out.println("  <birthmark-services>");
        for(BirthmarkSpi spi: spilist){
            out.println("    <birthmark-service>");
            out.printf("      <type>%s</type>%n", spi.getType());
            out.printf("      <display-name>%s</display-name>%n", spi.getDisplayType());
            out.printf("      <description>%s</description>%n", spi.getDescription());
            out.printf("      <class-name>%s</class-name>%n", spi.getClass().getName());
            out.printf("      <extractor>%s</extractor>%n", spi.getExtractorClassName());
            out.printf("      <comparator>%s</comparator>%n", spi.getComparatorClassName());
            out.println("    </birthmark-service>");
        }
        out.println("  </birthmark-services>");
        out.println("</birthmark-result-set>");

        out.flush();
    }
}
