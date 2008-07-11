package jp.sourceforge.stigmata.utils;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.OutputStream;

import jp.sourceforge.stigmata.ui.swing.UnsupportedFormatException;

/**
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public interface BinaryDataWritable{
    public void writeBinaryData(OutputStream out, String format) throws IOException, UnsupportedFormatException;
}
