package jp.sourceforge.stigmata.ui.swing.tab;

/*
 * $Id$
 */

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * This program is copied from below URL.
 * http://terai.xrea.jp/Swing/EditTabTitle.html
 * 
 * @author Haruaki Tamada
 * @author Terai Atsuhiro
 * @version $Revision$ 
 */
class EditableGlassPane extends JPanel{
    private static final long serialVersionUID = 9009103705988625476L;

    private EditableTabbedPane pane;
    private Rectangle rect;

    public EditableGlassPane(EditableTabbedPane panel){
        super((LayoutManager)null);
        this.pane = panel;

        setOpaque(false);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(rect == null || rect.contains(e.getPoint()))
                    return;
                pane.renameTab();
            }
        });
    }

    public void setRectangle(int x, int y, int w, int h){
        setRectangle(new Rectangle(x, y, w, h));
    }

    public void setRectangle(Rectangle rect){
        this.rect = rect;
    }

    public Rectangle getRectangle(){
        return rect;
    }
}
