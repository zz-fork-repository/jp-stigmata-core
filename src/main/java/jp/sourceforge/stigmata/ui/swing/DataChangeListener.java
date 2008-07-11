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
 * @version $Revision$ 
 */
public interface DataChangeListener extends EventListener{
    public void valueChanged(Object source);
}
