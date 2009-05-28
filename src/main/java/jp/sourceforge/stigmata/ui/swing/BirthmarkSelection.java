package jp.sourceforge.stigmata.ui.swing;

/**
 *  $Id$
 */

import java.io.Serializable;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ 
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

    public boolean isVisible(boolean experimentalFlag){
        return experimentalFlag || (!experimentalFlag && !service.isExperimental());
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