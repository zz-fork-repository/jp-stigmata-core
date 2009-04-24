package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.ui.swing.CompareTableCellRenderer;
import jp.sourceforge.stigmata.ui.swing.GUIUtility;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class UpdateBirthmarkCellColorAction extends AbstractAction{
    private static final long serialVersionUID = 2390797591047570440L;

    private StigmataFrame parent;
    private BirthmarkEnvironment environment;
    private JColorChooser chooser;

    public UpdateBirthmarkCellColorAction(StigmataFrame parent, BirthmarkEnvironment environment){
        this.parent = parent;
        this.environment = environment;
    }

    public UpdateBirthmarkCellColorAction(StigmataFrame parent){
        this(parent, BirthmarkEnvironment.getDefaultEnvironment());
    }

    public void actionPerformed(ActionEvent e){
        JComponent c = createPanel();
        JOptionPane.showMessageDialog(
            parent, c, parent.getMessages().get("updatecellcolor.dialog.title"),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JComponent createPanel(){
        Box panel = Box.createVerticalBox();
        for(int i = 0; i <= 5; i++){
            Color fore = CompareTableCellRenderer.getForegroundColor(i, environment);
            Color back = CompareTableCellRenderer.getBackgroundColor(i, environment);

            UpdateColorPane pane = new UpdateColorPane(i, fore, back);
            panel.add(pane);
        }

        return panel;
    }

    private Color updateColor(Color c, int rank, boolean foreground){
        if(chooser == null){
            chooser = new JColorChooser();
        }
        chooser.setColor(c);
        String l = parent.getMessages().get((foreground? "forecolor_": "backcolor_") + rank + ".label");
        int returnValue = JOptionPane.showConfirmDialog(
            parent, chooser, parent.getMessages().get("updatecell.title", l),
            JOptionPane.INFORMATION_MESSAGE
        );
        if(returnValue == JOptionPane.OK_OPTION){
            c = chooser.getColor();
        }

        return c;
    }

    private class UpdateColorPane extends JPanel{
        private static final long serialVersionUID = 8271684478406307685L;

        private int rank;
        private JLabel label;

        public UpdateColorPane(int rank, Color fore, Color back){
            this.rank = rank;
            initLayouts();

            label.setForeground(fore);
            label.setBackground(back);
        }

        public int getRank(){
            return rank;
        }

        private void initLayouts(){
            label = new JLabel(parent.getMessages().get("rank_" + rank + ".label"));
            label.setOpaque(true);
            JButton fore = GUIUtility.createButton(parent.getMessages(), "updatecellfore");
            JButton back = GUIUtility.createButton(parent.getMessages(), "updatecellback");

            ActionListener listener = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String command = e.getActionCommand();
                    boolean foreground = command.equals("updatecellfore");

                    Color c = label.getBackground();
                    if(foreground){
                        c = label.getForeground();
                    }
                    c = updateColor(c, getRank(), foreground);
                    if(foreground){
                        label.setForeground(c);
                        environment.addProperty(
                            "forecolor_" + getRank(), String.format("%06x", c.getRGB() & 0xffffff)
                        );
                    }
                    else{
                        label.setBackground(c);
                        environment.addProperty(
                            "backcolor_" + getRank(), String.format("%06x", c.getRGB() & 0xffffff)
                        );
                    }
                }
            };
            fore.addActionListener(listener);
            back.addActionListener(listener);

            setLayout(new GridLayout(1, 3));
            add(label);
            add(fore);
            add(back);
        }
    }
}
