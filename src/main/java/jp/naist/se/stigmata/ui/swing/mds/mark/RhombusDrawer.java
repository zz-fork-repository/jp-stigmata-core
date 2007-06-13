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
class RhombusDrawer extends AbstractMarkDrawer{

    public RhombusDrawer(){
        super();
    }

    public RhombusDrawer(boolean fill){
        super(fill);
    }

    @Override
    public Shape getPlainShape(int w, int h){
        float ww = (float)w / 2f;
        float hh = (float)h / 2f;

        GeneralPath path = new GeneralPath();
        path.moveTo(ww,      hh - 2f);
        path.lineTo(ww + 2f, hh);
        path.lineTo(ww,      hh + 2f);
        path.lineTo(ww - 2f, hh);
        path.lineTo(ww,      hh - 2f);

        return path;
    }

    @Override
    public Shape getOveredShape(int w, int h){
        float ww = (float)w / 2f;
        float hh = (float)h / 2f;

        GeneralPath path = new GeneralPath();
        path.moveTo(ww,     0);
        path.lineTo(0,      hh);
        path.lineTo(ww,     h - 1f);
        path.lineTo(w - 1f, hh);
        path.lineTo(ww,     0);

        return path;
    }

}
