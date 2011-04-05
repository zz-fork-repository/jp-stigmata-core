package jp.sourceforge.stigmata.printer;

import java.io.IOException;
import java.io.PrintWriter;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkServicePrinter{
    public void printResult(PrintWriter out, BirthmarkSpi[] spilist) throws IOException;

    public String getResult(BirthmarkSpi[] spilist);
}
