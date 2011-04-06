package jp.sourceforge.stigmata.ui.swing;

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
import java.util.Iterator;
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

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.ComparisonPair;
import jp.sourceforge.stigmata.ComparisonResultSet;
import jp.sourceforge.stigmata.printer.PrinterManager;
import jp.sourceforge.stigmata.result.CertainPairComparisonResultSet;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;
import jp.sourceforge.stigmata.ui.swing.actions.SaveAction;
import jp.sourceforge.stigmata.ui.swing.actions.UpdateBirthmarkCellColorAction;
import jp.sourceforge.stigmata.utils.AsciiDataWritable;

/**
 * 
 * 
 * @author Haruaki TAMADA
 */
public class PairComparisonResultSetPane extends JPanel{
    private static final long serialVersionUID = 3298346465652354302L;

    private StigmataFrame frame;
    private ComparisonResultSet comparison;
    private DefaultTableModel model = new PairComparisonResultSetTableModel();
    private JTable table = new JTable(model);
    private JLabel averageLabel, maximumLabel, minimumLabel;
    private double average, maximum, minimum;

    public PairComparisonResultSetPane(StigmataFrame frame, ComparisonResultSet resultset){
        this.frame = frame;
        this.comparison = resultset;

        initComponent();
        initData(model, resultset);
    }

    private void obfuscateClassNames(){
        ClassNameObfuscator obfuscator = new ClassNameObfuscator();
        List<ComparisonPair> newList = new ArrayList<ComparisonPair>();
        BirthmarkContext context = comparison.getContext();

        for(Iterator<ComparisonPair> i = comparison.iterator(); i.hasNext(); ){
            ComparisonPair pair = i.next();
            BirthmarkSet set1 = obfuscator.obfuscateClassName(pair.getTarget1());
            BirthmarkSet set2 = obfuscator.obfuscateClassName(pair.getTarget2());

            context = comparison.getContext();
            newList.add(new ComparisonPair(set1, set2, context));
        }

        try{
            File file = frame.getSaveFile(frame.getMessages().getArray("obfuscationmapping.extension"),
                frame.getMessages().get("obfuscationmapping.description"));
            if(file == null){
                return;
            }
            obfuscator.outputNameMappings(file);
        }catch(IOException e){
            JOptionPane.showMessageDialog(
                this, e.getMessage(), frame.getMessages().get("error.dialog.title"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        DefaultTableModel newModel = new PairComparisonResultSetTableModel();
        initData(newModel, new CertainPairComparisonResultSet(newList.toArray(new ComparisonPair[newList.size()]), context));
        model = newModel;
        table.setModel(newModel);
    }

    private void initData(DefaultTableModel model, ComparisonResultSet comparison){
        maximum = 0d;
        minimum = 1d;
        average = 0d;
        model.setColumnIdentifiers(frame.getMessages().getArray("comparepair.table.columns"));

        for(Iterator<ComparisonPair> i = comparison.iterator(); i.hasNext(); ){
            ComparisonPair pair = i.next();
            double similarity = pair.calculateSimilarity();
            if(similarity > maximum) maximum = similarity;
            if(similarity < minimum) minimum = similarity;
            average += similarity;

            model.addRow(new Object[] { pair.getTarget1().getName(),
                         pair.getTarget2().getName(), new Double(similarity) });
        }
        average = average / comparison.getPairCount();
        
        averageLabel.setText(Double.toString(average));
        maximumLabel.setText(Double.toString(maximum));
        minimumLabel.setText(Double.toString(minimum));
    }

    private void initComponent(){
        JComponent buttonPanel = Box.createHorizontalBox();
        JPanel similarityPane = new JPanel(new GridLayout(1, 3));
        JComponent southPanel = Box.createVerticalBox();
        JButton saveButton = GUIUtility.createButton(
            frame.getMessages(), "savecomparison", new SaveAction(frame, new AsciiDataWritable(){
                @Override
                public void writeAsciiData(PrintWriter out, String format) throws IOException{
                    ResultPrinterSpi service = PrinterManager.getInstance().getService(format);
                    if(service == null){
                        service = PrinterManager.getDefaultFormatService();
                    }
                    service.getComparisonResultSetPrinter().printResult(out, comparison);
                }
            })
        );
        JButton updateColorButton = GUIUtility.createButton(
            frame.getMessages(), "updatecellcolor", new UpdateBirthmarkCellColorAction(frame, comparison.getEnvironment())
        );
        JButton obfuscateButton = GUIUtility.createButton(frame.getMessages(), "obfuscate");
        JScrollPane scroll = new JScrollPane();
        averageLabel = new JLabel(Double.toString(average), JLabel.RIGHT);
        maximumLabel = new JLabel(Double.toString(maximum), JLabel.RIGHT);
        minimumLabel = new JLabel(Double.toString(minimum), JLabel.RIGHT);

        scroll.setViewportView(table);
        table.setDefaultRenderer(Double.class, new CompareTableCellRenderer(comparison.getEnvironment()));
        similarityPane.setBorder(new TitledBorder(frame.getMessages().get("similarity.border")));
        averageLabel.setBorder(new TitledBorder(frame.getMessages().get("average.border")));
        maximumLabel.setBorder(new TitledBorder(frame.getMessages().get("maximum.border")));
        minimumLabel.setBorder(new TitledBorder(frame.getMessages().get("minimum.border")));

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
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if(col >= 1 && col < table.getColumnCount() && row >= 0
                            && row < table.getRowCount()){
                        ComparisonPair pair = comparison.getPairAt(row);

                        frame.compareDetails(pair.getTarget1(), pair.getTarget2(), comparison.getContext());
                    }
                }
            }
        });

        obfuscateButton.addActionListener(new ActionListener(){
            @Override
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
