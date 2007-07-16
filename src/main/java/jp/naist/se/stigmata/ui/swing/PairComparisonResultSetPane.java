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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.CertainPairComparisonResultSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.ui.swing.actions.SaveAction;
import jp.naist.se.stigmata.ui.swing.actions.UpdateBirthmarkCellColorAction;
import jp.naist.se.stigmata.utils.AsciiDataWritable;

/**
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PairComparisonResultSetPane extends JPanel{
    private static final long serialVersionUID = 3298346465652354302L;

    private StigmataFrame frame;
    private BirthmarkEnvironment environment;
    private DefaultTableModel model = new PairComparisonResultSetTableModel();
    private JTable table = new JTable(model);
    private JLabel averageLabel, maximumLabel, minimumLabel;
    private List<ComparisonPair> list = new ArrayList<ComparisonPair>();
    private double average, maximum, minimum;

    public PairComparisonResultSetPane(StigmataFrame frame, ComparisonResultSet resultset){
        this.frame = frame;
        this.environment = resultset.getEnvironment();

        for(ComparisonPair pair: resultset){
            list.add(pair);
        }

        initComponent();
        initData(model, list);
    }

    private void obfuscateClassNames(){
        ClassNameObfuscator obfuscator = new ClassNameObfuscator();
        List<ComparisonPair> newList = new ArrayList<ComparisonPair>();
        for(ComparisonPair pair: list){
            BirthmarkSet set1 = obfuscator.obfuscateClassName(pair.getTarget1());
            BirthmarkSet set2 = obfuscator.obfuscateClassName(pair.getTarget2());

            newList.add(new ComparisonPair(set1, set2, environment));
        }

        try{
            File file = frame.getSaveFile(Messages.getStringArray("obfuscationmapping.extension"),
                    Messages.getString("obfuscationmapping.description"));
            if(file == null){
                return;
            }
            obfuscator.outputNameMappings(file);
        }catch(IOException e){
            JOptionPane.showMessageDialog(
                this, e.getMessage(), Messages.getString("error.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        DefaultTableModel newModel = new PairComparisonResultSetTableModel();
        initData(newModel, newList);
        list = newList;
        model = newModel;
        table.setModel(newModel);
    }

    private void initData(DefaultTableModel model, List<ComparisonPair> list){
        maximum = 0d;
        minimum = 1d;
        average = 0d;
        model.setColumnIdentifiers(Messages.getStringArray("comparepair.table.columns"));

        for(ComparisonPair pair: list){
            double similarity = pair.calculateSimilarity();
            if(similarity > maximum) maximum = similarity;
            if(similarity < minimum) minimum = similarity;
            average += similarity;

            model.addRow(new Object[] { pair.getTarget1().getName(),
                         pair.getTarget2().getName(), new Double(similarity) });
        }
        average = average / list.size();
        
        averageLabel.setText(Double.toString(average));
        maximumLabel.setText(Double.toString(maximum));
        minimumLabel.setText(Double.toString(minimum));
    }

    private void initComponent(){
        JComponent buttonPanel = Box.createHorizontalBox();
        JPanel similarityPane = new JPanel(new GridLayout(1, 3));
        JComponent southPanel = Box.createVerticalBox();
        JButton saveButton = Utility.createButton(
            "savecomparison", new SaveAction(frame, new AsciiDataWritable(){
                public void writeAsciiData(PrintWriter out, String format) throws IOException{
                    ResultFormatSpi service = FormatManager.getInstance().getService(format);
                    if(service == null){
                        service = FormatManager.getDefaultFormatService();
                    }
                    service.getComparisonResultFormat().printResult(
                        out, new CertainPairComparisonResultSet(
                            list.toArray(new ComparisonPair[list.size()]), environment
                        )
                    );
                }
            })
        );
        JButton updateColorButton = Utility.createButton(
            "updatecellcolor", new UpdateBirthmarkCellColorAction(this, environment)
        );
        JButton obfuscateButton = Utility.createButton("obfuscate");
        JScrollPane scroll = new JScrollPane();
        averageLabel = new JLabel(Double.toString(average), JLabel.RIGHT);
        maximumLabel = new JLabel(Double.toString(maximum), JLabel.RIGHT);
        minimumLabel = new JLabel(Double.toString(minimum), JLabel.RIGHT);

        scroll.setViewportView(table);
        table.setDefaultRenderer(Double.class, new CompareTableCellRenderer(environment));
        similarityPane.setBorder(new TitledBorder(Messages.getString("similarity.border")));
        averageLabel.setBorder(new TitledBorder(Messages.getString("average.border")));
        maximumLabel.setBorder(new TitledBorder(Messages.getString("maximum.border")));
        minimumLabel.setBorder(new TitledBorder(Messages.getString("minimum.border")));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        similarityPane.add(averageLabel);
        similarityPane.add(maximumLabel);
        similarityPane.add(minimumLabel);
        southPanel.add(similarityPane);
        southPanel.add(buttonPanel);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(updateColorButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(obfuscateButton);
        buttonPanel.add(Box.createHorizontalGlue());

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if(col >= 1 && col < table.getColumnCount() && row >= 0
                            && row < table.getRowCount()){
                        ComparisonPair pair = list.get(row);

                        frame.compareDetails(pair.getTarget1(), pair.getTarget2(), environment);
                    }
                }
            }
        });

        obfuscateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                obfuscateClassNames();
            }
        });
    }

    private static class PairComparisonResultSetTableModel extends DefaultTableModel{
        private static final long serialVersionUID = 93457234571623497L;

        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }

        @Override
        public Class<?> getColumnClass(int column){
            if(column == 2){
                return Double.class;
            }
            else{
                return String.class;
            }
        }
    }
}
