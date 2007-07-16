package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.OutputStream;

import jp.naist.se.stigmata.ui.swing.UnsupportedFormatException;

/**
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface BinaryDataWritable{
    public void writeBinaryData(OutputStream out, String format) throws IOException, UnsupportedFormatException;
}
