package jp.sourceforge.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.ComparisonPair;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.result.SingleExtractionResultSet;
import jp.sourceforge.stigmata.ui.swing.ClippedLRListCellRenderer;
import jp.sourceforge.stigmata.ui.swing.GUIUtility;
import jp.sourceforge.stigmata.ui.swing.PopupButton;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;
import jp.sourceforge.stigmata.ui.swing.actions.ChangeColorAction;
import jp.sourceforge.stigmata.ui.swing.actions.SaveAction;
import jp.sourceforge.stigmata.ui.swing.mds.mark.DrawerFactory;
import jp.sourceforge.talisman.i18n.Messages;
import Jama.Matrix;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class MDSGraphPanel extends JPanel{
    private static final long serialVersionUID = -7256554014379112897L;

    private StigmataFrame stigmata;
    private BirthmarkSet[] set;
    private BirthmarkContext context;
    private LabelMap labels;
    private MDSGraphViewer viewer;

    public MDSGraphPanel(StigmataFrame stigmata, BirthmarkSet[] set, BirthmarkContext context){
        this.stigmata = stigmata;
        this.context = context;
        this.set = set;

        double[][] matrix = initData(set, context);
        initLayouts(matrix);
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

    /**
     * This method must called after
     * {@link #initData(BirthmarkSet[], BirthmarkEnvironment) <code>initData</code>}.
     * Because this method uses calculated value in initData method.
     */
    private void initLayouts(double[][] matrix){
        final Messages messages = stigmata.getMessages();
        viewer = new MDSGraphViewer(stigmata.getMessages(), new MDSMethod(new Matrix(matrix)), labels);
        viewer.setShowLabel(true);

        viewer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String c = e.getActionCommand();
                for(int i = 0; i < set.length; i++){
                    if(c.equals(set[i].getName())){
                        ExtractionResultSet ers = new SingleExtractionResultSet(context, set[i]);
                        stigmata.showExtractionResult(ers);
                    }
                }
            }
        });
        JCheckBox check = new JCheckBox(stigmata.getMessages().get("showlabel.button.label"), true);
        check.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox c = (JCheckBox)e.getSource();
                viewer.setShowLabel(c.isSelected());
            }
        });

        Action pointColorAction = new ChangeColorAction(
            "updatecolor", stigmata, Color.BLACK, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ChangeColorAction action = (ChangeColorAction)e.getSource();
                viewer.setPointColor(action.getColor());
            }
        });
        Action overColorAction = new ChangeColorAction(
            "updateovercolor", stigmata, Color.BLUE, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ChangeColorAction action = (ChangeColorAction)e.getSource();
                viewer.setOverColor(action.getColor());
            }
        });
        SaveAction saveMDSAction = new SaveAction(stigmata, new MDSImageExporter(viewer));
        saveMDSAction.setExtensions(stigmata.getMessages().getArray("savemds.extensions"));
        saveMDSAction.setDescrpition(stigmata.getMessages().get("savemds.description"));

        SaveAction saveCoordinate = new SaveAction(stigmata, new MDSPointsLocationExporter(viewer));
        saveCoordinate.setExtensions(stigmata.getMessages().getArray("savelocation.extensions"));
        saveCoordinate.setDescrpition(stigmata.getMessages().get("savelocation.description"));

        PopupButton colorButton = new PopupButton(
            GUIUtility.createButton(messages, "updatecolor", pointColorAction)
        );
        colorButton.addMenuItem(GUIUtility.createJMenuItem(messages, "updateovercolor", overColorAction));
        PopupButton saveButton = new PopupButton(
            GUIUtility.createButton(messages, "savemds", saveMDSAction)
        );
        saveButton.addMenuItem(GUIUtility.createJMenuItem(messages, "savelocation", saveCoordinate));

        JLabel numberOfDotsLabel = new JLabel(String.valueOf(set.length));
        GUIUtility.decorateJComponent(messages, numberOfDotsLabel, "mdsgraph.count");
        // set the number of dots of each groups
        JComboBox numberOfGroupsLabelCombo = new JComboBox();
        GeometoryType[] types = GeometoryType.values();
        DrawerFactory factory = DrawerFactory.getInstance();
        for(String name: labels.getGroupNames()){
            int count = labels.getGroupElementCount(name);
            if(count != 0){
                ClippedLRListCellRenderer.LRItem item = new ClippedLRListCellRenderer.LRItem(name, count);
                item.setIcon(factory.createIcon(types[labels.getGroupId(name)]));
                numberOfGroupsLabelCombo.addItem(item);
            }
        }
        numberOfGroupsLabelCombo.setEditable(false);
        Dimension dim = new Dimension(100, numberOfGroupsLabelCombo.getPreferredSize().height);
        numberOfGroupsLabelCombo.setRenderer(new ClippedLRListCellRenderer(dim, 50));
        GUIUtility.decorateJComponent(messages, numberOfGroupsLabelCombo, "mdsgraph.group");

        JPanel north = new JPanel(new GridLayout(1, 2));
        north.add(numberOfDotsLabel);
        north.add(numberOfGroupsLabelCombo);

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(saveButton);
        south.add(Box.createHorizontalGlue());
        south.add(colorButton);
        south.add(Box.createHorizontalGlue());
        south.add(check);
        south.add(Box.createHorizontalGlue());

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
                Dimension d = e.getComponent().getSize();
                viewer.setSize(d.width - 10, d.height - 10);
            }
        });
        setLayout(new BorderLayout());

        center.add(viewer);

        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}
