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
 */
public abstract class AbstractBirthmarkServicePrinter implements BirthmarkServicePrinter, Printer{
    @Override
    public abstract void printResult(PrintWriter out, BirthmarkSpi[] spilist);

    @Override
    public String getResult(BirthmarkSpi[] spilist){
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);

        printResult(out, spilist);

        out.close();
        return writer.toString();
    }

    @Override
    public void printHeader(PrintWriter out){
    }

    @Override
    public void printFooter(PrintWriter out){
        out.flush();
    }
}
