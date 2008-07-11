package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.sourceforge.stigmata.ComparisonPair;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public abstract class AbstractComparisonPairPrinter implements ComparisonPairPrinter, Printer{

    public abstract void printResult(PrintWriter out, ComparisonPair pair);

    public void printFooter(PrintWriter out){
        out.flush();
    }

    public void printHeader(PrintWriter out){
    }

    public String getResult(ComparisonPair pair){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, pair);

        out.close();
        return writer.toString();
    }
}
