package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class Utility{
    private static final String iconPath = Messages.getString("icon.directory");

    private Utility(){
    }

    public static Action createAction(String key, final ActionListener listener){
        final Icon icon = getIcon(key + ".icon");
        final String label = Messages.getString(key + ".label");

        return new AbstractAction(label, icon){
            private static final long serialVersionUID = 1349393156253158607L;

            public void actionPerformed(ActionEvent e){
                listener.actionPerformed(e);
            }
        };
    }

    public static void decorateJComponent(JComponent component, String label){
        if(Messages.hasString(label + ".tooltip")){
            component.setToolTipText(Messages.getString(label + ".tooltip"));
        }
        if(Messages.hasString(label + ".border")){
            component.setBorder(new TitledBorder(Messages.getString(label + ".border")));
        }
        try{
            if(component.getClass().getMethod("setIcon") != null){
                Icon icon = getIcon(label + ".icon");
                if(icon != null){
                    component.getClass().getMethod("setIcon").invoke(component, icon);
                }
            }
        } catch(NoSuchMethodException e){
        } catch(IllegalAccessException e){
        } catch(InvocationTargetException e){
        }
    }

    public static JButton createButton(String label, Action action){
        JButton button = new JButton(action);
        button.setActionCommand(label);
        if(Messages.hasString(label + ".button.tooltip")){
            button.setToolTipText(Messages.getString(label + ".button.tooltip"));
        }
        return button;
    }

    public static JButton createButton(String label){
        JButton button = new JButton(Messages.getString(label + ".button.label"));
        button.setActionCommand(label);
        if(Messages.hasString(label + ".button.tooltip")){
            button.setToolTipText(Messages.getString(label + ".button.tooltip"));
        }
        Icon icon = getIcon(label + ".button.icon");
        if(icon != null){
            button.setIcon(icon);
        }

        return button;
    }

    public static JCheckBoxMenuItem createJCheckBoxMenuItem(String label){
        return createJCheckBoxMenuItem(label, false);
    }

    public static JCheckBoxMenuItem createJCheckBoxMenuItem(String label, boolean status){
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(Messages.getString(label + ".menuitem.label"), status);
        item.setActionCommand(label);
        if(Messages.hasString(label + ".menuitem.tooltip")){
            item.setToolTipText(Messages.getString(label + ".menuitem.tooltip"));
        }
        Icon icon = getIcon(label + ".menuitem.icon");
        if(icon != null){
            item.setIcon(icon);
        }
        return item;
    }

    public static JMenuItem createJMenuItem(String label, Action action){
        JMenuItem item = new JMenuItem(action);
        item.setActionCommand(label);
        if(Messages.hasString(label + ".menuitem.tooltip")){
            item.setToolTipText(Messages.getString(label + ".menuitem.tooltip"));
        }
        return item;
    }

    public static JMenuItem createJMenuItem(String label){
        JMenuItem item = new JMenuItem(Messages.getString(label + ".menuitem.label"));
        item.setActionCommand(label);
        if(Messages.hasString(label + ".menuitem.tooltip")){
            item.setToolTipText(Messages.getString(label + ".menuitem.tooltip"));
        }
        Icon icon = getIcon(label + ".menuitem.icon");
        if(icon != null){
            item.setIcon(icon);
        }
        return item;
    }

    public static JMenu createJMenu(String label){
        JMenu menu = new JMenu(Messages.getString(label + ".menu.label"));
        menu.setActionCommand(label);
        if(Messages.hasString(label + ".menu.tooltip")){
            menu.setToolTipText(Messages.getString(label + ".menu.tooltip"));
        }
        Icon icon = getIcon(label + ".menu.icon");
        if(icon != null){
            menu.setIcon(icon);
        }
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
