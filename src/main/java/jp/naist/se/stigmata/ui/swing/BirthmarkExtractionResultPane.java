package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.printer.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.printer.FormatManager;
import jp.naist.se.stigmata.spi.ResultPrinterSpi;
import jp.naist.se.stigmata.ui.swing.actions.SaveAction;
import jp.naist.se.stigmata.utils.AsciiDataWritable;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionResultPane extends JPanel{
    private static final long serialVersionUID = 239084365756236543L;

    private StigmataFrame frame;
    private ExtractionResultSet extraction;

    public BirthmarkExtractionResultPane(StigmataFrame stigmataFrame, ExtractionResultSet ers){
        this.frame = stigmataFrame;
        this.extraction = ers;

        JComponent southPanel = Box.createHorizontalBox(); 
        JButton saveButton = Utility.createButton("savebirthmark", new SaveAction(frame, new AsciiDataWritable(){
            public void writeAsciiData(PrintWriter out, String format){
                ResultPrinterSpi service = FormatManager.getInstance().getService(format);
                if(service == null){
                    service = FormatManager.getDefaultFormatService();
                }

                BirthmarkExtractionResultFormat list = service.getExtractionResultFormat();
                list.printResult(new PrintWriter(out), extraction);
            }
        }));
        JScrollPane scroll = new JScrollPane();

        scroll.setViewportView(new BirthmarkTree(ers.getBirthmarkSets(ExtractionTarget.TARGET_BOTH)));

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(saveButton);
        southPanel.add(Box.createHorizontalGlue());
    }
}
