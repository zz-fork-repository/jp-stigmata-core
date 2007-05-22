package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionResultPane extends JPanel implements BirthmarkDataWritable{
    private static final long serialVersionUID = 239084365756236543L;

    private List<BirthmarkSet> birthmarks;
    private StigmataFrame frame;

    public BirthmarkExtractionResultPane(StigmataFrame stigmataFrame, BirthmarkContext context, BirthmarkSet[] sets){
        this.frame = stigmataFrame;
        this.birthmarks = Arrays.asList(sets);

        JComponent southPanel = Box.createHorizontalBox(); 
        JButton saveButton = Utility.createButton("savebirthmark");
        JScrollPane scroll = new JScrollPane();

        scroll.setViewportView(new BirthmarkTree(birthmarks.toArray(new BirthmarkSet[birthmarks.size()])));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(saveButton);
        southPanel.add(Box.createHorizontalGlue());

        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveButtonActionPerformed(e);
            }
        });
    }

    private void saveButtonActionPerformed(ActionEvent e){
        frame.saveAction(this);
    }
    
    public void writeData(PrintWriter out, ResultFormatSpi service){
        BirthmarkExtractionResultFormat list = service.getExtractionResultFormat();

        list.printResult(new PrintWriter(out), birthmarks.toArray(new BirthmarkSet[birthmarks.size()]));
    }
}
