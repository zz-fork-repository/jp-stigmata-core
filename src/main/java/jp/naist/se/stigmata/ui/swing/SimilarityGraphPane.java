package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.Map;

import javax.swing.JPanel;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class SimilarityGraphPane extends JPanel {
    private static final long serialVersionUID = 2314463453465L;

    private Map<Integer, Integer> distributions;
    private int totalCount = 0;
    private int maxFrequency = 0;

    public SimilarityGraphPane(Map<Integer, Integer> distributions){
        setPreferredSize(new Dimension(300, 300));

        this.distributions = distributions;
        initialize();
    }

    public void paintComponent(Graphics g1){
        Graphics2D g = (Graphics2D)g1;
        Dimension dd = getSize();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension d = new Dimension(dd);
        paintBorder(g);

        g.setColor(Color.red);
        d.width = d.width - 20;
        d.height = d.height - 20;

        double w = (double)(d.width / 100d);

        Integer val1 = distributions.get(new Integer(0));
        double x = 20;
        for(int i = 1; i <= 100; i++){
            Integer val2 = distributions.get(new Integer(i));
            if(val2 == null) val2 = new Integer(0);

            double hh1 = val1.intValue() * ((double)d.height / (double)totalCount);
            double hh2 = val2.intValue() * ((double)d.height / (double)totalCount);

            g.draw(new Line2D.Double(x, d.height - hh1, x + w, d.height - hh2));
            x += w;
            val1 = val2;
        }
    }

    private void initialize(){
        maxFrequency = 0;
        for(int i = 0; i <= 100; i++){
            Integer frequency = distributions.get(new Integer(i));
            if(frequency == null){
                frequency = new Integer(0);
                distributions.put(new Integer(i), frequency);
            }
            if(maxFrequency < frequency.intValue()){
                maxFrequency = frequency.intValue();
            }
            totalCount += frequency.intValue();
        }
    }

    private void paintBorder(Graphics2D g){
        Dimension d = getSize();
        g.setColor(Color.BLACK);
        g.draw(new Line2D.Double(0, d.height - 20, d.width, d.height - 20));
        g.draw(new Line2D.Double(20, 0, 20, d.height));

        g.setColor(Color.GRAY);
        // x axis
        double h = (d.height - 20) / 2d;
        g.draw(new Line2D.Double(20, h, d.width, h));
        h = h / 2;
        g.draw(new Line2D.Double(20, h, d.width, h));
        g.draw(new Line2D.Double(20, h * 3, d.width, h * 3));

        // y axis
        double w = (d.width - 20d) / 2d;
        g.draw(new Line2D.Double(w + 20, 0, w + 20, d.height - 20));
        w = w / 2;
        g.draw(new Line2D.Double(w + 20, 0, w + 20, d.height - 20));
        g.draw(new Line2D.Double(w * 3 + 20, 0, w * 3 + 20, d.height - 20));

        g.drawString("0", 10, d.height - 5);
        g.drawString("50%", (d.width - 20) / 2 + 10, d.height - 5);
        g.drawString(Messages.getString("similarity.label"), d.width - 60, d.height - 5);
        g.drawString("50%", 0, (d.height - 20) / 2);
    }
}
