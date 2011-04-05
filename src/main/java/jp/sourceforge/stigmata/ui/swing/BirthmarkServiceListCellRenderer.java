package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkServiceListCellRenderer extends JPanel implements ListCellRenderer{
    private static final long serialVersionUID = 3254763527508235L;
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private final JLabel leftLabel  = new JLabel();
    private final JLabel rightLabel = new JLabel();

    public BirthmarkServiceListCellRenderer(Dimension dim, int rightw){
        super(new BorderLayout());
        leftLabel.setOpaque(true);
        rightLabel.setOpaque(true);
        this.setOpaque(true);
        leftLabel.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        rightLabel.setPreferredSize(new Dimension(rightw, 0));
        rightLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        this.add(leftLabel, BorderLayout.CENTER);
        this.add(rightLabel, BorderLayout.EAST);
        this.setPreferredSize(dim);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus){
        if(value instanceof BirthmarkSpi){
            BirthmarkSpi service = (BirthmarkSpi)value;
            leftLabel.setText(service.getDisplayType());
            rightLabel.setText("(" + service.getType() + ")");
        }
        else{
            leftLabel.setText(String.valueOf(value));
            rightLabel.setText("");
        }
        if(isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            leftLabel.setBackground(list.getSelectionBackground());
            leftLabel.setForeground(list.getSelectionForeground());
            rightLabel.setBackground(list.getSelectionBackground());
            rightLabel.setForeground(Color.gray.brighter());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            leftLabel.setBackground(list.getBackground());
            leftLabel.setForeground(list.getForeground());
            rightLabel.setBackground(list.getBackground());
            rightLabel.setForeground(Color.gray);
        }
        Border border = null;
        if(hasFocus) {
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
}