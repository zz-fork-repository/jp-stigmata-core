package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkServiceListFormat implements BirthmarkServiceListFormat{
    public abstract void printResult(PrintWriter out, BirthmarkSpi[] spilist);

    public String getResult(BirthmarkSpi[] spilist){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, spilist);

        out.close();
        return writer.toString();
    }

}