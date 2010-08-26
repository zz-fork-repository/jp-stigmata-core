package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import jp.sourceforge.stigmata.ui.swing.GUIUtility;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class ChangeColorAction extends AbstractAction{
    private static final long serialVersionUID = -7617597154707466764L;

    private StigmataFrame frame;
    private Color currentColor = Color.RED;
    private JColorChooser chooser;
    private boolean colorSelected = false;
    private ActionListener listener;

    public ChangeColorAction(String label, StigmataFrame frame, 
                              Color initialColor, ActionListener listener){
        super(frame.getMessages().get(label + ".label"), GUIUtility.getIcon(frame.getMessages(), label + ".icon"));
        this.frame = frame;
        this.listener = listener;
        this.currentColor = initialColor;

        chooser = new JColorChooser();
        chooser.setColor(initialColor);
    }

    public ChangeColorAction(StigmataFrame frame, Color color, ActionListener listener){
        this("changecolor", frame, color, listener);
    }

    public ChangeColorAction(StigmataFrame frame, ActionListener listener){
        this(frame, Color.RED, listener);
    }

    public boolean isColorSelected(){
        return colorSelected;
    }

    public Color getColor(){
        return currentColor;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        chooser.setColor(currentColor);
        JDialog dialog = JColorChooser.createDialog(
            frame, frame.getMessages().get("changecolor.title"), 
            true, chooser,
            new ActionListener(){ // ok
                @Override
                public void actionPerformed(ActionEvent e){
                    currentColor = chooser.getColor();
                    colorSelected = true;
                    listener.actionPerformed(new ActionEvent(ChangeColorAction.this, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                }
            },
            new ActionListener(){ // cancel
                @Override
                public void actionPerformed(ActionEvent e){
                    colorSelected = false;
                }
            }
        );
        dialog.setVisible(true);
    }
}
