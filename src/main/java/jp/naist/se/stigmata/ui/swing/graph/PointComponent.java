package jp.naist.se.stigmata.ui.swing.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

class PointComponent extends JLabel{
    private static final long serialVersionUID = 6945871049990818511L;
    public static final Color DEFAULT_OVER_COLOR = Color.BLUE;
    private String label;
    private boolean entered = false;
    private Color overColor = DEFAULT_OVER_COLOR;

    public PointComponent(String label, double x, double y){
        this(label);

        setToolTipText(String.format("%s [%g, %g]", label, x, y));
    }

    public PointComponent(String label){
        this.label = label;
        setSize(10, 10);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
        setPreferredSize(getSize());

        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                entered = true;
                updateUI();
            }

            @Override
            public void mouseExited(MouseEvent e){
                entered = false;
                updateUI();
            }
        });
        setToolTipText(this.label);
    }

    public String getLabel(){
        return label;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(entered){
            g.setColor(getOverColor());
            g.fillOval(0, 0, 10, 10);
        }
        else{
            g.setColor(getForeground());
            g.fillOval(4, 4, 3, 3);
        }
    }

    public Color getOverColor(){
        return overColor;
    }

    public void setOverColor(Color overColor){
        this.overColor = overColor;
    }
}
