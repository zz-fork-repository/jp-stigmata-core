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
public interface BirthmarkServiceHolder{
    public void addService(BirthmarkSpi service);

    public boolean hasService(String type);

    public BirthmarkSpi getService(String type);

    public void removeService(String type);
}
