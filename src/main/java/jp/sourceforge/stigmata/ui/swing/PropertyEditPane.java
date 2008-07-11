package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.ui.swing.actions.PopupShowAction;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public class PropertyEditPane extends JPanel{
    private static final long serialVersionUID = 12397342543653L;

    private StigmataFrame stigmata;
    private JTable table;
    private DefaultTableModel model;

    public PropertyEditPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initData();
    }

    public void updateEnvironment(BirthmarkEnvironment environment){
        environment.clearProperties();
        for(int i = 0; i < model.getRowCount(); i++){
            environment.addProperty(
                (String)model.getValueAt(i, 0),
                (String)model.getValueAt(i, 1)
            );
        }
    }

    private void initData(){
        BirthmarkEnvironment environment = stigmata.getEnvironment();
        environment.addPropertyListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt){
                String name = evt.getPropertyName();
                String value = (String)evt.getNewValue();
                if(value == null){
                    removeProperty(name);
                }
                else{
                    addOrUpdateProperty(name, value);
                }
            }
        });
        for(Iterator<String> i = environment.propertyKeys(); i.hasNext(); ){
            String key = i.next();
            model.addRow(new Object[] { key, environment.getProperty(key), });
        }
    }

    private void removeProperty(String name){
        int index = findIndex(name);
        if(index >= 0){
            model.removeRow(index);
        }
        stigmata.setNeedToSaveSettings(true);
    }

    private void addOrUpdateProperty(String name, String value){
        int index = findIndex(name);
        if(index >= 0){
            model.setValueAt(value, index, 1);
        }
        else{
            model.addRow(new Object[] { name, value, });
        }
        stigmata.setNeedToSaveSettings(true);
    }

    private int findIndex(String name){
        for(int i = 0; i < model.getRowCount(); i++){
            String v = (String)model.getValueAt(i, 0);
            if(v.equals(name)){
                return i;
            }
        }
        return -1;
    }

    private void addNewProperty(int index){
        final Messages messages = stigmata.getMessages();
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel(layout);
        JLabel nameLabel = new JLabel(messages.get("propertyname.label"));
        JLabel valueLabel = new JLabel(messages.get("propertyvalue.label"));
        JTextField name = new JTextField(15);
        JTextField value = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridwidth = 1;
        gbc.gridy = 0; gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 5, 0);
        layout.setConstraints(nameLabel, gbc);
        panel.add(nameLabel);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.gridy = 0; gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(name, gbc);
        panel.add(name);

        gbc.gridx = 0; gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridheight = 1;
        layout.setConstraints(valueLabel, gbc);
        panel.add(valueLabel);

        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.gridy = 1; gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(value, gbc);
        panel.add(value);

        if(index >= 0){
            String keyValue = String.valueOf(table.getValueAt(index, 0));
            String valueValue = String.valueOf(table.getValueAt(index, 1));
            if(keyValue != null)   name.setText(keyValue);
            if(valueValue != null) value.setText(valueValue);
        }

        int val = JOptionPane.showConfirmDialog(
            stigmata, panel, messages.get("propertyadd.dialog.title"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if(val == JOptionPane.YES_OPTION){
            if(index >= 0){
                model.setValueAt(name.getText(), index, 0);
                model.setValueAt(value.getText(), index, 1);
            }
            else{
                model.addRow(new Object[] {
                    name.getText().trim(),
                    value.getText()
                });
            }
            stigmata.setNeedToSaveSettings(true);
        }
    }

    private void removeSelectedProperty(){
        int[] indexes = table.getSelectedRows();
        for(int i = indexes.length - 1; i >= 0; i--){
            model.removeRow(indexes[i]);
        }
        stigmata.setNeedToSaveSettings(true);
    }

    private void initLayouts(){
        final Messages messages = stigmata.getMessages();
        model = new DefaultTableModel();
        model.addColumn(messages.get("propertyname.label"));
        model.addColumn(messages.get("propertyvalue.label"));
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setColumnSelectionAllowed(false);

        Action addAction = new AbstractAction(){
            private static final long serialVersionUID = 1283676936119122278L;

            public void actionPerformed(ActionEvent e){
                addNewProperty(-1);
            }
        };
        final Action removeAction = new AbstractAction(){
            private static final long serialVersionUID = -411260949451039374L;

            public void actionPerformed(ActionEvent e){
                removeSelectedProperty();
            }
        };
        final Action changeAction = new AbstractAction(){
            private static final long serialVersionUID = -7406073660916286349L;

            public void actionPerformed(ActionEvent e){
                addNewProperty(table.getSelectedRow());
            }
        };
        JButton addButton = GUIUtility.createButton(messages, "propertyadd", addAction);
        JButton changeButton = GUIUtility.createButton(messages, "propertychange", changeAction);
        JButton removeButton = GUIUtility.createButton(messages, "propertyremove", removeAction);

        final JPopupMenu popup = new JPopupMenu();
        popup.add(GUIUtility.createJMenuItem(messages, "propertyadd", addAction));
        popup.add(GUIUtility.createJMenuItem(messages, "propertychange", changeAction));
        popup.add(GUIUtility.createJMenuItem(messages, "propertyremove", removeAction));

        setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(table);
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(addButton);
        box.add(Box.createHorizontalGlue());
        box.add(changeButton);
        box.add(Box.createHorizontalGlue());
        box.add(removeButton);
        box.add(Box.createHorizontalGlue());

        add(scroll, BorderLayout.CENTER);
        add(box, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent arg0){
                removeAction.setEnabled(table.getSelectedRowCount() != 0);
                changeAction.setEnabled(table.getSelectedRowCount() == 1);
            }
        });
        table.addMouseListener(new PopupShowAction(popup));
        changeAction.setEnabled(false);
        removeAction.setEnabled(false);
    }
}
