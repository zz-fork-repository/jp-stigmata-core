package jp.naist.se.stigmata.printer.csv;

/*
 * $Id$
 */

import java.io.PrintWriter;

import jp.naist.se.stigmata.printer.AbstractBirthmarkServicePrinter;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkServiceCsvPrinter extends AbstractBirthmarkServicePrinter{
    public void printResult(PrintWriter out, BirthmarkSpi[] spilist){
        printHeader(out);
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
        printFooter(out);
    }
}
