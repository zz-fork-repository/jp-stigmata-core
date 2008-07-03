package jp.sourceforge.stigmata.ui.swing.actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopupShowAction extends MouseAdapter{
    private JPopupMenu popup;

    public PopupShowAction(JPopupMenu popup){
        this.popup = popup;
    }

    @Override
    public void mousePressed(MouseEvent e){
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e){
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e){
        if(e.isPopupTrigger()){
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
