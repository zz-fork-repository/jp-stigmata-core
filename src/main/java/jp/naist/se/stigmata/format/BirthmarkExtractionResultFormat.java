package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.ExtractionResultSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkExtractionResultFormat{
    public void printResult(PrintWriter out, ExtractionResultSet ers);

    public String getResult(ExtractionResultSet ers);
}
