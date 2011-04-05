package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.sourceforge.stigmata.ui.swing.actions.PopupShowAction;
import jp.sourceforge.stigmata.utils.WellknownClassJudgeRule;
import jp.sourceforge.stigmata.utils.WellknownClassManager;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * Well-known classes judge rules management pane.
 * 
 * @author Haruaki TAMADA
 */
public class WellknownClassesSettingsPane extends JPanel{
    private static final long serialVersionUID = 329734546345634532L;

    private StigmataFrame stigmata;
    private WellknownClassManager manager;
    private JList list;
    private DefaultListModel listmodel;

    private Map<String, String> matchTypeMap = new HashMap<String, String>();
    private Map<String, String> partTypeMap = new HashMap<String, String>();

    public WellknownClassesSettingsPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
        this.manager = stigmata.getEnvironment().getWellknownClassManager();

        initLayouts();
        initializeData();
    }

    public synchronized void setWellknownClasses(WellknownClassManager manager){
        manager.clear();
        for(int i = 0; i < listmodel.getSize(); i++){
            WellknownClassJudgeRule rule = (WellknownClassJudgeRule)listmodel.getElementAt(i);
            manager.add(rule);
        }
    }

    public synchronized WellknownClassManager createWellknownClassManager(){
        WellknownClassManager manager = new WellknownClassManager();
        setWellknownClasses(manager);
        return manager;
    }

    public void reset(){
        listmodel.clear();
        initializeData();
    }

    private synchronized boolean isWellknownClass(String className){
        WellknownClassManager manager = createWellknownClassManager();

        return manager.isWellKnownClass(className);
    }

    private String findType(JComboBox combo, Map<String, String> map){
        String item = (String)combo.getSelectedItem();
        for(Map.Entry<String, String> entry: map.entrySet()){
            if(item.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }

    public void addRule(WellknownClassJudgeRule rule){
        if(rule != null){
            listmodel.addElement(rule);
            stigmata.setNeedToSaveSettings(true);
        }
    }

    public void editRule(int index){
        WellknownClassJudgeRule rule = (WellknownClassJudgeRule)listmodel.getElementAt(index);
        WellknownClassJudgeRule newrule = createOrUpdateRule(stigmata.getMessages(), rule);
        if(newrule != null){
            listmodel.setElementAt(newrule, index);
            stigmata.setNeedToSaveSettings(true);
        }
    }


    private void initializeData(){
        for(WellknownClassJudgeRule rule : manager){
            listmodel.addElement(rule);
        }

        for(WellknownClassJudgeRule.MatchType type: WellknownClassJudgeRule.MatchType.values()){
            matchTypeMap.put(type.name(), stigmata.getMessages().get("matchtype." + type.name()));
        }
        for(WellknownClassJudgeRule.MatchPartType type: WellknownClassJudgeRule.MatchPartType.values()){
            partTypeMap.put(type.name(), stigmata.getMessages().get("matchparttype." + type.name()));
        }
    }

    private void initLayouts(){
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());
        listmodel = new DefaultListModel();

        list = new JList(listmodel);
        JScrollPane scroll = new JScrollPane(list);

        center.add(scroll, BorderLayout.CENTER);
        center.add(createSouthPane(stigmata.getMessages()), BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
        add(createCheckPane(stigmata.getMessages()), BorderLayout.SOUTH);
    }

    private JComponent createCheckPane(final Messages messages){
        final JTextField text = new JTextField();
        final JButton checkButton = GUIUtility.createButton(messages, "checkwellknown");
        final JLabel label = new JLabel(GUIUtility.getIcon(messages, "wellknownclasschecker.default.icon"));
        checkButton.setEnabled(false);

        ActionListener listener = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String t = text.getText().trim();
                if(t.length() > 0){
                    String message = messages.get("wellknownclasschecker.wellknown.tooltip");
                    if(isWellknownClass(t)){
                        label.setIcon(GUIUtility.getIcon(messages, "wellknownclasschecker.wellknown.icon"));
                    }
                    else{
                        label.setIcon(GUIUtility.getIcon(messages, "wellknownclasschecker.notwellknown.icon"));
                        message = messages.get("wellknownclasschecker.notwellknown.tooltip");
                    }
                    label.setToolTipText(message);
                    String dm = String.format(
                        "<html><body><dl><dt>%s</dt><dd>%s</dd></body></html>", t, message
                    );
                    JOptionPane.showMessageDialog(
                        stigmata, dm, stigmata.getMessages().get("wellknownclasschecker.dialog.title"),
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        };
        checkButton.addActionListener(listener);
        text.addActionListener(listener);
        text.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent arg0){
                checkButton.setEnabled(text.getText().trim().length() > 0);
            }

            @Override
            public void insertUpdate(DocumentEvent arg0){
                checkButton.setEnabled(text.getText().trim().length() > 0);
            }

            @Override
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

        south.setBorder(new TitledBorder(stigmata.getMessages().get("wellknownclasschecker.border")));

        return south;
    }

    private JComponent createSouthPane(final Messages messages){
        JComponent southPanel = Box.createHorizontalBox();
        Action addAction = new AbstractAction(){
            private static final long serialVersionUID = -8749957850400877529L;

            @Override
            public void actionPerformed(ActionEvent e){
                addRule(createOrUpdateRule(messages, null));
            }
        };
        final Action removeAction = new AbstractAction(){
            private static final long serialVersionUID = 8776209200186477040L;

            @Override
            public void actionPerformed(ActionEvent e){
                int[] indeces = list.getSelectedIndices();
                for(int i = indeces.length - 1; i >= 0; i--){
                    listmodel.removeElementAt(indeces[i]);
                }
                list.getSelectionModel().clearSelection();
                stigmata.setNeedToSaveSettings(true);
            }
        };
        final Action updateAction = new AbstractAction(){
            private static final long serialVersionUID = 852965501722574084L;

            @Override
            public void actionPerformed(ActionEvent e){
                editRule(list.getSelectedIndex());
            }
        };
        JButton addButton = GUIUtility.createButton(messages, "addwellknown", addAction);
        JButton removeButton = GUIUtility.createButton(messages, "removewellknown", removeAction);
        JButton updateButton = GUIUtility.createButton(messages, "updatewellknown", updateAction);
        removeAction.setEnabled(false);
        updateAction.setEnabled(false);

        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(addButton);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(updateButton);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(removeButton);
        southPanel.add(Box.createHorizontalGlue());

        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    editRule(list.getSelectedIndex());
                }
            }
        });
        JPopupMenu popup = new JPopupMenu();
        popup.add(GUIUtility.createJMenuItem(messages, "addwellknown", addAction));
        popup.add(GUIUtility.createJMenuItem(messages, "updatewellknown", updateAction));
        popup.add(GUIUtility.createJMenuItem(messages, "removewellknown", removeAction));

        list.addMouseListener(new PopupShowAction(popup));
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent arg0){
                int[] indeces = list.getSelectedIndices();
                removeAction.setEnabled(indeces != null && indeces.length > 0);
                updateAction.setEnabled(indeces != null && indeces.length == 1);
            }
        });

        return southPanel;
    }

    private WellknownClassJudgeRule createOrUpdateRule(Messages messages, WellknownClassJudgeRule rule){
        JTextField text = new JTextField();
        text.setOpaque(true);
        GUIUtility.decorateJComponent(messages, text, "addwellknown.newrule.pattern");
        JCheckBox excludeCheck = new JCheckBox(messages.get("addwellknown.newrule.exclude.label"), false);
        GUIUtility.decorateJComponent(messages, excludeCheck, "addwellknown.newrule.exclude");
        JComboBox matchTypeComboBox = new JComboBox();
        for(Map.Entry<String, String> entry: matchTypeMap.entrySet()){
            matchTypeComboBox.addItem(entry.getValue());
        }
        JComboBox partTypeComboBox = new JComboBox();
        for(Map.Entry<String, String> entry: partTypeMap.entrySet()){
            partTypeComboBox.addItem(entry.getValue());
        }
        if(rule != null){
            text.setText(rule.getPattern());
            excludeCheck.setSelected(rule.isExclude());
            matchTypeComboBox.setSelectedItem(matchTypeMap.get(rule.getMatchType().name()));
            partTypeComboBox.setSelectedItem(partTypeMap.get(rule.getMatchPartType().name()));
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        JPanel centerPanel = new JPanel(new FlowLayout());

        centerPanel.add(partTypeComboBox);
        centerPanel.add(matchTypeComboBox);

        panel.add(excludeCheck, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(text, BorderLayout.SOUTH);

        int value = JOptionPane.showConfirmDialog(
            stigmata, panel, stigmata.getMessages().get("addwellknown.dialog.title"),
            JOptionPane.OK_CANCEL_OPTION
        );
        if(value == JOptionPane.OK_OPTION){
            String matchType = findType(matchTypeComboBox, matchTypeMap);
            String partType = findType(partTypeComboBox, partTypeMap);
            WellknownClassJudgeRule.MatchType match = null;
            WellknownClassJudgeRule.MatchPartType part = null;
            String pattern = text.getText();
            boolean excludeFlag = excludeCheck.isSelected();

            if(matchType != null && partType != null){
                match = WellknownClassJudgeRule.MatchType.valueOf(matchType);
                part = WellknownClassJudgeRule.MatchPartType.valueOf(partType);
            }

            if(match != null && partType != null && pattern != null && !pattern.equals("")){
                return new WellknownClassJudgeRule(pattern, match, part, excludeFlag);
            }
        }
        return null;
    }
}
