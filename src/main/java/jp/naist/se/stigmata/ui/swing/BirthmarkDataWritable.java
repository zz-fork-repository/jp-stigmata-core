package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkDataWritable{
    public void writeData(PrintWriter out, ResultFormatSpi service) throws IOException;
}
