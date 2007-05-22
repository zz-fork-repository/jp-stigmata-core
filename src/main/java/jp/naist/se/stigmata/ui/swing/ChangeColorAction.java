package jp.naist.se.stigmata.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

public class ChangeColorAction extends AbstractAction{
    private static final long serialVersionUID = -7617597154707466764L;

    private Color color = Color.RED;
    private JColorChooser chooser;
    private Component component;
    private boolean colorSelected = false;
    private ActionListener listener;

    public ChangeColorAction(Component component, Color color, ActionListener listener){
        super(Messages.getString("changecolor.button.label"),
                Utility.getIcon("changecolor.button.icon"));
        this.component = component;
        this.listener = listener;
        this.color = color;

        chooser = new JColorChooser();
        chooser.setColor(color);
    }

    public ChangeColorAction(Component component, ActionListener listener){
        this(component, Color.RED, listener);
    }

    public boolean isColorSelected(){
        return colorSelected;
    }

    public Color getColor(){
        return color;
    }

    public void actionPerformed(ActionEvent e){
        chooser.setColor(color);
        JDialog dialog = JColorChooser.createDialog(
            component, Messages.getString("changecolor.title"), 
            true, chooser,
            new ActionListener(){ // ok
                public void actionPerformed(ActionEvent e){
                    color = chooser.getColor();
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
