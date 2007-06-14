package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$ 
 */

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import jp.naist.se.stigmata.BirthmarkContext;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CompareTableCellRenderer extends DefaultTableCellRenderer{
    private static final long serialVersionUID = 234557758658567345L;
    private static final double EPSILON = 1E-8d;

    private BirthmarkContext context;

    public CompareTableCellRenderer(BirthmarkContext context){
        this.context = context;
    }

    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected,
                                                    boolean hasForcus, int row, int col){
        Object value = table.getValueAt(row, col);
        Component c = super.getTableCellRendererComponent(
            table, obj, isSelected, hasForcus, row, col
        );
        if(value instanceof Double && !isSelected){
            double d = ((Double)value).doubleValue();
            int rank = 0;
            if(d < EPSILON)       rank = 0;
            else if((d - 0.2d) < EPSILON) rank = 1;
            else if((d - 0.4d) < EPSILON) rank = 2;
            else if((d - 0.6d) < EPSILON) rank = 3;
            else if((d - 0.8d) < EPSILON) rank = 4;
            else if((d - 1.0d) < EPSILON) rank = 5;

            c.setBackground(getBackgroundColor(rank, context));
            c.setForeground(getForegroundColor(rank, context));
        }

        return c;
    }

    public static Color getDefaultForegroundColor(int rank){
        int c = 180 - ((180 / 5) * rank);
        return new Color(c, c, c);
    }

    public static Color getDefaultBackgroundColor(int rank){
        Color c;
        switch(rank){
        case 5:
            c = Color.RED;
            break;
        case 0: case 1: case 2: case 3: case 4:
        default:
            c = Color.WHITE;
            break;
        }
        return c;
    }

    public static Color getBackgroundColor(int rank, BirthmarkContext context){
        Color c = getColor("backcolor_" + rank, context);
        if(c == null){
            return getDefaultBackgroundColor(rank);
        }
        return c;
    }

    public static Color getForegroundColor(int rank, BirthmarkContext context){
        Color c = getColor("forecolor_" + rank, context);
        if(c == null){
            c = getDefaultForegroundColor(rank);
        }
        return c;
    }

    private static Color getColor(String key, BirthmarkContext context){
        String v = context.getProperty(key);
        try{
            int color = Integer.parseInt(v, 16);

            return new Color(
                (color >>> 16) & 0xff, (color >>> 8) & 0xff, color & 0xff
            );
        } catch(NumberFormatException e){
            return null;
        }
    }
}