package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.metal.MetalComboBoxIcon;

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
        icon = new MetalComboBoxIcon();
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
}
