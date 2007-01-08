package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PairComparisonResultSetPane extends JPanel implements BirthmarkDataWritable{
    private static final long serialVersionUID = 3298346465652354302L;

    private StigmataFrame frame;

    private ComparisonResultSet resultset;

    private DefaultTableModel model = new DefaultTableModel();

    private JTable table = new JTable(model);

    private List<ComparisonPair> list = new ArrayList<ComparisonPair>();

    double average, maximum, minimum;

    public PairComparisonResultSetPane(StigmataFrame frame, ComparisonResultSet resultset){
        this.frame = frame;
        this.resultset = resultset;

        initData();
        initComponent();
    }

    public void writeData(PrintWriter out, ResultFormatSpi service) throws IOException{
        service.getComparisonResultFormat().printResult(out, resultset);
    }

    private void initData(){
        maximum = 0d;
        minimum = 1d;
        average = 0d;
        model.setColumnIdentifiers(Messages.getStringArray("comparepair.table.columns"));

        for(ComparisonPair pair : resultset){
            list.add(pair);

            double similarity = pair.calculateSimilarity();
            if(similarity > maximum) maximum = similarity;
            if(similarity < minimum) minimum = similarity;
            average += similarity;

            model.addRow(new Object[] { pair.getTarget1().getClassName(),
                    pair.getTarget2().getClassName(), new Double(similarity) });
        }
        average = average / list.size();
    }

    private void initComponent(){
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel similarityPane = new JPanel(new GridLayout(1, 3));
        JComponent southPanel = Box.createVerticalBox();
        JButton saveButton = Utility.createButton("savecomparison");
        JButton obfuscateButton = Utility.createButton("obfuscate");
        JScrollPane scroll = new JScrollPane();
        JLabel avg = new JLabel(Double.toString(average));
        JLabel max = new JLabel(Double.toString(maximum));
        JLabel min = new JLabel(Double.toString(minimum));

        scroll.setViewportView(table);
        similarityPane.setBorder(new TitledBorder(Messages.getString("similarity.border")));
        avg.setBorder(new TitledBorder(Messages.getString("average.border")));
        max.setBorder(new TitledBorder(Messages.getString("maximum.border")));
        min.setBorder(new TitledBorder(Messages.getString("minimum.border")));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        similarityPane.add(avg);
        similarityPane.add(max);
        similarityPane.add(min);
        southPanel.add(similarityPane);
        southPanel.add(buttonPanel);
        buttonPanel.add(saveButton);
        buttonPanel.add(obfuscateButton);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if(col >= 1 && col < table.getColumnCount() && row >= 0
                            && row < table.getRowCount()){
                        ComparisonPair pair = list.get(row);

                        frame.compareDetails(pair.getTarget1(), pair.getTarget2(), resultset
                                .getContext());
                    }
                }
            }
        });
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.saveAction(PairComparisonResultSetPane.this);
            }
        });

        obfuscateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
    }
}
