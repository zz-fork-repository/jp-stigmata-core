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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.RoundRobinComparisonResultSet;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class RoundRobinComparisonResultPane extends JPanel implements BirthmarkDataWritable{
    private static final long serialVersionUID = 2134574576543623L;

    private List<BirthmarkSet> birthmarksX;
    private List<BirthmarkSet> birthmarksY;
    private JTable table;
    private DefaultTableModel model;
    private JLabel classCount, comparisonCount, distinctionRatio;
    private JLabel average, minimum, maximum;
    private StigmataFrame stigmataFrame;
    private BirthmarkContext context;

    public RoundRobinComparisonResultPane(StigmataFrame stigmata, BirthmarkContext context,
                                          BirthmarkSet[] birthmarksX, BirthmarkSet[] birthmarksY){
        this.stigmataFrame = stigmata;
        this.context = context;
        this.birthmarksX = Arrays.asList(birthmarksX);
        this.birthmarksY = Arrays.asList(birthmarksY);

        initialize();
        compare(model);
    }

    public void writeData(PrintWriter out, ResultFormatSpi service){
        service.getComparisonResultFormat().printResult(out,
            new RoundRobinComparisonResultSet(
                birthmarksX.toArray(new BirthmarkSet[birthmarksX.size()]), 
                birthmarksY.toArray(new BirthmarkSet[birthmarksY.size()]), context
            )
        );
    }

    private void compare(DefaultTableModel model){
        int comparison = birthmarksX.size() * birthmarksY.size();

        classCount.setText(Integer.toString(birthmarksX.size() + birthmarksY.size()));
        comparisonCount.setText(Integer.toString(comparison));
        int correct = 0;
        double avg = 0d;
        double max = 0d;
        double min = 100d;
        model.addColumn("");
        for(BirthmarkSet x: birthmarksX){
            model.addColumn(x.getClassName());
        }
        for(int j = 0; j < birthmarksY.size(); j++){
            Object[] rows = new Object[birthmarksX.size() + 1];
            rows[0] = birthmarksY.get(j).getClassName();

            for(int i = 0; i < birthmarksX.size(); i++){
                double similarity = compare(context, birthmarksX.get(i), birthmarksY.get(j));
                rows[i + 1] = new Double(similarity);

                if(Math.abs(similarity - 1) < 1E-8){
                    correct += 1;
                }
                avg += similarity;
                if(max < similarity) max = similarity;
                if(min > similarity) min = similarity;
            }
            model.addRow(rows);
        }
        distinctionRatio.setText(
            Double.toString((double)(comparison - correct) / (double)comparison)
        );
        avg = avg / comparison;
        average.setText(Double.toString(avg));
        minimum.setText(Double.toString(min));
        maximum.setText(Double.toString(max));
    }

    private double compare(BirthmarkContext context, BirthmarkSet x, BirthmarkSet y){
        double similarity = 0d;
        int count = 0;

        for(String type: x.getBirthmarkTypes()){
            Birthmark b1 = x.getBirthmark(type);
            Birthmark b2 = y.getBirthmark(type);
            if(b1 != null && b2 != null){
                BirthmarkComparator comparator = context.getService(type).getComparator();
                double result = comparator.compare(b1, b2);
                if(result != Double.NaN){
                    similarity += result;
                    count++;
                }
            }
        }
        return similarity / count;
    }

    private Component getMainPane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        model = new RoundRobinComparisonResultSetTableModel();
        table = new JTable(model);
        table.setDefaultRenderer(Double.class, new CompareTableCellRenderer());
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if(col >= 1 && col < table.getColumnCount() && row >= 0
                            && row < table.getRowCount()){
                        BirthmarkSet b1 = birthmarksX.get(col - 1);
                        BirthmarkSet b2 = birthmarksY.get(row);

                        stigmataFrame.compareDetails(b1, b2, context);
                    }
                }
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(table);
        scroll.setWheelScrollingEnabled(true);
        panel.add(scroll, BorderLayout.CENTER);
        JComponent south = Box.createVerticalBox();
        JPanel box1 = new JPanel(new GridLayout(1, 3));
        box1.add(classCount = new JLabel());
        box1.add(comparisonCount = new JLabel());
        box1.add(distinctionRatio = new JLabel());
        south.add(box1);
        JPanel box2 = new JPanel(new GridLayout(1, 3));
        box2.setBorder(new TitledBorder(Messages.getString("similarity.border"))); //$NON-NLS-1$
        box2.add(average = new JLabel());
        box2.add(minimum = new JLabel());
        box2.add(maximum = new JLabel());
        south.add(box2);

        classCount.setBorder(new TitledBorder(Messages.getString("numberofclasses.border"))); //$NON-NLS-1$
        comparisonCount.setBorder(new TitledBorder(Messages.getString("comparisoncount.border"))); //$NON-NLS-1$
        distinctionRatio.setBorder(new TitledBorder(Messages.getString("distinctionratio.border"))); //$NON-NLS-1$
        average.setBorder(new TitledBorder(Messages.getString("average.border"))); //$NON-NLS-1$
        minimum.setBorder(new TitledBorder(Messages.getString("minimum.border"))); //$NON-NLS-1$
        maximum.setBorder(new TitledBorder(Messages.getString("maximum.border"))); //$NON-NLS-1$

        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private void saveButtonActionPerformed(ActionEvent e){
        stigmataFrame.saveAction(this);
    }

    private void graphButtonActionPerformed(ActionEvent e){
        Map<Integer, Integer> values = new HashMap<Integer, Integer>();
        for(int i = 0; i < table.getRowCount(); i++){
            for(int j = 1; j < table.getColumnCount(); j++){
                Double d = (Double)table.getValueAt(i, j);
                int similarity = (int)Math.round(d.doubleValue() * 100);
                Integer dist = values.get(new Integer(similarity));
                if(dist == null){
                    dist = new Integer(0);
                }
                dist = new Integer(dist.intValue() + 1);
                values.put(new Integer(similarity), dist);
            }
        }
        stigmataFrame.showGraph(values);
    }

    private void initialize(){
        JButton save = Utility.createButton("savecomparison"); //$NON-NLS-1$
        JButton graph = Utility.createButton("showgraph"); //$NON-NLS-1$
        JButton obfuscate = Utility.createButton("obfuscate"); //$NON-NLS-1$
        JButton compare = Utility.createButton("compare"); //$NON-NLS-1$
        final JComboBox combo = new JComboBox();
        JComponent southPanel = Box.createHorizontalBox();

        String[] comparisonMethods = Messages.getStringArray("comparison.methods.inroundrobinresult");
        for(int i = 0; i < comparisonMethods.length; i++){
            combo.addItem(comparisonMethods[i]);
        }

        setLayout(new BorderLayout());
        add(getMainPane(), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(save);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(graph);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(obfuscate);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(compare);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(combo);
        southPanel.add(Box.createHorizontalGlue());

        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveButtonActionPerformed(e);
            }
        });

        graph.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                graphButtonActionPerformed(e);
            }
        });
        obfuscate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                obfuscateClassNames();
            }
        });

        compare.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String item = (String)combo.getSelectedItem();
                if(item.equals(Messages.getString("guessedpair.label"))){
                    compareGuessedPair();
                }
                else if(item.equals(Messages.getString("specifiedpair.label"))){
                    compareSpecifiedPair();
                }
            }
        });
    }

    private void compareGuessedPair(){
        ComparisonResultSet resultset = new CertainPairComparisonResultSet(
            birthmarksX.toArray(new BirthmarkSet[birthmarksX.size()]), 
            birthmarksY.toArray(new BirthmarkSet[birthmarksY.size()]),
            context
        );
        stigmataFrame.showComparisonResultSet(resultset);
    }

    private void compareSpecifiedPair(){
        File file = stigmataFrame.getOpenFile(Messages.getStringArray("comparemapping.extension"), Messages.getString("comparemapping.description"));
        if(file != null){
            Map<String, String> mapping = stigmataFrame.constructMapping(file);

            ComparisonResultSet resultset = new CertainPairComparisonResultSet(
                birthmarksX.toArray(new BirthmarkSet[birthmarksX.size()]), 
                birthmarksY.toArray(new BirthmarkSet[birthmarksY.size()]),
                mapping, context
            ); 
            stigmataFrame.showComparisonResultSet(resultset);
        }
    }

    private void obfuscateClassNames(){
        ClassNameObfuscator obfuscator = new ClassNameObfuscator();

        for(int i = 0; i < birthmarksX.size(); i++){
            birthmarksX.set(i, obfuscator.obfuscateClassName(birthmarksX.get(i)));
        }
        for(int i = 0; i < birthmarksY.size(); i++){
            birthmarksY.set(i, obfuscator.obfuscateClassName(birthmarksY.get(i)));
        }

        try{
            File file = stigmataFrame.getSaveFile(Messages.getStringArray("obfuscationmapping.extension"),
                    Messages.getString("obfuscationmapping.description"));
            obfuscator.outputNameMappings(file);
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), Messages
                    .getString("error.dialog.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = new RoundRobinComparisonResultSetTableModel();
        compare(model);
        table.setModel(model);
        this.model = model;
    }

    private static class RoundRobinComparisonResultSetTableModel extends DefaultTableModel{
        private static final long serialVersionUID = 765435324523543242L;

        @Override
        public boolean isCellEditable(int row, int col){
            return false;
        }

        public Class<?> getColumnClass(int column){
            if(column == 0){
                return String.class;
            }
            else{
                return Double.class;
            }
        }
    }
}
