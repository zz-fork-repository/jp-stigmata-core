package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.sourceforge.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ExtractionResultSetPrinter{
    public void printResult(PrintWriter out, ExtractionResultSet ers);

    public String getResult(ExtractionResultSet ers);
}
