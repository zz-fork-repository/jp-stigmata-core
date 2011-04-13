package jp.sourceforge.stigmata.ui.swing;

import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public interface BirthmarkServiceListener{
    public void serviceAdded(BirthmarkService service);

    public void serviceRemoved(BirthmarkService service);
}
