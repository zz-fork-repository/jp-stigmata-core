package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import jp.naist.se.stigmata.utils.WellknownClassManager;
import jp.naist.se.stigmata.utils.WellknownClassSection;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class WellknownClassesSettingsPane extends JPanel{
    private static final long serialVersionUID = 329734546345634532L;

    private JTable sectionTable = null;

    private DefaultTableModel model;

    private WellknownClassManager manager;

    private JComboBox checkPartType;

    private JComboBox matchType;

    private String matchTypeColumnIdentifier;

    private String checkPartColumnIdentifier;

    private String patternColumnIdentifier;

    public WellknownClassesSettingsPane(WellknownClassManager manager){
        super(new BorderLayout());
        this.manager = manager;

        initialize();
        initializeData();
    }

    public synchronized void setWellknownClasses(WellknownClassManager manager){
        manager.clear();
        for(int i = 0; i < model.getRowCount(); i++){
            int partType = getPartType(model.getValueAt(i, 0));
            int match = getMatchType(model.getValueAt(i, 1));
            String value = (String)model.getValueAt(i, 2);
            WellknownClassSection sect = new WellknownClassSection(value, partType | match);
            manager.add(sect);
        }
    }

    public synchronized WellknownClassManager createWellknownClassManager(){
        WellknownClassManager manager = new WellknownClassManager();
        setWellknownClasses(manager);
        return manager;
    }

    public void reset(){
        for(int i = model.getRowCount() - 1; i >= 0; i--){
            model.removeRow(i);
        }
        initializeData();
    }

    private synchronized boolean isWellknownClass(String className){
        WellknownClassManager manager = createWellknownClassManager();

        return manager.isWellKnownClass(className);
    }

    private int getPartType(Object object){
        if(object instanceof Integer){
            return ((Integer)object).intValue();
        }
        else{
            if(object.equals(Messages.getString("fully.label"))){
                return WellknownClassSection.FULLY_TYPE;
            }
            else if(object.equals(Messages.getString("package.label"))){
                return WellknownClassSection.PACKAGE_TYPE;
            }
            else if(object.equals(Messages.getString("classname.label"))){
                return WellknownClassSection.CLASS_NAME_TYPE;
            }
            else if(object.equals(Messages.getString("exclude.label"))){
                return WellknownClassSection.EXCLUDE_TYPE;
            }
        }
        return WellknownClassSection.FULLY_TYPE;
    }

    private int getMatchType(Object object){
        if(object instanceof Integer){
            return ((Integer)object).intValue();
        }
        else{
            if(object.equals(Messages.getString("prefix.label"))){
                return WellknownClassSection.PREFIX_TYPE;
            }
            else if(object.equals(Messages.getString("suffix.label"))){            
                return WellknownClassSection.SUFFIX_TYPE;
            }
            else if(object.equals(Messages.getString("exactmatch.label"))){
                return WellknownClassSection.MATCH_TYPE;
            }
        }
        return WellknownClassSection.PREFIX_TYPE;
    }

    public void addSection(){
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JTextField text = new JTextField();
        inputPanel.setOpaque(true);
        inputPanel.add(new JLabel(matchTypeColumnIdentifier));
        inputPanel.add(matchType);
        inputPanel.add(new JLabel(checkPartColumnIdentifier));
        inputPanel.add(checkPartType);
        inputPanel.add(new JLabel(patternColumnIdentifier));
        inputPanel.add(text);

        int value = JOptionPane.showConfirmDialog(this, inputPanel, Messages
                .getString("addwellknown.dialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if(value == JOptionPane.OK_OPTION){
            int part = getPartType(checkPartType.getSelectedItem());
            int match = getMatchType(matchType.getSelectedItem());
            String pattern = text.getText();

            model.addRow(new Object[] { new Integer(part), new Integer(match), pattern, });
        }
        adjustColumnPreferredWidths(sectionTable);
    }

    private void initializeData(){
        WellknownClassSection[] sections = manager.getSections();
        for(WellknownClassSection section : sections){
            model.addRow(new Object[] { new Integer(section.getMatchPartType()),
                    new Integer(section.getMatchType()), section.getName() });
        }
        adjustColumnPreferredWidths(sectionTable);
    }

    /**
     * copy from Swing Hacks.
     */
    private void adjustColumnPreferredWidths(JTable table){
        TableColumnModel columnModel = table.getColumnModel();
        for(int col = 0; col < table.getColumnCount(); col++){
            int maxWidth = 0;
            for(int row = 0; row < table.getRowCount(); row++){
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Object value = table.getValueAt(row, col);
                Component component = renderer.getTableCellRendererComponent(table, value, false,
                        false, row, col);
                maxWidth = (int)Math.max(component.getPreferredSize().getWidth(), maxWidth);
            }
            TableColumn column = columnModel.getColumn(col);
            column.setPreferredWidth(maxWidth);
        }
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(){
        JPanel center = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane();

        scroll.setViewportView(getSectionTable());
        center.add(scroll, BorderLayout.CENTER);
        center.add(getSouthPanel(), BorderLayout.SOUTH);
        center.setBorder(new TitledBorder(Messages.getString("rules.border")));

        add(center, BorderLayout.CENTER);
        add(getCheckPanel(), BorderLayout.SOUTH);
    }

    private JComponent getCheckPanel(){
        final JTextField text = new JTextField();
        final JButton checkButton = Utility.createButton("checkwellknown");
        final JLabel label = new JLabel(Utility.getIcon("wellknownclasschecker.default.icon"));
        checkButton.setEnabled(false);

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String t = text.getText().trim();
                if(t.length() > 0){
                    if(isWellknownClass(t)){
                        label.setIcon(Utility.getIcon("wellknownclasschecker.wellknown.icon"));
                        label.setToolTipText(Messages.getString("wellknownclasschecker.wellknown.tooltip"));
                    }
                    else{
                        label.setIcon(Utility.getIcon("wellknownclasschecker.notwellknown.icon"));
                        label.setToolTipText(Messages.getString("wellknownclasschecker.notwellknown.tooltip"));
                    }
                }
            }
        };
        checkButton.addActionListener(listener);
        text.addActionListener(listener);
        text.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent arg0){
                checkButton.setEnabled(text.getText().trim().length() > 0);
            }

            public void insertUpdate(DocumentEvent arg0){
                checkButton.setEnabled(text.getText().trim().length() > 0);
            }

            public void removeUpdate(DocumentEvent arg0){
                checkButton.setEnabled(text.getText().trim().length() > 0);
            }
        });

        JComponent south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(text);
        south.add(Box.createHorizontalGlue());
        south.add(checkButton);
        south.add(Box.createHorizontalGlue());
        south.add(label);
        south.add(Box.createHorizontalGlue());

        south.setBorder(new TitledBorder(Messages.getString("wellknownclasschecker.border")));

        return south;
    }

    private JPanel getSouthPanel(){
        JPanel southPanel = new JPanel();
        JButton addButton = Utility.createButton("addwellknown");
        final JButton removeButton = Utility.createButton("removewellknown");
        removeButton.setEnabled(false);

        southPanel.add(addButton);
        southPanel.add(removeButton);

        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addSection();
            }
        });

        removeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int[] rows = sectionTable.getSelectedRows();
                for(int i = rows.length - 1; i >= 0; i--){
                    model.removeRow(rows[i]);
                }
                adjustColumnPreferredWidths(sectionTable);
            }
        });

        sectionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent arg0){
                removeButton.setEnabled(sectionTable.getSelectedRowCount() > 0);
            }
        });

        return southPanel;
    }

    /**
     * This method initializes sectionTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getSectionTable(){
        if(sectionTable == null){
            model = new DefaultTableModel(0, 3);
            matchTypeColumnIdentifier = Messages.getString("matchtype.table.column");
            checkPartColumnIdentifier = Messages.getString("checkpart.table.column");
            patternColumnIdentifier = Messages.getString("pattern.table.column");
            model.setColumnIdentifiers(new Object[] { checkPartColumnIdentifier,
                    matchTypeColumnIdentifier, patternColumnIdentifier, });
            sectionTable = new JTable(model);

            checkPartType = new JComboBox();
            final String[] checkPartItems = Messages.getStringArray("checkpart.items");
            for(int i = 0; i < checkPartItems.length; i++){
                checkPartType.addItem(checkPartItems[i]);
            }
            TableColumn column1 = sectionTable.getColumn(checkPartColumnIdentifier);
            column1.setCellEditor(new DefaultCellEditor(checkPartType));
            column1.setCellRenderer(new DefaultTableCellRenderer(){
                private static final long serialVersionUID = 923743563L;

                public void setValue(Object value){
                    if(value instanceof Integer){
                        int type = ((Integer)value).intValue();
                        if(type == WellknownClassSection.FULLY_TYPE){
                            setText(checkPartItems[0]);
                        }
                        else if(type == WellknownClassSection.PACKAGE_TYPE){
                            setText(checkPartItems[1]);
                        }
                        else if(type == WellknownClassSection.CLASS_NAME_TYPE){
                            setText(checkPartItems[2]);
                        }
                        else if(type == WellknownClassSection.EXCLUDE_TYPE){
                            setText(checkPartItems[3]);
                        }
                        else{
                            setText(value.toString());
                        }
                    }
                    else{
                        setText(value.toString());
                    }
                }
            });

            matchType = new JComboBox();
            final String[] matchTypeStrings = Messages.getStringArray("matchtype.items");
            for(int i = 0; i < matchTypeStrings.length; i++){
                matchType.addItem(matchTypeStrings[i]);
            }
            TableColumn column2 = sectionTable.getColumn(matchTypeColumnIdentifier);
            column2.setCellEditor(new DefaultCellEditor(matchType));
            column2.setCellRenderer(new DefaultTableCellRenderer(){
                private static final long serialVersionUID = 972356346L;

                public void setValue(Object value){
                    if(value instanceof Integer){
                        int type = ((Integer)value).intValue();
                        if(type == WellknownClassSection.PREFIX_TYPE){
                            setText(matchTypeStrings[0]);
                        }
                        else if(type == WellknownClassSection.SUFFIX_TYPE){
                            setText(matchTypeStrings[1]);
                        }
                        else if(type == WellknownClassSection.MATCH_TYPE){
                            setText(matchTypeStrings[2]);
                        }
                        else{
                            setText(value.toString());
                        }
                    }
                    else{
                        setText(value.toString());
                    }
                }
            });
        }
        return sectionTable;
    }

}
