package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.sourceforge.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public abstract class AbstractComparisonResultSetPrinter implements ComparisonResultSetPrinter, Printer{
    public abstract void printResult(PrintWriter out, ComparisonResultSet resultset);

    public void printHeader(PrintWriter out){
    }

    public void printFooter(PrintWriter out){
        out.flush();
    }

    public String getResult(ComparisonResultSet resultset){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, resultset);

        out.close();
        return writer.toString();
    }
}
