package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.util.EventListener;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface DataChangeListener extends EventListener{
    public void valueChanged(Object source);
}
