package jp.naist.se.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.ComparisonPair;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface ComparisonPairPrinter{
    public String getResult(ComparisonPair pair);

    public void printResult(PrintWriter out, ComparisonPair pair);
}
