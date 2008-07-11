package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public interface BirthmarkServiceListener{
    public void serviceAdded(BirthmarkSpi service);

    public void serviceRemoved(BirthmarkSpi service);
}
