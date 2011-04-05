package jp.sourceforge.stigmata.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.metal.MetalComboBoxIcon;

/**
 * Popup button.
 * 
 * @author tamada
 */
public class PopupButton extends JPanel{
    private static final long serialVersionUID = -4428839967597028837L;

    private JButton button;
    private JPopupMenu popup;
    private JButton arrowButton;

    public PopupButton(JButton initButton){
        button = initButton;
        arrowButton = new JButton(new MetalComboBoxIcon());
        popup = new JPopupMenu();

        arrowButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Point p = button.getLocation();
                popup.show(PopupButton.this, p.x, button.getHeight());
            }
        });
        Insets insets = arrowButton.getMargin();
        arrowButton.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0d; gbc.weighty = 0d;
        gbc.gridx = 0;    gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;

        add(button, gbc);

        gbc.weightx = 0d;
        gbc.gridx = 1;
        add(arrowButton, gbc);

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
}
