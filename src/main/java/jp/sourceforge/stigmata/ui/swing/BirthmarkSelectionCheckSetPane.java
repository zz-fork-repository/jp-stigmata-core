package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class BirthmarkSelectionCheckSetPane extends BirthmarkSelectablePane{
    private static final long serialVersionUID = 3209854654743223453L;

    private JPanel checks = new JPanel();

    public BirthmarkSelectionCheckSetPane(StigmataFrame stigmata){
        super(stigmata);

        initLayouts();
    }

    private void initLayouts(){
        setLayout(new BorderLayout());
        add(checks, BorderLayout.CENTER);

        JButton checkAll = GUIUtility.createButton(getMessages(), "checkall");
        JButton uncheckAll = GUIUtility.createButton(getMessages(), "uncheckall");

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(checkAll);
        box.add(Box.createHorizontalGlue());        
        box.add(uncheckAll);
        box.add(Box.createHorizontalGlue());
        add(box, BorderLayout.SOUTH);

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                boolean flag = e.getActionCommand().equals("checkall");
                for(Iterator<BirthmarkSelection> i = birthmarkSelections(); i.hasNext(); ){
                    BirthmarkSelection le = i.next();
                    le.setSelected(flag);
                    fireEvent();
                }
                updateLayouts();
            }
        };
        checkAll.addActionListener(listener);
        uncheckAll.addActionListener(listener);
    }

    /**
     * update layouts and update selected birthmarks list.
     */
    protected void updateLayouts(){
        checks.removeAll();
        Dimension d = calculateDimension();
        checks.setLayout(new GridLayout(d.height, d.width));

        for(Iterator<BirthmarkSelection> i = birthmarkSelections(); i.hasNext(); ){
            final BirthmarkSelection elem = i.next();
            if(elem.isVisible(isExpertMode())){
                JCheckBox check = new JCheckBox(elem.getService().getDisplayType());
                check.setSelected(elem.isSelected());
                check.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JCheckBox c = (JCheckBox)e.getSource();
                        elem.setSelected(c.isSelected());
                        select(elem.getService().getType(), c.isSelected());
                        fireEvent();
                    }
                });
                checks.add(check);
            }

            select(elem.getType(), elem.isVisible(isExpertMode()) && elem.isSelected());
        }
        updateUI();
    }

    private Dimension calculateDimension(){
        int rows = 1;
        int cols = 0;
        for(Iterator<BirthmarkSelection> i = birthmarkSelections(); i.hasNext(); ){
            BirthmarkSelection selection = i.next();
            if(selection.isVisible(isExpertMode())){
                cols++;
            }
        }

        if(cols > 4){
            rows = (cols / 3);
            if((cols % 3) != 0) rows++;
            cols = 3;
        }

        return new Dimension(cols, rows);
    }
}
