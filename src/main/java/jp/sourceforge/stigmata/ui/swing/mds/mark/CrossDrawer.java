package jp.sourceforge.stigmata.ui.swing.mds.mark;

/*
 * $Id$
 */

import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class CrossDrawer extends LineMarkDrawer{
    public CrossDrawer(){
    }

    @Override
    public Shape getPlainShape(int w, int h){
        float ww = w / 2f;
        float hh = h / 2f;

        GeneralPath path = new GeneralPath();
        path.moveTo(0, hh);
        path.lineTo(w, hh);
        path.moveTo(ww, 0);
        path.lineTo(ww, h);

        return path;
    }

    @Override
    public Shape getOveredShape(int w, int h){
        return getPlainShape(w, h);
    }
}
