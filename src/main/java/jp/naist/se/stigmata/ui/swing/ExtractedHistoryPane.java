package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkHistory;
import jp.naist.se.stigmata.result.history.ExtractedBirthmarkServiceManager;
import jp.naist.se.stigmata.ui.swing.actions.PopupShowAction;

/**
 * Birthmark extraction history viewer.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class ExtractedHistoryPane extends JPanel{
    private static final long serialVersionUID = 4070750464486981964L;

    private StigmataFrame stigmata;
    private JComboBox combo;
    private JList list;
    private DefaultListModel model;
    private ExtractedBirthmarkServiceManager historyManager;
    private ExtractedBirthmarkHistory currentHistory;

    public ExtractedHistoryPane(StigmataFrame stigmata){
        this.stigmata = stigmata;

        initLayouts();
        initData();
    }

    private void updateList(){
        String historyId = (String)combo.getSelectedItem();
        currentHistory = historyManager.getHistory(historyId);
        model.clear();

        for(String id: currentHistory){
            model.addElement(id);
        }
    }

    private void initData(){
        historyManager = new ExtractedBirthmarkServiceManager(stigmata.getEnvironment());

        for(String id: historyManager.getHistoryIds()){
            combo.addItem(id);
        }
    }

    private void showAction(String id){
        ExtractionResultSet ers = currentHistory.getResultSet(id);
        stigmata.showExtractionResult(ers);
    }

    private void initLayouts(){
        setLayout(new BorderLayout());

        final Action showAction = new AbstractAction(){
            private static final long serialVersionUID = 2156350514762218963L;

            public void actionPerformed(ActionEvent e){
                showAction((String)model.get(list.getSelectedIndex()));
            }
        };
        final Action refreshAction = new AbstractAction(){
            private static final long serialVersionUID = 214765021455345371L;

            public void actionPerformed(ActionEvent e){
                updateList();
            }
        };
        final Action deleteAction = new AbstractAction(){
            private static final long serialVersionUID = 8145188292702648924L;

            public void actionPerformed(ActionEvent e){
                int[] indeces = list.getSelectedIndices();
                for(int i = indeces.length - 1; i >= 0; i--){
                    String id = (String)model.get(indeces[i]);
                    currentHistory.deleteResultSet(id);
                    model.remove(indeces[i]);
                }
                list.clearSelection();
            }
        };
        model = new DefaultListModel();
        list = new JList(model);
        combo = new JComboBox();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        GUIUtility.decorateJComponent(list, "historylist");
        GUIUtility.decorateJComponent(combo, "historylocation");

        JButton showButton = GUIUtility.createButton("showhistory", showAction);
        JButton refreshButton = GUIUtility.createButton("refreshhistory", refreshAction);
        JButton deleteButton = GUIUtility.createButton("deletehistory", deleteAction);
        deleteAction.setEnabled(false);
        showAction.setEnabled(false);

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                int[] indeces = list.getSelectedIndices();
                showAction.setEnabled(currentHistory != null && indeces.length == 1);
                deleteAction.setEnabled(currentHistory != null && indeces.length > 0);
            }
        });
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = list.locationToIndex(e.getPoint());
                if(index >= 0 && e.getClickCount() == 2){
                    showAction((String)model.get(index));
                }
            }
        });
        combo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateList();
            }
        });
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(list);

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(showButton);
        south.add(Box.createHorizontalGlue());
        south.add(refreshButton);
        south.add(Box.createHorizontalGlue());
        south.add(deleteButton);
        south.add(Box.createHorizontalGlue());

        add(combo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        JPopupMenu popup = new JPopupMenu();
        popup.add(GUIUtility.createJMenuItem("showhistory"), showAction);
        popup.add(GUIUtility.createJMenuItem("refreshhistory"), refreshAction);
        popup.add(GUIUtility.createJMenuItem("deletehistory"), deleteAction);
        list.addMouseListener(new PopupShowAction(popup));
    }
}
