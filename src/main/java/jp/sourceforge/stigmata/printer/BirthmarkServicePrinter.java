package jp.sourceforge.stigmata.printer;

import java.io.IOException;
import java.io.PrintWriter;

import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkServicePrinter{
    public void printResult(PrintWriter out, BirthmarkService[] spilist) throws IOException;

    public String getResult(BirthmarkService[] spilist);
}
