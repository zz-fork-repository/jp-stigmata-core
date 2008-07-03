package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.sourceforge.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractExtractionResultSetPrinter implements ExtractionResultSetPrinter{
    public abstract void printResult(PrintWriter out, ExtractionResultSet ers);

    public void printHeader(PrintWriter out){
    }

    public void printFooter(PrintWriter out){
        out.flush();
    }

    public String getResult(ExtractionResultSet ers){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, ers);

        out.close();
        return writer.toString();
    }
}
