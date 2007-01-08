package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Component;
import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;


/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
class Utility{
    private static final String iconPath = Messages.getString("icon.directory");

    private Utility(){
    }

    public static JButton createButton(String label){
        JButton button = new JButton(Messages.getString(label + ".button.label"));
        button.setActionCommand(label);
        if(Messages.hasString(label + ".button.tooltip")){
            button.setToolTipText(Messages.getString(label + ".button.tooltip"));
        }
        Icon icon = getIcon(label + ".button.icon");
        button.setIcon(icon);

        return button;
    }

    public static JMenuItem createJMenuItem(String label){
        JMenuItem item = new JMenuItem(Messages.getString(label + ".menuitem.label"));
        item.setActionCommand(label);
        if(Messages.hasString(label + ".menuitem.tooltip")){
            item.setToolTipText(Messages.getString(label + ".menuitem.tooltip"));
        }
        item.setIcon(getIcon(label + ".menuitem.icon"));
        return item;
    }

    public static JMenu createJMenu(String label){
        JMenu menu = new JMenu(Messages.getString(label + ".menu.label"));
        menu.setActionCommand(label);
        if(Messages.hasString(label + ".menu.tooltip")){
            menu.setToolTipText(Messages.getString(label + ".menu.tooltip"));
        }
        menu.setIcon(getIcon(label + ".menu.icon"));
        return menu;
    }

    public static Icon getIcon(String label){
        if(Messages.hasString(label)){
            String iconFile = Messages.getString(label);
            ImageIcon icon = new ImageIcon(Utility.class.getResource(iconPath + iconFile));
            return icon;
        }
        return null;
    }

    public static String array2String(String[] values){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < values.length; i++){
            if(i != 0)
                builder.append(", ");
            builder.append(values[i]);
        }
        return new String(builder);
    }

    public static void addNewTab(String key, JTabbedPane tabPane, Component comp){
        addNewTab(key, tabPane, comp, null, null);
    }

    public static void addNewTab(String key, JTabbedPane tabPane, Component comp, Object[] tabnameValues, Object[] values){
        String tabName = Messages.getString(key + ".tab.label");
        String tooltip = Messages.getString(key + ".tab.tooltip");
        Icon icon = getIcon(key + ".tab.icon");
    
        if(tabnameValues != null){
            tabName = MessageFormat.format(tabName, tabnameValues);
        }
        
        if(values != null){
            tooltip = MessageFormat.format(tooltip, values);
        }
        tabPane.addTab(tabName, icon, comp, tooltip);
    }
}
