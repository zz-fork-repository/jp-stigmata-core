package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
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
import jp.naist.se.stigmata.format.FormatManager;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.ui.swing.actions.SaveAction;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionResultPane extends JPanel{
    private static final long serialVersionUID = 239084365756236543L;

    private List<BirthmarkSet> birthmarks;
    private StigmataFrame frame;

    public BirthmarkExtractionResultPane(StigmataFrame stigmataFrame, BirthmarkContext context, BirthmarkSet[] sets){
        this.frame = stigmataFrame;
        this.birthmarks = Arrays.asList(sets);

        JComponent southPanel = Box.createHorizontalBox(); 
        JButton saveButton = Utility.createButton("savebirthmark", new SaveAction(frame, new AsciiDataWritable(){
            public void writeAsciiData(PrintWriter out, String format){
                ResultFormatSpi service = FormatManager.getInstance().getService(format);
                if(service == null){
                    service = FormatManager.getDefaultFormatService();
                }

                BirthmarkExtractionResultFormat list = service.getExtractionResultFormat();
                list.printResult(new PrintWriter(out), birthmarks.toArray(new BirthmarkSet[birthmarks.size()]));
            }
        }));
        JScrollPane scroll = new JScrollPane();

        scroll.setViewportView(new BirthmarkTree(birthmarks.toArray(new BirthmarkSet[birthmarks.size()])));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(saveButton);
        southPanel.add(Box.createHorizontalGlue());
    }
}
