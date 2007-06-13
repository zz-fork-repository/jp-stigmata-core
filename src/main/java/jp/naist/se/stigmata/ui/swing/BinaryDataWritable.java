package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface BinaryDataWritable{
    public void writeBinaryData(OutputStream out, String format) throws IOException, UnsupportedFormatException;
}
