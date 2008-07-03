package jp.sourceforge.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class MarkIcon implements Icon{
    private static final int DEFAULT_ICON_WIDTH = 16;
    private static final int DEFAULT_ICON_HEIGHT = 16;

    private MarkDrawer drawer;
    private int width = -1;
    private int height = -1;

    public MarkIcon(MarkDrawer drawer){
        this.drawer = drawer;
    }

    public void setIconHeight(int height){
        this.height = height;
    }

    public int getIconHeight(){
        if(height < 0){
            return DEFAULT_ICON_HEIGHT;
        }
        return height;
    }

    public void setIconWidth(int width){
        this.width = width;
    }

    public int getIconWidth(){
        if(width < 0){
            return DEFAULT_ICON_WIDTH;
        }
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y){
        Color initialColor = g.getColor();
        g.setColor(Color.BLACK);
        g.translate(x, y);
        drawer.drawOver((Graphics2D)g, getIconWidth(), getIconHeight());
        g.translate(-x, -y);
        g.setColor(initialColor);
    }
}
