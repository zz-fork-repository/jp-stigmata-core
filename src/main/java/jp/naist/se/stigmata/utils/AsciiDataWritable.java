package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

import jp.naist.se.stigmata.ui.swing.UnsupportedFormatException;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface AsciiDataWritable{
    public void writeAsciiData(PrintWriter out, String format) throws IOException, UnsupportedFormatException;
}
