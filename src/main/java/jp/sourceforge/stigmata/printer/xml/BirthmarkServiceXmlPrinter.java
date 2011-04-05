package jp.sourceforge.stigmata.printer.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.sourceforge.stigmata.printer.AbstractBirthmarkServicePrinter;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class BirthmarkServiceXmlPrinter extends AbstractBirthmarkServicePrinter{
    @Override
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
