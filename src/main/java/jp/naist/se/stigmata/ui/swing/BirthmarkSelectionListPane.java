package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkSelectionListPane extends BirthmarkSelectablePane{
    private static final long serialVersionUID = 3209854654743223453L;

    private DefaultListModel model;
    private JList list;

    public BirthmarkSelectionListPane(StigmataFrame stigmata){
        super(stigmata);

        initLayouts();
    }

    public void serviceRemoved(BirthmarkSpi service){
        BirthmarkSelection elem = getSelection(service.getType());
        model.removeElement(elem);
        
        super.serviceRemoved(service);
    }

    private void initLayouts(){
        setLayout(new BorderLayout());
        list = new JList(model = new DefaultListModel());
        JScrollPane scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
        list.setCellRenderer(new Renderer());
        list.setVisibleRowCount(5);
        JButton checkAll = GUIUtility.createButton("checkall");
        JButton uncheckAll = GUIUtility.createButton("uncheckall");

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
                updateUI();
            }
        };
        checkAll.addActionListener(listener);
        uncheckAll.addActionListener(listener);
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(index);
                BirthmarkSelection elem = (BirthmarkSelection)model.getElementAt(index);
                elem.setSelected(!elem.isSelected());
                updateUI();
            }
        });
    }

    /**
     * update layouts and update selected birthmarks list.
     */
    protected void updateLayouts(){
        model.removeAllElements();

        for(Iterator<BirthmarkSelection> i = birthmarkSelections(); i.hasNext(); ){
            BirthmarkSelection elem = i.next();
            if(elem.isVisible(isExpertMode())){
                model.addElement(elem);
            }

            select(elem.getType(), elem.isVisible(isExpertMode()) && elem.isSelected());
        }
        updateUI();
    }

    public static class Renderer extends JCheckBox implements ListCellRenderer{
        private static final long serialVersionUID = -324432943654654L;
        private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public Renderer(){
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean forcus){
            BirthmarkSelection elem = (BirthmarkSelection)value;
            setText(elem.getService().getDisplayType());
            setToolTipText(elem.getService().getDescription());
            setSelected(elem.isSelected());

            if(isSelected){
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else{
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            Border border = null;
            if(forcus) {
                if (isSelected) {
                    border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getBorder("List.focusCellHighlightBorder");
                }
            } else {
                border = noFocusBorder;
            }
            setBorder(border);

            return this;
        }
    };
}
