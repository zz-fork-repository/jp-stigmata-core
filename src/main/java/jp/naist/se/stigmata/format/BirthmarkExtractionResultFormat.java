package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.BirthmarkSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkExtractionResultFormat{
    public void printResult(PrintWriter out, BirthmarkSet[] holders);

    public String getResult(BirthmarkSet[] holders);
}
