package jp.naist.se.stigmata.format.csv;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.format.AbstractBirthmarkServiceListFormat;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkServiceListCsvFormat extends AbstractBirthmarkServiceListFormat{
    public void printResult(PrintWriter out, BirthmarkSpi[] spilist){
        for(BirthmarkSpi spi: spilist){
            out.print(spi.getType());
            out.print(",");
            out.print(spi.getDisplayType());
            out.print(",");
            out.print(spi.getClass().getName());
            out.print(",");
            out.print(spi.getDescription());
            out.println();
        }
        out.flush();
    }
}
