package jp.naist.se.stigmata.ui.swing.mds;

import java.awt.Graphics2D;

public interface MarkDrawer{
    public void draw(Graphics2D g, int w, int h);

    public void drawOver(Graphics2D g, int w, int h);
}
