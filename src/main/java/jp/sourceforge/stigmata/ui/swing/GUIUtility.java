package jp.sourceforge.stigmata.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jp.sourceforge.talisman.i18n.Messages;

/**
 * Utility routines for building GUI.
 *
 * @author Haruaki TAMADA
 */
public class GUIUtility{
    private GUIUtility(){
    }

    public static void decorateJComponent(Messages messages, JComponent component, String label){
        if(messages.hasValue(label + ".tooltip")){
            component.setToolTipText(messages.get(label + ".tooltip"));
        }
        if(messages.hasValue(label + ".border")){
            component.setBorder(new TitledBorder(messages.get(label + ".border")));
        }
        try{
            Icon icon = getIcon(messages, label + ".icon");
            if(icon != null){
                Method[] methods = component.getClass().getMethods();
                for(Method m: methods){
                    if(m.getName().equals("setIcon")){
                        m.invoke(component, icon);
                    }
                }
            }
        } catch(IllegalAccessException e){
            throw new InternalError(e.getMessage());
        } catch(InvocationTargetException e){
            throw new InternalError(e.getMessage());
        }
    }

    public static JButton createButton(Messages messages, String label, Action action){
        JButton button = new JButton(action);
        button.setText(messages.get(label + ".button.label"));
        button.setActionCommand(label);
        if(messages.hasValue(label + ".button.tooltip")){
            button.setToolTipText(messages.get(label + ".button.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".button.icon");
        if(icon != null){
            button.setIcon(icon);
        }

        return button;
    }

    public static JButton createButton(Messages messages, String label){
        JButton button = new JButton(messages.get(label + ".button.label"));
        button.setActionCommand(label);
        if(messages.hasValue(label + ".button.tooltip")){
            button.setToolTipText(messages.get(label + ".button.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".button.icon");
        if(icon != null){
            button.setIcon(icon);
        }

        return button;
    }

    public static JCheckBoxMenuItem createJCheckBoxMenuItem(Messages messages, String label){
        return createJCheckBoxMenuItem(messages, label, false);
    }

    public static JCheckBoxMenuItem createJCheckBoxMenuItem(Messages messages, String label, boolean status){
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(messages.get(label + ".menuitem.label"), status);
        item.setActionCommand(label);
        if(messages.hasValue(label + ".menuitem.tooltip")){
            item.setToolTipText(messages.get(label + ".menuitem.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".menuitem.icon");
        if(icon != null){
            item.setIcon(icon);
        }
        return item;
    }

    public static JMenuItem createJMenuItem(Messages messages, String label, Action action){
        JMenuItem item = new JMenuItem(action);
        item.setText(messages.get(label + ".menuitem.label"));
        item.setActionCommand(label);
        if(messages.hasValue(label + ".menuitem.tooltip")){
            item.setToolTipText(messages.get(label + ".menuitem.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".menuitem.icon");
        if(icon != null){
            item.setIcon(icon);
        }
        return item;
    }

    public static JMenuItem createJMenuItem(Messages messages, String label){
        JMenuItem item = new JMenuItem(messages.get(label + ".menuitem.label"));
        item.setActionCommand(label);
        if(messages.hasValue(label + ".menuitem.tooltip")){
            item.setToolTipText(messages.get(label + ".menuitem.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".menuitem.icon");
        if(icon != null){
            item.setIcon(icon);
        }
        return item;
    }

    public static JMenu createJMenu(Messages messages, String label){
        JMenu menu = new JMenu(messages.get(label + ".menu.label"));
        menu.setActionCommand(label);
        if(messages.hasValue(label + ".menu.tooltip")){
            menu.setToolTipText(messages.get(label + ".menu.tooltip"));
        }
        Icon icon = getIcon(messages, label + ".menu.icon");
        if(icon != null){
            menu.setIcon(icon);
        }
        return menu;
    }

    public static URL getResource(Messages messages, String resourcePathLabel, String resourcePathPrefix){
        if(messages.hasValue(resourcePathLabel)){
            String resourcePath = messages.get(resourcePathLabel);
            if(resourcePathPrefix != null){
                resourcePath = resourcePathPrefix + resourcePath;
            }
            return GUIUtility.class.getResource(resourcePath);
        }
        return null;
    }

    public static URL getResource(Messages messages, String resourcePathLabel){
        return getResource(messages, resourcePathLabel, null);
    }

    public static Icon getIcon(Messages messages, String label){
        URL url = getResource(messages, label, messages.get("icon.path"));
        if(url != null){
            ImageIcon icon = new ImageIcon(url);
            return icon;
        }

        return null;
    }

    public static Image getImage(Messages messages, String imageFilePathLabel){
        Icon icon = getIcon(messages, imageFilePathLabel);
        if(icon != null && icon instanceof ImageIcon){
            return ((ImageIcon)icon).getImage();
        }
        return null;
    }

    public static void addNewTab(Messages messages, String key, JTabbedPane tabPane, Component comp){
        addNewTab(messages, key, tabPane, comp, null, null);
    }

    public static void addNewTab(Messages messages, String key, JTabbedPane tabPane, Component comp, Object[] tabnameValues, Object[] values){
        addNewTab(messages, key, tabPane, comp, tabnameValues, values, false);
    }

    public static void addNewTab(Messages messages, String key, final JTabbedPane tabPane,
                                 final Component comp, Object[] tabnameValues, Object[] values,
                                 boolean closable){
        String tabName = messages.get(key + ".tab.label");
        String tooltip = messages.get(key + ".tab.tooltip");
        Icon icon = getIcon(messages, key + ".tab.icon");

        if(tabnameValues != null){
            tabName = MessageFormat.format(tabName, tabnameValues);
        }

        if(values != null){
            tooltip = MessageFormat.format(tooltip, values);
        }
        tabPane.addTab(tabName, icon, comp, tooltip);
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
        if(closable){
            JLabel label = new JLabel(tabName);
            if(icon != null){
                label.setIcon(icon);
            }
            Icon closeIcon = getIcon(messages, "closetab.tabicon");
            JButton button = new JButton(closeIcon);
            button.setPreferredSize(new Dimension(closeIcon.getIconWidth(), closeIcon.getIconHeight()));
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            label.setBorder(new EmptyBorder(0, 0, 0, 4));
            panel.add(label, BorderLayout.WEST);
            panel.add(button, BorderLayout.EAST);
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    tabPane.remove(comp);
                }
            });
            button.setBorder(new EmptyBorder(1, 1, 1, 1));
            panel.setBorder(new EmptyBorder(2, 1, 1, 1));
            tabPane.setTabComponentAt(tabPane.getTabCount() - 1, panel);
        }
    }

    public static void showErrorDialog(Component parent, Messages messages, Exception e){
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();

        panel.add(new JLabel(e.getLocalizedMessage()), BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        area.setText(stackTrace);
        area.setEditable(false);
        panel.setPreferredSize(new Dimension(500, 400));
        panel.setSize(panel.getPreferredSize());

        JOptionPane.showMessageDialog(parent, panel, messages.get("error.dialog.title"), JOptionPane.WARNING_MESSAGE);
    }
}
