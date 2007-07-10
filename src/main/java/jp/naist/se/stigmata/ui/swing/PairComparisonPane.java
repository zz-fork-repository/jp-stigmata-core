package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairElement;
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.ui.swing.actions.SaveAction;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PairComparisonPane extends JPanel{
    private static final long serialVersionUID = 2342856785474356234L;

    private StigmataFrame frame;
    private ComparisonPair pair;

    public PairComparisonPane(StigmataFrame frame, ComparisonPair pair){
        this.frame = frame;
        this.pair = pair;

        initialize();
    }

    private void initialize(){
        this.setLayout(new java.awt.BorderLayout());
        this.add(getSouthPanel(), java.awt.BorderLayout.SOUTH);
        this.add(getMainPane(), java.awt.BorderLayout.CENTER);
    }

    private JPanel getSouthPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel similarityPanel = new JPanel();
        similarityPanel.setLayout(new GridLayout(1, 3));

        JLabel label = new JLabel(Double.toString(pair.calculateSimilarity()));
        label.setBorder(BorderFactory.createTitledBorder(Messages.getString("result.border")));
        JComboBox combo = new JComboBox();
        for(ComparisonPairElement elem : pair){
            combo.addItem(new ClippedLRListCellRenderer.LRItem(elem.getType(), elem.getSimilarity()));
        }
        combo.setRenderer(new ClippedLRListCellRenderer(new Dimension(100, combo.getPreferredSize().height), 50));
        combo.setBorder(BorderFactory.createTitledBorder(Messages.getString("eachbirthmarksimilarity.border")));
        similarityPanel.add(label);
        similarityPanel.add(combo);
        similarityPanel.setBorder(new TitledBorder(Messages.getString("similarity.border")));

        panel.add(similarityPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = Utility.createButton(
            "savecomparison", new SaveAction(frame, new AsciiDataWritable(){
                public void writeAsciiData(PrintWriter out, String format) throws IOException{
                    ResultFormatSpi service = FormatManager.getInstance().getService(format);
                    if(service == null){
                        service = FormatManager.getDefaultFormatService();
                    }

                    service.getComparisonResultFormat().printResult(out, pair);
                }
            })
        );

        buttonPanel.add(saveButton);
        panel.add(buttonPanel);

        return panel;
    }

    private JSplitPane getMainPane(){
        JSplitPane spliter = new javax.swing.JSplitPane();
        spliter.setLeftComponent(getSpliterPanel(pair.getTarget1()));
        spliter.setRightComponent(getSpliterPanel(pair.getTarget2()));
        spliter.setDividerLocation((spliter.getWidth() - spliter.getDividerSize()) / 2);
        spliter.setContinuousLayout(true);

        return spliter;
    }

    private JPanel getSpliterPanel(BirthmarkSet birthmark){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTree tree = new JTree(new BirthmarkTreeNode(birthmark));

        panel.add(new JScrollPane(tree), BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        panel.add(south, BorderLayout.SOUTH);

        JLabel elementCount = new JLabel(Integer.toString(birthmark.getSumOfElementCount()));
        elementCount.setBorder(new TitledBorder(Messages.getString("elementcount.border")));
        south.add(elementCount, BorderLayout.CENTER);

        return panel;
    }
}
