package jp.sourceforge.stigmata.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.ComparisonPairFilterSet;
import jp.sourceforge.stigmata.filter.ComparisonPairFilterManager;
import jp.sourceforge.stigmata.ui.swing.filter.ComparisonPairFilterRetainable;
import jp.sourceforge.stigmata.ui.swing.filter.FilterSetDefinitionPane;

/**
 * 
 * @author Haruaki TAMADA
 */
public class FilterSelectionPane extends JPanel implements ComparisonPairFilterRetainable{
    private static final long serialVersionUID = 1825547576389498336L;
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private StigmataFrame stigmata;
    private ComparisonPairFilterManager manager;
    private FilterSetDefinitionPane filterDef;
    private Map<String, Boolean> enableMap = new HashMap<String, Boolean>();
    private Map<String, ComparisonPairFilterSet> filters = new HashMap<String, ComparisonPairFilterSet>(); 
    private DefaultListModel model;
    private JList list;

    public FilterSelectionPane(StigmataFrame stigmata, ComparisonPairFilterManager manager){
        this.stigmata = stigmata;
        this.manager = manager;

        initLayout();
    }

    @Override
    public void addFilterSet(ComparisonPairFilterSet filter){
        filters.put(filter.getName(), filter);
        enableMap.put(filter.getName(), false);
        model.addElement(filter.getName());
    }

    @Override
    public void filterSelected(ComparisonPairFilter filter){
    }

    @Override
    public ComparisonPairFilterSet getFilterSet(String name){
        return filters.get(name);
    }

    @Override
    public void removeFilterSet(String name){
        filters.remove(name);
        enableMap.remove(name);
        model.removeElement(name);
    }

    @Override
    public void updateFilterSet(String name, ComparisonPairFilterSet filter){
        int index = model.indexOf(name);
        model.set(index, filter.getName());
        filters.put(filter.getName(), filter);
        enableMap.put(filter.getName(), enableMap.get(name));

        if(!name.equals(filter.getName())){
            enableMap.remove(name);
            filters.remove(name);
        }
    }

    public String[] getSelectedFilters(){
        List<String> list = new ArrayList<String>();
        for(String key: enableMap.keySet()){
            if(enableMap.get(key)){
                list.add(key);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    private void initLayout(){
        setLayout(new BorderLayout());
        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new CheckableListCellRenderer());

        JScrollPane scroll = new JScrollPane(list);
        filterDef = new FilterSetDefinitionPane(stigmata, this, false);

        add(scroll, BorderLayout.WEST);
        add(filterDef, BorderLayout.CENTER);

        list.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                String name = (String)list.getSelectedValue();
                filterDef.setFilterSet(filters.get(name));
                
                updateUI();
            }
        });
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = list.locationToIndex(e.getPoint());
                String value = (String)model.getElementAt(index);
                enableMap.put(value, !enableMap.get(value));
                updateUI();
            }
        });

        for(ComparisonPairFilterSet filterset: manager.getFilterSets()){
            enableMap.put(filterset.getName(), false);
            filters.put(filterset.getName(), filterset);
            model.addElement(filterset.getName());
        }
    }

    private class CheckableListCellRenderer extends JCheckBox implements ListCellRenderer{
        private static final long serialVersionUID = 2120743754620361163L;

        public CheckableListCellRenderer(){
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object v, int index, boolean isSelected, boolean cellHasFocus){
            String value = (String)v;
            setText(value);
            setSelected(enableMap.get(value));

            if(isSelected){
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else{
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            Border border = null;
            if(cellHasFocus){
                if(isSelected){
                    border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                }
                if(border == null){
                    border = UIManager.getBorder("List.focusCellHighlightBorder");
                }
            }
            else{
                border = noFocusBorder;
            }
            setBorder(border);            

            return this;
        }
        
    }
}
