package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.filter.ComparisonPairFilterManager;
import jp.naist.se.stigmata.ui.swing.filter.FilterEditingPane;
import jp.naist.se.stigmata.ui.swing.filter.FilterSetDefinitionPane;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FilterManagementPane extends JPanel implements SettingsExportable{
    private static final long serialVersionUID = 972135792354L;
    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private StigmataFrame stigmata;
    private FilterSetDefinitionPane definition;
    private FilterEditingPane filterPane;
    private JList list;
    private DefaultListModel model;
    private Map<String, ComparisonPairFilterSet> filters = new HashMap<String, ComparisonPairFilterSet>();
    private Map<String, Boolean> enableMap = new HashMap<String, Boolean>();

    public FilterManagementPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
        
        initLayouts();
    }

    public void exportSettings(PrintWriter out) throws IOException{
        out.println("  <filterset-list>");
        for(ComparisonPairFilterSet filterset: filters.values()){
            out.println("    <filterset>");
            out.printf("      <name>%s</name>%n", filterset.getName());
            out.printf("      <match>%s</match>%n", filterset.isMatchAll()? "all": "any");
            out.println("      <filter-list>");
            for(ComparisonPairFilter filter: filterset){
                out.println("        <filter>");
                out.printf("          <filter-type>%s</filter-type>%n", filter.getService().getFilterName());
                out.printf("          <criterion>%s</criterion>%n", filter.getCriterion());
                try{
                    Map props = BeanUtils.describe(filter);
                    props.remove("service");
                    props.remove("class");
                    props.remove("criterion");
                    props.remove("acceptableCriteria");
                    out.println("          <attributes>");
                    for(Object key: props.keySet()){
                        out.println("            <attribute>");
                        out.printf("              <name>%s</name>%n", String.valueOf(key));
                        out.printf("              <value>%s</value>%n", String.valueOf(props.get(key)));
                        out.println("            </attribute>");
                    }
                    out.println("          </attributes>");
                    
                } catch(Exception e){
                    e.printStackTrace();
                }
                out.println("        </filter>");
            }
            out.println("      </filter-list>");
            out.println("    </filterset>");
        }
        out.println("  </filterset-list>");
    }

    public void reset(){
        definition.reset();
        filterPane.reset();
        model.removeAllElements();
        
        ComparisonPairFilterManager manager = stigmata.getContext().getFilterManager();
        for(ComparisonPairFilterSet filterset: manager.getFilterSets()){
            addFilterSet(filterset);
        }
    }

    public void updateFilterManager(ComparisonPairFilterManager manager){
        for(ComparisonPairFilterSet filterset: filters.values()){
            manager.addFilterSet(filterset);
        }
    }

    public synchronized String[] getSelectedFilterNames(){
        List<String> list = new ArrayList<String>();
        for(String key: enableMap.keySet()){
            if(enableMap.get(key)){
                list.add(key);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public void addFilterSet(ComparisonPairFilterSet filterset){
        filters.put(filterset.getName(), filterset);
        enableMap.put(filterset.getName(), Boolean.TRUE);

        model.addElement(filterset.getName());
        list.setSelectedIndex(model.getSize() - 1);
    }

    public void removeFilterSet(String name){
        filters.remove(name);
        enableMap.remove(name);
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
        Boolean flag = enableMap.remove(oldName);
        filters.put(newfilter.getName(), newfilter);
        enableMap.put(newfilter.getName(), flag);
    }

    public ComparisonPairFilterSet getEnableFilterSet(String name){
        if(enableMap.get(name)){
            return getFilterSet(name);
        }
        return null;
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

        Utility.decorateJComponent(filtersetlist, "filtersetlist.pane");
        Utility.decorateJComponent(definition, "filterdefinition.pane");
        Utility.decorateJComponent(filterPane, "filter.pane");

        setLayout(new GridLayout(1, 3));
        add(filtersetlist);
        add(definition);
        add(filterPane);

        /*
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(definition, BorderLayout.WEST);
        panel.add(filterPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(createFilterSetPane(), BorderLayout.WEST);
        add(panel, BorderLayout.CENTER);
        */
    }

    private JComponent createFilterSetPane(){
        model = new DefaultListModel();
        list = new JList(model);
        list.setCellRenderer(new CheckedListCellRenderer());

        final JButton upButton = Utility.createButton("moveup");
        final JButton downButton = Utility.createButton("movedown");

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
                String v = (String)model.getElementAt(index);
                enableMap.put(v, !enableMap.get(v));
                updateUI();
            }
        });
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                int[] indexes = list.getSelectedIndices();
                int rows = model.getSize();

                upButton.setEnabled(rows > 1 && indexes[0] != 0 && indexes.length == 1);
                downButton.setEnabled(rows > 1 && indexes[0] != (rows - 1) && indexes.length == 1);
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

    private class CheckedListCellRenderer extends JPanel implements ListCellRenderer{
        private static final long serialVersionUID = -8148037001055727351L;

        private JCheckBox check;
        private JLabel label;

        public CheckedListCellRenderer(){
            setOpaque(true);
            setLayout(new BorderLayout());
            add(check = new JCheckBox(), BorderLayout.WEST);
            add(label = new JLabel(), BorderLayout.CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            label.setText(String.valueOf(value));
            check.setSelected(enableMap.get(value));

            setBackground(isSelected ? list.getSelectionBackground(): list.getBackground());
            check.setBackground(isSelected ? list.getSelectionBackground(): list.getBackground());
            check.setForeground(isSelected ? list.getSelectionForeground(): list.getForeground());
            label.setBackground(isSelected ? list.getSelectionBackground(): list.getBackground());
            label.setForeground(isSelected ? list.getSelectionForeground(): list.getForeground());

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
