package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class AbstractBirthmarkServicePrinter implements BirthmarkServicePrinter, Printer{
    public abstract void printResult(PrintWriter out, BirthmarkSpi[] spilist);

    public String getResult(BirthmarkSpi[] spilist){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, spilist);

        out.close();
        return writer.toString();
    }

    public void printHeader(PrintWriter out){
    }

    public void printFooter(PrintWriter out){
        out.flush();
    }
}
