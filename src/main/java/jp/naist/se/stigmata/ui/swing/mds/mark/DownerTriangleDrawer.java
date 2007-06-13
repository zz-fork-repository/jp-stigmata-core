package jp.naist.se.stigmata.ui.swing.mds.mark;

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
class DownerTriangleDrawer extends AbstractMarkDrawer{
    public DownerTriangleDrawer(){
        super();
    }

    public DownerTriangleDrawer(boolean fill){
        super(fill);
    }

    @Override
    public Shape getPlainShape(int w, int h){
        float ww = (float)w / 2f;
        float hh = (float)h / 2f;

        GeneralPath path = new GeneralPath();
        path.moveTo(ww - 2f, hh - 2f);
        path.lineTo(ww + 2f, hh - 2f);
        path.lineTo(ww, hh - 2f + (float)(2 * Math.sqrt(3)));
        path.lineTo(ww - 2f, hh - 2f);

        return path;
    }

    @Override
    public Shape getOveredShape(int w, int h){
        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.lineTo(w, 0);
        path.lineTo(w / 2f, h);
        path.lineTo(0, 0);

        return path;
    }

}
