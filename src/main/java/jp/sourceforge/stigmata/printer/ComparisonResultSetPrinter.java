package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.sourceforge.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface ComparisonResultSetPrinter{
    public void printResult(PrintWriter out, ComparisonResultSet resultset);

    public String getResult(ComparisonResultSet resultset);
}
