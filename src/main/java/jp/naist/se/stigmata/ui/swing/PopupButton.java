package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Popup button.
 * 
 * @author tamada
 * @version $Revision$ $Date$
 */
public class PopupButton extends JPanel{
    private static final long serialVersionUID = -4428839967597028837L;

    private JButton button;
    private JPopupMenu popup;
    private JButton arrowButton;
    private Icon icon;

    public PopupButton(JButton initButton){
        setLayout(new BorderLayout());

        button = initButton;
        icon = createIcon();
        arrowButton = new JButton(icon);
        popup = new JPopupMenu();
        add(button, BorderLayout.CENTER);
        add(arrowButton, BorderLayout.EAST);

        arrowButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Point p = button.getLocation();
                popup.show(PopupButton.this, p.x, button.getHeight());
            }
        });
        updateUI();
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        button.setEnabled(enabled);
        arrowButton.setEnabled(enabled);
    }

    public JButton getButton(){
        return button;
    }

    public JMenuItem addMenuItem(JMenuItem menuItem){
        return popup.add(menuItem);
    }

    public void updateUI(){
        super.updateUI();
        if(button != null){
            Dimension prefferedSize = button.getPreferredSize();

            arrowButton.setPreferredSize(new Dimension(icon.getIconWidth() + 4, prefferedSize.height));
            setPreferredSize(new Dimension(prefferedSize.width + icon.getIconWidth() + 4, prefferedSize.height));

            Dimension maxSize = button.getMaximumSize();
            arrowButton.setMaximumSize(new Dimension(icon.getIconWidth() + 4, maxSize.height));
            setMaximumSize(new Dimension(maxSize.width + icon.getIconWidth() + 4, maxSize.height));

            Dimension minSize = button.getMinimumSize();
            arrowButton.setMaximumSize(new Dimension(icon.getIconWidth() + 4, minSize.height));
            setMinimumSize(new Dimension(minSize.width + icon.getIconWidth() + 4, minSize.height));

            arrowButton.setSize(arrowButton.getPreferredSize());
            setSize(getPreferredSize());
        }
    }

    private Icon createIcon(){
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int[] x = new int[] { 0, 5, 10, };
        int[] y = new int[] { 3, 8, 3, };
        Polygon polygon = new Polygon(x, y, x.length);
        g.setColor(Color.black);
        g.fill(polygon);

        return new ImageIcon(image);
    }
}
