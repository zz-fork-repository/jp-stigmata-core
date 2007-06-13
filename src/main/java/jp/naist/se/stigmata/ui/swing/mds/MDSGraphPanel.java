package jp.naist.se.stigmata.ui.swing.mds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ui.swing.ChangeColorAction;
import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.PopupButton;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;
import jp.naist.se.stigmata.ui.swing.Utility;
import Jama.Matrix;

public class MDSGraphPanel extends JPanel{
    private static final long serialVersionUID = -7256554014379112897L;
    private StigmataFrame stigmata;
    private BirthmarkSet[] set;
    private LabelMap labels;
    private MDSGraphViewer viewer;

    public MDSGraphPanel(StigmataFrame stigmata, BirthmarkSet[] set){
        this(stigmata, set, stigmata.getContext());
    }

    public MDSGraphPanel(StigmataFrame stigmata, BirthmarkSet[] set, BirthmarkContext context){
        this.stigmata = stigmata;
        this.set = set;

        double[][] matrix = initData(set, context);
        initLayouts(matrix);
    }

    private void saveMDSImage(){
        File file = stigmata.getSaveFile(
            Messages.getStringArray("savemds.extensions"),
            Messages.getString("savemds.description")
        );
        Dimension size = viewer.getSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);
        viewer.update(g);
        try{
            String name = file.getName();
            ImageIO.write(image, name.substring(name.lastIndexOf('.') + 1), file);
        } catch(IOException e){
            JOptionPane.showMessageDialog(
                stigmata, e.getMessage(), "File store error", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void saveCoordinate(){
        File file = stigmata.getSaveFile(
            Messages.getStringArray("savelocation.extensions"),
            Messages.getString("savelocation.description")
        );
        PrintWriter out = null;
        try{
            out = new PrintWriter(new FileWriter(file));
            for(Iterator<Coordinate> i = viewer.coordinates(); i.hasNext(); ){
                Coordinate c = i.next();
                out.printf("%s,%s,%g,%g%n", c.getLabel(), c.getGroupId(), c.getX(), c.getY());
            }
        } catch(IOException e){
            JOptionPane.showMessageDialog(
                stigmata, e.getMessage(), "File store error", JOptionPane.WARNING_MESSAGE
            );
        } finally{
            if(out != null){
                out.close();
            }
        }
    }

    private double[][] initData(BirthmarkSet[] set, BirthmarkContext context){
        labels = new LabelMap();
        double[][] matrix = new double[set.length][set.length];

        for(int i = 0; i < set.length; i++){
            for(int j = 0; j <= i; j++){
                ComparisonPair pair = new ComparisonPair(set[i], set[j], context);
                matrix[i][j] = 1d - pair.calculateSimilarity();
                if(i != j){
                    matrix[j][i] = matrix[i][j];
                }
            }
            String className = set[i].getName();
            labels.addLabel(className);
            String groupName = getGroupName(set[i].getLocation());
            labels.setGroup(className, groupName);
        }
        return matrix;
    }

    private String getGroupName(URL location){
        String url = location.toString();
        if(url.startsWith("jar:")){
            url = url.substring("jar:".length(), url.lastIndexOf('!'));
        }
        return url;
    }

    private void initLayouts(double[][] matrix){
        viewer = new MDSGraphViewer(new MDSMethod(new Matrix(matrix)), labels);
        viewer.setShowLabel(true);
        
        Box south = Box.createHorizontalBox();

        viewer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String c = e.getActionCommand();
                for(int i = 0; i < set.length; i++){
                    if(c.equals(set[i].getName())){
                        stigmata.showExtractionResult(new BirthmarkSet[] { set[i], }, stigmata.getContext());
                    }
                }
            }
        });
        JCheckBox check = new JCheckBox(Messages.getString("showlabel.button.label"), true);
        check.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox c = (JCheckBox)e.getSource();
                viewer.setShowLabel(c.isSelected());
            }
        });

        Action pointColorAction = new ChangeColorAction(
            "updatecolor", stigmata, Color.BLACK,
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    ChangeColorAction action = (ChangeColorAction)e.getSource();
                    viewer.setPointColor(action.getColor());
                }
            }
        );
        Action overColorAction = new ChangeColorAction(
            "updateovercolor", stigmata, Color.BLUE,
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    ChangeColorAction action = (ChangeColorAction)e.getSource();
                    viewer.setOverColor(action.getColor());
                }
            }
        );
        Action saveMDSAction = new AbstractAction(){
            private static final long serialVersionUID = 3314135350231965216L;

            public void actionPerformed(ActionEvent e){
                saveMDSImage();
            }
        };
        Action saveCoordinate = new AbstractAction(){
            private static final long serialVersionUID = 1956405328339468706L;

            public void actionPerformed(ActionEvent e){
                saveCoordinate();
            }
        };
        PopupButton colorButton = new PopupButton(Utility.createButton("updatecolor", pointColorAction));
        colorButton.addMenuItem(Utility.createJMenuItem("updateovercolor", overColorAction));
        PopupButton saveButton = new PopupButton(Utility.createButton("savemds", saveMDSAction));
        saveButton.addMenuItem(Utility.createJMenuItem("savelocation", saveCoordinate));
        
        south.add(Box.createHorizontalGlue());
        south.add(saveButton);
        south.add(Box.createHorizontalGlue());
        south.add(colorButton);
        south.add(Box.createHorizontalGlue());
        south.add(check);
        south.add(Box.createHorizontalGlue());

        setLayout(new BorderLayout());
        add(viewer, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}
