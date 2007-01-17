package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;

import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairElement;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PairComparisonPane extends JPanel implements BirthmarkDataWritable{
    private static final long serialVersionUID = 2342856785474356234L;

    private StigmataFrame frame;

    private ComparisonPair pair;

    public PairComparisonPane(StigmataFrame frame, ComparisonPair pair){
        this.frame = frame;
        this.pair = pair;

        initialize();
    }

    public void writeData(PrintWriter out, ResultFormatSpi service) throws IOException{
        service.getComparisonResultFormat().printResult(out, pair);
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
            combo.addItem(elem);
        }
        combo.setRenderer(new ComparisonPairElementRenderer(new Dimension(100, combo.getPreferredSize().height), 50));
        combo.setBorder(BorderFactory.createTitledBorder(Messages.getString("eachbirthmarksimilarity.border")));
        similarityPanel.add(label);
        similarityPanel.add(combo);
        similarityPanel.setBorder(new TitledBorder(Messages.getString("similarity.border")));

        panel.add(similarityPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = Utility.createButton("savecomparison");

        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.saveAction(PairComparisonPane.this);
            }
        });
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

    /**
     * copy from tempura memo available at
     * http://terai.xrea.jp/Swing/ClippedLRComboBox.html
     */
    private static class ComparisonPairElementRenderer extends JPanel implements ListCellRenderer{
        private static final long serialVersionUID = 32943674625674235L;
        private final JLabel left = new JLabel();

        private final JLabel right = new JLabel();

        public ComparisonPairElementRenderer(Dimension dim, int rightWidth){
            super(new BorderLayout());
            setOpaque(true);
            left.setOpaque(true);
            right.setOpaque(true);
            left.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
            right.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            right.setPreferredSize(new Dimension(rightWidth, 0));
            add(left, BorderLayout.CENTER);
            add(right, BorderLayout.EAST);
            setPreferredSize(dim);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus){
            ComparisonPairElement elem = (ComparisonPairElement)value;
            left.setText(elem.getType());
            right.setText(Double.toString(elem.getSimilarity()));

            setBackground(isSelected ? SystemColor.textHighlight: Color.white);
            left.setBackground(isSelected ? SystemColor.textHighlight: Color.white);
            right.setBackground(isSelected ? SystemColor.textHighlight: Color.white);
            left.setForeground(isSelected ? Color.white: Color.black);
            right.setForeground(isSelected ? Color.gray.brighter(): Color.gray);

            return this;
        }
    }
}
