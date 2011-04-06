package jp.sourceforge.stigmata.ui.swing;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki Tamada
 */
public interface BirthmarkServiceListener{
    public void serviceAdded(BirthmarkSpi service);

    public void serviceRemoved(BirthmarkSpi service);
}
