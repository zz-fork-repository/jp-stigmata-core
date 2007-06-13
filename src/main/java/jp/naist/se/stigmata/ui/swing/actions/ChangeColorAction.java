package jp.naist.se.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.Utility;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class ChangeColorAction extends AbstractAction{
    private static final long serialVersionUID = -7617597154707466764L;

    private Color currentColor = Color.RED;
    private JColorChooser chooser;
    private Component component;
    private boolean colorSelected = false;
    private ActionListener listener;

    public ChangeColorAction(String label, Component component, 
                              Color initialColor, ActionListener listener){
        super(Messages.getString(label + ".label"), Utility.getIcon(label + ".icon"));
        this.component = component;
        this.listener = listener;
        this.currentColor = initialColor;

        chooser = new JColorChooser();
        chooser.setColor(initialColor);
    }

    public ChangeColorAction(Component component, Color color, ActionListener listener){
        this("changecolor", component, color, listener);
    }

    public ChangeColorAction(Component component, ActionListener listener){
        this(component, Color.RED, listener);
    }

    public boolean isColorSelected(){
        return colorSelected;
    }

    public Color getColor(){
        return currentColor;
    }

    public void actionPerformed(ActionEvent e){
        chooser.setColor(currentColor);
        JDialog dialog = JColorChooser.createDialog(
            component, Messages.getString("changecolor.title"), 
            true, chooser,
            new ActionListener(){ // ok
                public void actionPerformed(ActionEvent e){
                    currentColor = chooser.getColor();
                    colorSelected = true;
                    listener.actionPerformed(new ActionEvent(ChangeColorAction.this, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                }
            },
            new ActionListener(){ // cancel
                public void actionPerformed(ActionEvent e){
                    colorSelected = false;
                }
            }
        );
        dialog.setVisible(true);
    }
}
