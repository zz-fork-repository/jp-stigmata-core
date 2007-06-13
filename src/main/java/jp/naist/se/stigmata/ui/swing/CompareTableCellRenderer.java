package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$ 
 */

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CompareTableCellRenderer extends DefaultTableCellRenderer{
    private static final long serialVersionUID = 234557758658567345L;

    public Component getTableCellRendererComponent(JTable table, Object obj,
            boolean isSelected, boolean hasForcus, int row, int cols){
        Object value = table.getValueAt(row, cols);
        Component c = super.getTableCellRendererComponent(table, obj, isSelected, hasForcus,
                row, cols);
        if(value instanceof Double && !isSelected){
            double d = ((Double)value).doubleValue();
            if(Math.abs(d - 1) < 1E-8){
                c.setBackground(Color.red);
            }
            else{
                c.setBackground(Color.white);
            }
            float ratio = Math.round(d * 10) / 10f;
            int color = Math.round(255 - (200 * ratio));
            c.setForeground(new Color(color, color, color));
        }

        return c;
    }
}