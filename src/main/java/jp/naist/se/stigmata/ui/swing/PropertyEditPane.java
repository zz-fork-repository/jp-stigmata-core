package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import jp.naist.se.stigmata.BirthmarkEnvironment;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
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
    }

    private void addOrUpdateProperty(String name, String value){
        int index = findIndex(name);
        if(index >= 0){
            model.setValueAt(value, index, 1);
        }
        else{
            model.addRow(new Object[] { name, value, });
        }
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

    private void addNewProperty(){
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel(layout);
        JLabel nameLabel = new JLabel(Messages.getString("propertyname.label"));
        JLabel valueLabel = new JLabel(Messages.getString("propertyvalue.label"));
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

        int val = JOptionPane.showConfirmDialog(
            stigmata, panel, Messages.getString("propertyadd.dialog.title"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if(val == JOptionPane.YES_OPTION){
            model.addRow(new Object[] {
                name.getText().trim(),
                value.getText()
            });
        }
    }

    private void removeSelectedProperty(){
        int[] indexes = table.getSelectedRows();
        for(int i = indexes.length - 1; i >= 0; i--){
            model.removeRow(indexes[i]);
        }
    }

    private void initLayouts(){
        model = new DefaultTableModel();
        model.addColumn(Messages.getString("propertyname.label"));
        model.addColumn(Messages.getString("propertyvalue.label"));
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setColumnSelectionAllowed(false);

        JButton add = Utility.createButton("propertyadd");
        final JButton remove = Utility.createButton("propertyremove");

        setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(table);
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(add);
        box.add(Box.createHorizontalGlue());
        box.add(remove);
        box.add(Box.createHorizontalGlue());

        add(scroll, BorderLayout.CENTER);
        add(box, BorderLayout.SOUTH);

        add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addNewProperty();
            }
        });
        remove.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeSelectedProperty();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent arg0){
                remove.setEnabled(table.getSelectedRowCount() != 0);
            }
        });
        remove.setEnabled(false);
    }
}
