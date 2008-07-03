package jp.sourceforge.stigmata.ui.swing.mds.mark;

/*
 * $Id$
 */

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class RectangleDrawer extends AbstractMarkDrawer{

    public RectangleDrawer(){
        super();
    }

    public RectangleDrawer(boolean fill){
        super(fill);
    }

    @Override
    public Shape getPlainShape(int w, int h){
        double ww = (double)w / 2d;
        double hh = (double)h / 2d;

        return new Rectangle2D.Double(ww - 2d, hh - 2d, 4d, 4d);
    }

    @Override
    public Shape getOveredShape(int w, int h){
        return new Rectangle2D.Double(0, 0, w - 1, h - 1);
    }
}
