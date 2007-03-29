package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.Utility;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FilterSetDefinitionPane extends JPanel implements ComparisonPairFilterListener{
    private static final long serialVersionUID = 7519306603672717119L;

    private ComparisonPairFilterRetainable manager;

    private JTextField name;
    private JRadioButton matchall, matchany;
    private JList list;
    private DefaultListModel model;
    private ComparisonPairFilterSet filterset;
    private boolean buttonShown = true;

    private JButton addfilter;
    private JButton updatefilter;
    private JButton removefilter;
    private JButton upButton;
    private JButton downButton;
    
    public FilterSetDefinitionPane(ComparisonPairFilterRetainable manager){
        this(manager, true);
    }

    public FilterSetDefinitionPane(ComparisonPairFilterRetainable manager, boolean showButtons){
        this.manager = manager;
        
        initLayouts(showButtons);
    }

    public void reset(){
        // nothing to do...
    }

    public void setFilterSet(ComparisonPairFilterSet filterset){
        this.filterset = filterset;
        if(filterset != null){
            model.removeAllElements();

            name.setText(filterset.getName());
            matchall.setSelected(filterset.isMatchAll());
            matchany.setSelected(filterset.isMatchAny());
            for(ComparisonPairFilter filter: filterset){
                model.addElement(filter);
            }
        }
    }

    public void filterAdded(ComparisonPairFilter filter){
        model.addElement(filter);
        list.setSelectedIndex(model.getSize() - 1);
        updateButtonEnabled();
    }

    public void filterRemoved(ComparisonPairFilter filter){
        model.removeElement(filter);
        list.clearSelection();
        updateButtonEnabled();
    }

    public void filterUpdated(ComparisonPairFilter oldfilter, ComparisonPairFilter newfilter){
        int index = model.indexOf(oldfilter);
        if(index >= 0){
            model.setElementAt(newfilter, index);
        }
        updateButtonEnabled();
    }

    public void setEnabled(boolean flag){
        super.setEnabled(flag);

        matchall.setEnabled(flag);
        matchany.setEnabled(flag);
        name.setEnabled(flag);
        list.setEnabled(flag);
    }

    private void initLayouts(boolean showButtons){
        this.buttonShown = showButtons;

        ButtonGroup group = new ButtonGroup();
        matchall = new JRadioButton(Messages.getString("matchall.button.label"), true);
        matchall.setToolTipText(Messages.getString("matchall.button.tooltip"));
        group.add(matchall);
        matchany = new JRadioButton(Messages.getString("matchany.button.label"), false);
        matchany.setToolTipText(Messages.getString("matchany.button.tooltip"));
        group.add(matchany);
        
        JPanel north = new JPanel(new GridLayout(3, 1));
        north.add(name = new JTextField());
        north.add(matchall);
        north.add(matchany);

        addfilter = Utility.createButton("newfilterset");
        updatefilter = Utility.createButton("updatefilterset");
        removefilter = Utility.createButton("removefilterset");
        upButton = Utility.createButton("moveup");
        downButton = Utility.createButton("movedown");
        
        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(addfilter);
        south.add(Box.createHorizontalGlue());
        south.add(updatefilter);
        south.add(Box.createHorizontalGlue());
        south.add(removefilter);
        south.add(Box.createHorizontalGlue());
        south.add(upButton);
        south.add(Box.createHorizontalGlue());
        south.add(downButton);
        south.add(Box.createHorizontalGlue());

        model = new DefaultListModel();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                manager.filterSelected((ComparisonPairFilter)list.getSelectedValue());
                updateButtonEnabled();
            }
        });

        addfilter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ComparisonPairFilterSet current = createCurrentFilterSet();
                filterset = current;
                manager.addFilterSet(filterset);
                list.clearSelection();
                manager.filterSelected(null);

                updateButtonEnabled();
            }
        });

        removefilter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                manager.removeFilterSet(filterset.getName());
                filterset = null;
                name.setText("");
                matchall.setSelected(true);
                matchany.setSelected(false);
                model.removeAllElements();
                manager.filterSelected(null);

                updateButtonEnabled();
            }
        });

        updatefilter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ComparisonPairFilterSet current = createCurrentFilterSet();
                manager.updateFilterSet(filterset.getName(), current);
                filterset = current;
                list.clearSelection();
                manager.filterSelected(null);
                updateButtonEnabled();
            }
        });

        ActionListener listener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int index = list.getSelectedIndex();
                int step = 1;
                if(e.getActionCommand().equals("moveup")) step = -1;
                Object element1 = model.get(index);
                Object element2 = model.get(index + step);
                model.setElementAt(element1, index + step);
                model.setElementAt(element2, index);
                list.setSelectedIndex(index + step);
            }
        };
        name.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent e){
                updateButtonEnabled();
            }

            public void insertUpdate(DocumentEvent e){
                updateButtonEnabled();
            }

            public void removeUpdate(DocumentEvent e){
                updateButtonEnabled();
            }
        });
        upButton.addActionListener(listener);
        downButton.addActionListener(listener);

        JScrollPane listpane = new JScrollPane(list);
        setLayout(new BorderLayout());
        add(north, BorderLayout.NORTH);
        add(listpane, BorderLayout.CENTER);
        if(showButtons){
            add(south, BorderLayout.SOUTH);
        }

        Utility.decorateJComponent(name, "filtername");
        Utility.decorateJComponent(listpane, "filterorder");

        setEnabled(buttonShown);

        updateButtonEnabled();
    }

    private void updateButtonEnabled(){
        if(buttonShown){
            int index = list.getSelectedIndex();
            int size = model.getSize();

            upButton.setEnabled(size > 1 && index > 0);
            downButton.setEnabled(size > 1 && index < (size - 1));
            updatefilter.setEnabled(filterset != null);
            addfilter.setEnabled(size > 0 && name.getText().length() > 0 && manager.getFilterSet(name.getText()) == null);
            removefilter.setEnabled(filterset != null);
        }
    }

    private ComparisonPairFilterSet createCurrentFilterSet(){
        ComparisonPairFilterSet current = new ComparisonPairFilterSet();
        current.setName(name.getText());
        if(matchall.isSelected())       current.setMatchAll();
        else if(matchany.isSelected()) current.setMatchAny();
        current.removeAllFilters();

        for(int i = 0; i < model.getSize(); i++){
            current.addFilter((ComparisonPairFilter)model.get(i));
        }
        return current;
    }
}