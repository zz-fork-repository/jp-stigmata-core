package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.sourceforge.stigmata.ComparisonPair;

/**
 * 
 * @author Haruaki Tamada
 */
public interface ComparisonPairPrinter{
    public String getResult(ComparisonPair pair);

    public void printResult(PrintWriter out, ComparisonPair pair);
}
