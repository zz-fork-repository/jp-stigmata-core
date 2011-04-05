package jp.sourceforge.stigmata.printer;

import java.io.PrintWriter;

import jp.sourceforge.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface ExtractionResultSetPrinter{
    public void printResult(PrintWriter out, ExtractionResultSet ers);

    public String getResult(ExtractionResultSet ers);
}
