package jp.naist.se.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import jp.naist.se.stigmata.ui.swing.mds.mark.DrawerFactory;
import Jama.Matrix;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class MDSGraphViewer extends JLayeredPane{
    private static final long serialVersionUID = -9196070059428975126L;
    private static final int POINT_LAYER = DEFAULT_LAYER;
    private static final int LABEL_LAYER = DEFAULT_LAYER;

    private MDSMethod mds;
    private List<Coordinate> plots = new ArrayList<Coordinate>();
    private Color overColor = PointComponent.DEFAULT_OVER_COLOR;
    private Color pointColor = getForeground();
    private boolean sameAspect = false;
    private boolean showLabel = false;
    private List<PointComponent> points = new ArrayList<PointComponent>();
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    public MDSGraphViewer(MDSMethod mds){
        this(mds, null);
    }

    public MDSGraphViewer(MDSMethod mds, LabelMap labels){
        this.mds = mds;

        setSize(500, 500);
        setMinimumSize(getSize());
        initLayouts(labels);
    }

    public void addActionListener(ActionListener listener){
        listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener){
        listeners.remove(listener);
    }

    @Override
    public void setSize(int width, int height){
        if(width < height) height = width;
        else               width = height;
        super.setSize(width, height);
        setPreferredSize(getSize());
    }

    public Iterator<Coordinate> coordinates(){
        return plots.iterator();
    }

    public int getPointCount(){
        return plots.size();
    }

    public Coordinate getCoordinate(int index){
        return plots.get(index);
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

        g.drawLine(0, 0, d.width - 1, 0);
        g.drawLine(d.width - 1, 0, d.width - 1, d.height - 1);
        g.drawLine(d.width / 2, d.height, d.width / 2, 0);

        g.drawLine(0, 0, 0, d.height - 1);
        g.drawLine(0, d.height - 1, d.width - 1, d.height - 1);
        g.drawLine(0, d.height / 2, d.width, d.height / 2);

        updatePointComponents(d);
    }

    protected void fireEvent(PointComponent p){
        ActionEvent e = new ActionEvent(this, 0, p.getLabel());
        for(ActionListener l: listeners){
            l.actionPerformed(e);
        }
    }

    private void initLayouts(LabelMap labels){
        double[] x = mds.getCoordinate(0);
        double[] y = mds.getCoordinate(1);
        double[] z = mds.getCoordinate(2);

        double max = 0d;
        for(int i = 0; i < x.length; i++){
            if(max < Math.abs(x[i])) max = Math.abs(x[i]);
        }
        for(int i = 0; i < y.length; i++){
            if(max < Math.abs(y[i])) max = Math.abs(y[i]);
        }
        for(int i = 0; i < z.length; i++){
            if(max < Math.abs(z[i])) max = Math.abs(z[i]);
        }

        int w = getWidth();
        int h = getHeight();

        int ww = w - 20;
        int hh = h - 20;
        DrawerFactory factory = DrawerFactory.getInstance();
        GeometoryType[] types = GeometoryType.values();
        ActionListener clickedListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                fireEvent((PointComponent)e.getSource());
            }
        };

        for(int i = 0; i < x.length; i++){
            double[] xy = new double[] { - x[i] / max, - y[i] / max, z[i] / max, };
            double xx = xy[0] * ww / 2 + (w / 2);
            double yy = xy[1] * hh / 2 + (h / 2);
            String label = String.valueOf(i);
            if(labels != null && labels.isAvailableLabel(i)){
                label = labels.getLabel(i);
            }
            Coordinate coordinate = new Coordinate(label, xy[0], xy[1], xy[2]);
            if(labels != null && labels.isGroupEnabled()){
                coordinate.setGroupId(labels.getGroupIdFromElementName(coordinate.getLabel()));
            }
            plots.add(coordinate);

            PointComponent p = new PointComponent(
                label, x[i], y[i], factory.create(types[coordinate.getGroupId()])
            );
            p.addActionListener(clickedListener);
            add(p, POINT_LAYER);
            JLabel l = new JLabel(coordinate.getShowName());
            add(l, LABEL_LAYER);
            p.setShowLabel(l);

            Dimension size = p.getSize();
            p.setLocation(
                new Point((int)(xx - (size.getWidth() / 2d)), (int)(yy - (size.getHeight() / 2d)))
            );
            points.add(p);

            l.setSize(l.getPreferredSize());
            Point pcp = p.getLocation();
            Dimension dsize = l.getSize();
            l.setLocation(new Point(pcp.x - (dsize.width / 2), pcp.y - 15));
            l.setVisible(isShowLabel());
        }
    }

    private void updatePointComponents(Dimension d){
        int index = 0;
        double width = d.getWidth();
        double height = d.getHeight();
        if(isSameAspect()){
            if(width < height) height = width;
            else               width  = height;
        }

        for(PointComponent pc: points){
            updateLocation(pc, plots.get(index), width, height, d);
            JLabel label = pc.getShowLabel();
            Point pcp = pc.getLocation();
            Dimension dsize = label.getSize();
            label.setLocation(new Point(pcp.x - (dsize.width / 2), pcp.y - 15));
            label.setVisible(isShowLabel());
            index++;
        }
    }

    private void updateLocation(PointComponent c, Coordinate coordinate, double width, double height, Dimension d){
        double xx = coordinate.getX() * (width - 20d) / 2d + (d.getWidth() / 2d);
        double yy = coordinate.getY() * (height - 20d) / 2d + (d.getHeight() / 2d);
        Dimension size = c.getSize();
        c.setLocation(
            new Point((int)(xx - (size.getWidth() / 2d)), (int)(yy - (size.getHeight() / 2)))
        );
    }

    public static void main(String[] args) throws Exception{
        MDSMethod mds;
        LabelMap labels = new LabelMap(new String[] {
            "Atlanta", "Chicago", "Denver", "Houston", "Los Angeles", "Miami",
            "New York", "San Francisco", "Seattle", "Washington D.C.",
        });
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
        mds = new MDSMethod(matrix);

        mds.getCoordinateMatrix().print(8, 4);
        MDSGraphViewer viewer = new MDSGraphViewer(mds, labels);
        viewer.setShowLabel(true);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}
