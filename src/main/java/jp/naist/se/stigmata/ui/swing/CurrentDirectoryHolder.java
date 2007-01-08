package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.io.File;

/**
 * This interface has current directory.
 *
 * @author tamada
 * @version $Revision$ $Date$
 */
public interface CurrentDirectoryHolder {
    public File getCurrentDirectory();

    public void setCurrentDirectory(File file);
}
