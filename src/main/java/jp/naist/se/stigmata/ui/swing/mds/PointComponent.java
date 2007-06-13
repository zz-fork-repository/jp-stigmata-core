package jp.naist.se.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import jp.naist.se.stigmata.ui.swing.mds.mark.DrawerFactory;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class PointComponent extends JLabel{
    private static final long serialVersionUID = 6945871049990818511L;
    public static final Color DEFAULT_OVER_COLOR = Color.BLUE;

    private boolean entered = false;
    private Color overColor = DEFAULT_OVER_COLOR;
    private String label;
    private MarkDrawer drawer;
    private JLabel showLabel = null;
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public PointComponent(String label, double x, double y){
        this(label);

        setToolTipText(String.format("%s [%g, %g]", label, x, y));
        drawer = DrawerFactory.getInstance().create(GeometoryType.UPPER_TRIANGLE);
    }

    public PointComponent(String label, double x, double y, MarkDrawer drawer){
        this(label);
        this.drawer = drawer;
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
                if(showLabel != null){
                    showLabel.setVisible(true);
                    showLabel.repaint();
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e){
                entered = false;
                if(showLabel != null){
                    showLabel.setVisible(false);
                    showLabel.repaint();
                }
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() >= 2){
                    ActionEvent ae = new ActionEvent(PointComponent.this, 0, null);
                    for(ActionListener listener: listeners){
                        listener.actionPerformed(ae);
                    }
                }
            }
        });
        setToolTipText(this.label);
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener){
        listeners.remove(listener);
    }

    public void setShowLabel(JLabel label){
        this.showLabel = label;
    }

    public JLabel getShowLabel(){
        return showLabel;
    }

    public String getLabel(){
        return label;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D gg = (Graphics2D)g;
        Stroke stroke = gg.getStroke();
        Dimension d = getSize();

        if(entered){
            g.setColor(getOverColor());
            drawer.drawOver(gg, d.width, d.height);
        }
        else{
            g.setColor(getForeground());
            drawer.draw(gg, d.width, d.height);
        }
        gg.setStroke(stroke);
    }

    public Color getOverColor(){
        return overColor;
    }

    public void setOverColor(Color overColor){
        this.overColor = overColor;
    }
}
