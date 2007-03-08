package jp.naist.se.stigmata.ui.swing;

/**
 *  $Id$
 */

import java.io.Serializable;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class BirthmarkSelection implements Serializable{
    private static final long serialVersionUID = -3244323970546344L;

    private BirthmarkSpi service;
    private boolean selected = true;

    public BirthmarkSelection(BirthmarkSpi service){
        this.service = service;
    }

    public BirthmarkSpi getService(){
        return service;
    }

    public boolean isVisible(boolean expertFlag){
        return expertFlag || (!expertFlag && !service.isExpert());
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean flag){
        this.selected = flag;
    }

    public String getType(){
        return service.getType();
    }
}