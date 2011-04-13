package jp.sourceforge.stigmata.ui.swing;

import java.io.Serializable;

import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * @author Haruaki TAMADA
 */
class BirthmarkSelection implements Serializable{
    private static final long serialVersionUID = -3244323970546344L;

    private BirthmarkService service;
    private boolean selected = true;

    public BirthmarkSelection(BirthmarkService service){
        this.service = service;
    }

    public BirthmarkService getService(){
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