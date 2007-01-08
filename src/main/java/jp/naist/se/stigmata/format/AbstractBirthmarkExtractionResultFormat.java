package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.naist.se.stigmata.BirthmarkSet;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkExtractionResultFormat implements BirthmarkExtractionResultFormat{
    public abstract void printResult(PrintWriter out, BirthmarkSet[] holders);

    public String getResult(BirthmarkSet[] holders){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, holders);

        out.close();
        return writer.toString();
    }
}
