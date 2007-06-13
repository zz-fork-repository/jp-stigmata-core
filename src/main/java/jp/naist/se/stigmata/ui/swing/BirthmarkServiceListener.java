package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public interface BirthmarkServiceListener{
    public void serviceAdded(BirthmarkSpi service);

    public void serviceRemoved(BirthmarkSpi service);
}
