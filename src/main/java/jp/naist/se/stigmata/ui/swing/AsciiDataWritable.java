package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface AsciiDataWritable{
    public void writeAsciiData(PrintWriter out, String format) throws IOException, UnsupportedFormatException;
}
