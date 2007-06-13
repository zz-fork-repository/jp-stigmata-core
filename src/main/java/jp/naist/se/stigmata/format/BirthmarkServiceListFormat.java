package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkServiceListFormat{
    public void printResult(PrintWriter out, BirthmarkSpi[] spilist) throws IOException;

    public String getResult(BirthmarkSpi[] spilist);
}
