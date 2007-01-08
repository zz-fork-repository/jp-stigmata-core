package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkComparisonResultFormat implements BirthmarkComparisonResultFormat{
    public abstract void printResult(PrintWriter out, ComparisonResultSet resultset);

    public abstract void printResult(PrintWriter out, ComparisonPair pair);

    public String getResult(ComparisonResultSet resultset){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, resultset);

        out.close();
        return writer.toString();
    }

    public String getResult(ComparisonPair pair){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, pair);

        out.close();
        return writer.toString();
    }
}
