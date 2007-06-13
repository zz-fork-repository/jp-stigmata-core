package jp.naist.se.stigmata.ui.swing.mds.mark;

/*
 * $Id$
 */

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

import jp.naist.se.stigmata.ui.swing.mds.MarkDrawer;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
abstract class AbstractMarkDrawer implements MarkDrawer{
    private boolean fill;

    public AbstractMarkDrawer(){
        this(false);
    }

    public AbstractMarkDrawer(boolean fill){
        setFilled(fill);
    }

    public boolean isFilled(){
        return fill;
    }

    public void setFilled(boolean fill){
        this.fill = fill;
    }

    public abstract Shape getPlainShape(int w, int h);

    public abstract Shape getOveredShape(int w, int h);

    public Stroke getPlainStroke(){
        return null;
    }

    public Stroke getOverStroke(){
        return null;
    }

    public void draw(Graphics2D g, int w, int h){
        Stroke s = getPlainStroke();
        if(s != null){
            g.setStroke(s);
        }
        drawImpl(g, getPlainShape(w, h));
    }

    public void drawOver(Graphics2D g, int w, int h){
        Stroke s = getOverStroke();
        if(s != null){
            g.setStroke(s);
        }
        drawImpl(g, getOveredShape(w, h));
    }

    private void drawImpl(Graphics2D g, Shape s){
        if(isFilled()){
            g.fill(s);
        }
        else{
            g.draw(s);
        }
    }
}
