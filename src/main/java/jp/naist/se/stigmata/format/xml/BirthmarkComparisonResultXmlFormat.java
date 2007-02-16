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
        out.println("<stigmata>");
        out.println("  <extracted-birthmarks>");
        list.printBirthmarkHolder(out, pair.getTarget1());
        list.printBirthmarkHolder(out, pair.getTarget2());
        out.println("  </extracted-birthmarks>");
        out.println("  <birthmark-compare-result-set>");
        printComparisonPair(out, pair);
        out.println("  </birthmark-compare-result-set>");
        out.println("</stigmata>");
    }

    @Override
    public void printResult(PrintWriter out, ComparisonResultSet resultset){
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<stigmata>");
        out.println("  <birthmark-compare-result-set>");
        for(ComparisonPair pair: resultset){
            printComparisonPair(out, pair);
        }
        out.println("  </birthmark-compare-result-set>");
        out.println("</stigmata>");
    }

    private void printComparisonPair(PrintWriter out, ComparisonPair pair){
        out.println("    <birthmark-compare-result>");
        printTarget(out, pair.getTarget1(), 1);
        printTarget(out, pair.getTarget1(), 2);
        out.println("      <birthmark-similarities>");
        for(ComparisonPairElement element: pair){
            printPairElement(out, element);
        }
        out.println("      </birthmark-similarities>");
        out.print("      <similarity>");
        out.print(pair.calculateSimilarity());
        out.println("      <similarity>");
        out.println("    </birthmark-compare-result>");
    }

    private void printTarget(PrintWriter out, BirthmarkSet holder, int index){
        out.println("      <target" + index + ">");
        out.print("        <class-name>");
        out.print(list.escapeToXmlString(holder.getClassName()));
        out.println("</class-name>");
        out.print("        <location>");
        out.print(list.escapeToXmlString(holder.getLocation()));
        out.println("</location>");
        out.println("      </target" + index + ">");

    }

    private void printPairElement(PrintWriter out, ComparisonPairElement element){
        out.print("        <birthmark-similarity type=\">");
        out.print(element.getType());
        out.print("\" comparisonCount=\"");
        out.print(element.getComparisonCount());
        out.print("\">");
        out.print(element.getSimilarity());
        out.println("</birthmark-similarity");
    }
}
