package jp.naist.se.stigmata.format.xml;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairElement;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.format.AbstractBirthmarkComparisonResultFormat;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkComparisonResultXmlFormat extends AbstractBirthmarkComparisonResultFormat{
    private BirthmarkExtractionListXmlFormat list;

    public BirthmarkComparisonResultXmlFormat(BirthmarkExtractionListXmlFormat list){
        this.list = list;
    }

    @Override
    public void printResult(PrintWriter out, ComparisonPair pair){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark>");
        out.println("  <extracted-birthmarks>");
        list.printBirthmarkSet(out, pair.getTarget1());
        list.printBirthmarkSet(out, pair.getTarget2());
        out.println("  </extracted-birthmarks>");
        out.println("  <comparison-result-set>");
        printComparisonPair(out, pair);
        out.println("  </comparison-result-set>");
        out.println("</birthmark>");
    }

    @Override
    public void printResult(PrintWriter out, ComparisonResultSet resultset){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<birthmark>");
        out.println("  <comparison-result-set>");
        for(ComparisonPair pair: resultset){
            printComparisonPair(out, pair);
        }
        out.println("  </comparison-result-set>");
        out.println("</birthmark>");
    }

    private void printComparisonPair(PrintWriter out, ComparisonPair pair){
        out.println("    <comparison-result>");
        printTarget(out, pair.getTarget1(), 1);
        printTarget(out, pair.getTarget2(), 2);
        out.println("      <birthmark-similarities>");
        for(ComparisonPairElement element: pair){
            printPairElement(out, element);
        }
        out.println("      </birthmark-similarities>");
        out.print("      <similarity>");
        out.print(pair.calculateSimilarity());
        out.println("</similarity>");
        out.println("    </comparison-result>");
    }

    private void printTarget(PrintWriter out, BirthmarkSet set, int index){
        out.printf("      <target%d>%n", index);
        out.printf("        <class-name>%s</class-name>%n", list.escapeToXmlString(set.getName()));
        out.printf("        <location>%s</location>%n", list.escapeToXmlString(set.getLocation()));
        out.printf("      </target%d>%n", index);

    }

    private void printPairElement(PrintWriter out, ComparisonPairElement e){
        out.printf("        <birthmark-similarity type=\"%s\" comparison-count=\"%d\">%g</birthmark-similarity>%n",
                   e.getType(), e.getComparisonCount(), e.getSimilarity());

    }
}
