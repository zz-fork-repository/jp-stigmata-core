package jp.naist.se.stigmata.ui.swing.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import Jama.Matrix;

public class MultiDimensionalScalingViewer extends JLayeredPane{
    private static final long serialVersionUID = -9196070059428975126L;
    private static final int POINT_LAYER = DEFAULT_LAYER;
    private static final int LABEL_LAYER = DEFAULT_LAYER;

    private MultiDimensionalScalingMethod mds;
    private List<double[]> list = new ArrayList<double[]>();
    private Color overColor = PointComponent.DEFAULT_OVER_COLOR;
    private Color pointColor = getForeground();
    private boolean sameAspect = false;
    private boolean showLabel = false;
    private List<PointComponent> points = new ArrayList<PointComponent>();
    private List<JLabel> labellist = new ArrayList<JLabel>();

    public MultiDimensionalScalingViewer(MultiDimensionalScalingMethod mds){
        this(mds, null);
    }

    public MultiDimensionalScalingViewer(MultiDimensionalScalingMethod mds, String[] labels){
        this.mds = mds;

        setSize(300, 300);
        setMinimumSize(getSize());
        setPreferredSize(getSize());
        initLayouts(labels);
    }

    public boolean isShowLabel(){
        return showLabel;
    }

    public void setShowLabel(boolean showLabel){
        this.showLabel = showLabel;
        repaint();
    }

    public boolean isSameAspect(){
        return sameAspect;
    }

    public void setSameAspect(boolean sameAspect){
        this.sameAspect = sameAspect;
        repaint();
    }

    public Color getOverColor(){
        return overColor;
    }

    public void setOverColor(Color color){
        this.overColor = color;
        for(int i = 0; i < getComponentCount(); i++){
            Component c = getComponent(i);
            if(c instanceof PointComponent){
                ((PointComponent)c).setOverColor(color);
            }
        }
    }

    public Color getPointColor(){
        return pointColor;
    }

    public void setPointColor(Color color){
        this.pointColor = color;
        for(int i = 0; i < getComponentCount(); i++){
            Component c = getComponent(i);
            if(c instanceof PointComponent){
                ((PointComponent)c).setForeground(color);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Dimension d = getSize();

        g.setColor(Color.GRAY);
        g.drawLine(d.width / 2, d.height, d.width / 2, 0);
        g.drawLine(0, d.height / 2, d.width, d.height / 2);

        updateComponents(d);
    }

    private void initLayouts(String[] labels){
        double[] x = mds.getCoordinate(0);
        double[] y = mds.getCoordinate(1);

        double xmax = 0d;
        double ymax = 0d;
        for(int i = 0; i < x.length; i++){
            if(xmax < Math.abs(x[i])) xmax = Math.abs(x[i]);
        }
        for(int i = 0; i < y.length; i++){
            if(ymax < Math.abs(y[i])) ymax = Math.abs(y[i]);
        }
        if(sameAspect){
            if(ymax > xmax) xmax = ymax;
            else            ymax = xmax;
        }

        int w = getWidth();
        int h = getHeight();

        int ww = w - 20;
        int hh = h - 20;

        for(int i = 0; i < x.length; i++){
            double[] xy = new double[] { x[i] / xmax, - y[i] / ymax, };
            list.add(xy);
            double xx = xy[0] * ww / 2 + (w / 2);
            double yy = xy[1] * hh / 2 + (h / 2);
            String label = String.valueOf(i);
            if(labels != null && labels.length > i){
                label = labels[i];
            }

            PointComponent p = new PointComponent(label, x[i], y[i]);
            add(p, POINT_LAYER);
            Dimension size = p.getSize();
            p.setLocation(
                new Point((int)(xx - (size.getWidth() / 2d)), (int)(yy - (size.getHeight() / 2)))
            );
            points.add(p);

            JLabel l = new JLabel(p.getLabel());
            add(l, LABEL_LAYER);
            l.setSize(l.getPreferredSize());
            Point pcp = p.getLocation();
            Dimension dsize = l.getSize();
            l.setLocation(new Point(pcp.x - (dsize.width / 2), pcp.y - 15));
            l.setVisible(isShowLabel());
            labellist.add(l);
        }
    }

    private void updateComponents(Dimension d){
        int index = 0;
        for(PointComponent pc: points){
            updateLocation(pc, list.get(index), d);
            JLabel label = labellist.get(index);
            Point pcp = pc.getLocation();
            Dimension dsize = label.getSize();
            label.setLocation(new Point(pcp.x - (dsize.width / 2), pcp.y - 15));
            label.setVisible(isShowLabel());
            index++;
        }
    }

    private void updateLocation(PointComponent c, double[] xy, Dimension d){
        double xx = xy[0] * (d.width - 20) / 2 + (d.width / 2);
        double yy = xy[1] * (d.height - 20) / 2 + (d.height / 2);
        Dimension size = c.getSize();
        c.setLocation(
            new Point((int)(xx - (size.getWidth() / 2d)), (int)(yy - (size.getHeight() / 2)))
        );
    }

    public static void main(String[] args) throws Exception{
        MultiDimensionalScalingMethod mds;
        String[] labels = null;

        labels = new String[] { 
            "Atlanta", "Chicago", "Denver", "Houston", "Los Angeles", "Miami",
            "New York", "San Francisco", "Seattle", "Washington D.C.",
        };
        Matrix matrix = new Matrix(new double[][]{
            {    0,  587, 1212,  701, 1936,  604,  748, 2139, 2182,  543, },
            {  587,    0,  920,  940, 1745, 1188,  713, 1858, 1737,  597, },
            { 1212,  920,    0,  879,  831, 1726, 1631,  949, 1021, 1494, },
            {  701,  940,  879,    0, 1374,  968, 1420, 1645, 1891, 1220, },
            { 1936, 1745,  831, 1374,    0, 2339, 2451,  347,  959, 2300, },
            {  604, 1188, 1726,  968, 2339,    0, 1092, 2592, 2734,  923, },
            {  748,  713, 1631, 1420, 2451, 1092,    0, 2571, 2408,  205, },
            { 2139, 1858,  949, 1645,  347, 2594, 2571,    0,  678, 2442, },
            { 2182, 1737, 1021, 1891,  959, 2734, 2408,  678,    0, 2329, },
            {  543,  597, 1494, 1220, 2300,  923,  205, 2442, 2329,    0, },
        });
        mds = new MultiDimensionalScalingMethod(matrix);

        mds.getCoordinateMatrix().print(8, 4);
        MultiDimensionalScalingViewer viewer = new MultiDimensionalScalingViewer(mds, labels);
        viewer.setShowLabel(true);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}
