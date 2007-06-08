package jp.naist.se.stigmata.ui.swing.mds.mark;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class XMarkDrawer extends LineMarkDrawer{
    public XMarkDrawer(){
    }

    @Override
    public Shape getPlainShape(int w, int h){
        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.lineTo(w, h);
        path.moveTo(w, 0);
        path.lineTo(0, h);

        return path;
    }

    @Override
    public Shape getOveredShape(int w, int h){
        return getPlainShape(w, h);
    }
}
