package jp.naist.se.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.Graphics2D;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface MarkDrawer{
    public void draw(Graphics2D g, int w, int h);

    public void drawOver(Graphics2D g, int w, int h);
}
