package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.ui.swing.filter.ComparisonPairFilterRetainable;
import jp.naist.se.stigmata.ui.swing.filter.FilterEditingPane;
import jp.naist.se.stigmata.ui.swing.filter.FilterSetDefinitionPane;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FilterManagementPane extends JPanel implements ComparisonPairFilterRetainable{
    private static final long serialVersionUID = 972135792354L;

    private StigmataFrame stigmata;
    private FilterSetDefinitionPane definition;
    private FilterEditingPane filterPane;
    private JList list;
    private DefaultListModel model;
    private Map<String, ComparisonPairFilterSet> filters = new HashMap<String, ComparisonPairFilterSet>();

    public FilterManagementPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
        
        initLayouts();
    }

    public void reset(){
        definition.reset();
        filterPane.reset();
        model.removeAllElements();
        
        ComparisonPairFilterManager manager = stigmata.getEnvironment().getFilterManager();
        for(ComparisonPairFilterSet filterset: manager.getFilterSets()){
            addFilterSet(filterset);
        }
    }

    public void updateFilterManager(ComparisonPairFilterManager manager){
        for(ComparisonPairFilterSet filterset: filters.values()){
            manager.addFilterSet(filterset);
        }
    }

    public void addFilterSet(ComparisonPairFilterSet filterset){
        filters.put(filterset.getName(), filterset);

        model.addElement(filterset.getName());
        list.setSelectedIndex(model.getSize() - 1);
    }

    public void removeFilterSet(String name){
        filters.remove(name);
        model.removeElement(name);

        list.clearSelection();
    }

    public void updateFilterSet(String oldName, ComparisonPairFilterSet newfilter){
        for(int i = 0; i < model.getSize(); i++){
            if(oldName.equals(model.getElementAt(i))){
                model.setElementAt(newfilter.getName(), i);
                break;
            }
        }
        filters.remove(oldName);
        filters.put(newfilter.getName(), newfilter);
    }

    public ComparisonPairFilterSet getFilterSet(String name){
        return filters.get(name);
    }

    public void filterSelected(ComparisonPairFilter filter){
        filterPane.setFilter(filter);
    }

    private void initLayouts(){
        definition = new FilterSetDefinitionPane(this);
        filterPane = new FilterEditingPane(stigmata);
        filterPane.addComparisonPairFilterListener(definition);
        JComponent filtersetlist = createFilterSetPane();

        GUIUtility.decorateJComponent(filtersetlist, "filtersetlist.pane");
        GUIUtility.decorateJComponent(definition, "filterdefinition.pane");
        GUIUtility.decorateJComponent(filterPane, "filter.pane");

        setLayout(new GridLayout(1, 3));
        add(filtersetlist);
        add(definition);
        add(filterPane);
    }

    private JComponent createFilterSetPane(){
        model = new DefaultListModel();
        list = new JList(model);

        final JButton upButton = GUIUtility.createButton("moveup");
        final JButton downButton = GUIUtility.createButton("movedown");

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(upButton);
        south.add(Box.createHorizontalGlue());
        south.add(downButton);
        south.add(Box.createHorizontalGlue());

        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(index);
                updateUI();
            }
        });
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                int[] indexes = list.getSelectedIndices();
                int rows = model.getSize();

                upButton.setEnabled(indexes.length == 1 && rows > 1 && indexes[0] != 0);
                downButton.setEnabled(indexes.length == 1 && rows > 1 && indexes[0] != (rows - 1));
                if(indexes.length != 1){
                    definition.setFilterSet(null);
                }
                else{
                    definition.setFilterSet(filters.get(model.getElementAt(indexes[0])));
                }
            }
        });
        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int step = 1;
                if(e.getActionCommand().equals("moveup")){
                    step = -1;
                }
                int index = list.getSelectedIndex();
                Object e1 = model.getElementAt(index);
                Object e2 = model.getElementAt(index + step);
                model.setElementAt(e1, index + step);
                model.setElementAt(e2, index);
                list.setSelectedIndex(index + step);
            }
        };
        upButton.addActionListener(listener);
        downButton.addActionListener(listener);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        
        return panel;
    }
}
