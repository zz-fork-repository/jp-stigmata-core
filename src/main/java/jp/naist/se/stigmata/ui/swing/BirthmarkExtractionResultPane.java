package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
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

    private BirthmarkSet[] holders;
    private StigmataFrame frame;

    public BirthmarkExtractionResultPane(StigmataFrame stigmataFrame, BirthmarkContext context, BirthmarkSet[] holders){
        this.frame = stigmataFrame;
        this.holders = holders;

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = Utility.createButton("savebirthmark");
        JScrollPane scroll = new JScrollPane();

        scroll.setViewportView(new BirthmarkTree(holders));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(saveButton);

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

        list.printResult(new PrintWriter(out), holders);
    }
}
