package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import javax.swing.JPanel;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class BirthmarkSelectablePane extends JPanel implements BirthmarkServiceHolder{

    public abstract void setExpertMode(boolean expertmode);

    public abstract boolean isExpertMode();

    public abstract void reset();

    public abstract void addDataChangeListener(DataChangeListener listener);

    public abstract String[] getServices();

    public abstract String[] getSelectedServices();

    public abstract void addService(BirthmarkSpi service);

    public abstract BirthmarkSpi getService(String type);

}