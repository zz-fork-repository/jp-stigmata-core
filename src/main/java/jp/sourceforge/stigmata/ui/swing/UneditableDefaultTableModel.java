package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

/**
 * 
 *
 * @author Haruaki Tamada
 * @version $Revision$
 */
public class UneditableDefaultTableModel extends DefaultTableModel{
    private static final long serialVersionUID = -7483377914997660949L;

    private Map<Integer, Class<?>> columnClasses = new HashMap<Integer, Class<?>>();

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }

    public void setColumnClass(int columnIndex, Class<?> clazz){
        columnClasses.put(columnIndex, clazz);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex){
        Class<?> clazz = columnClasses.get(columnIndex);
        if(clazz == null){
            clazz = super.getColumnClass(columnIndex);
        }
        return clazz;
    }
}
