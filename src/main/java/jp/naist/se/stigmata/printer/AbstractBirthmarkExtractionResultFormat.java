package jp.naist.se.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.naist.se.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkExtractionResultFormat implements BirthmarkExtractionResultFormat{
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
