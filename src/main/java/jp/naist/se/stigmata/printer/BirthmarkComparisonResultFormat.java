package jp.naist.se.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkComparisonResultFormat{
    public void printResult(PrintWriter out, ComparisonResultSet resultset);

    public String getResult(ComparisonResultSet resultset);

    public String getResult(ComparisonPair pair);

    public void printResult(PrintWriter out, ComparisonPair pair);
}
