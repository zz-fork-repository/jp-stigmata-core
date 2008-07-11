package jp.sourceforge.stigmata.utils;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

import jp.sourceforge.stigmata.ui.swing.UnsupportedFormatException;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public interface AsciiDataWritable{
    public void writeAsciiData(PrintWriter out, String format) throws IOException, UnsupportedFormatException;
}
