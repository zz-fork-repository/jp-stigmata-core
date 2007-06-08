package jp.naist.se.stigmata.ui.swing.mds.mark;

import java.awt.BasicStroke;
import java.awt.Stroke;

public abstract class LineMarkDrawer extends AbstractMarkDrawer{
    public LineMarkDrawer(){
    }

    @Override
    public boolean isFilled(){
        return false;
    }

    @Override
    public void setFilled(boolean fill){
        // nothing to to.
    }

    @Override
    public Stroke getOverStroke(){
        return new BasicStroke(3f);
    }

}
