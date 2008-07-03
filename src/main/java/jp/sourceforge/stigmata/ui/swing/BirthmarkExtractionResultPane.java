package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.ExtractionTarget;
import jp.sourceforge.stigmata.printer.ExtractionResultSetPrinter;
import jp.sourceforge.stigmata.printer.PrinterManager;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;
import jp.sourceforge.stigmata.ui.swing.actions.PopupShowAction;
import jp.sourceforge.stigmata.ui.swing.actions.SaveAction;
import jp.sourceforge.stigmata.utils.AsciiDataWritable;
import jp.sourceforge.talisman.i18n.Messages;

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

        initLayouts();
    }

    private void initLayouts(){
        JComponent southPanel = Box.createHorizontalBox();
        Action saveAction = new SaveAction(frame, new AsciiDataWritable(){
            public void writeAsciiData(PrintWriter out, String format){
                ResultPrinterSpi service = PrinterManager.getInstance().getService(format);
                if(service == null){
                    service = PrinterManager.getDefaultFormatService();
                }

                ExtractionResultSetPrinter list = service.getExtractionResultSetPrinter();
                list.printResult(new PrintWriter(out), extraction);
            }
        });
        Action compareAction = new AbstractAction(){
            private static final long serialVersionUID = -1938101718384412339L;

            public void actionPerformed(ActionEvent e){
                frame.compareExtractionResult(extraction);
            }
        };
        Messages messages = frame.getMessages();
        JButton saveButton = GUIUtility.createButton(messages, "savebirthmark", saveAction);
        JButton compareButton = GUIUtility.createButton(messages, "comparebirthmark", compareAction);

        JPopupMenu popup = new JPopupMenu();
        popup.add(GUIUtility.createJMenuItem(messages, "savebirthmark", saveAction));
        popup.add(GUIUtility.createJMenuItem(messages, "comparebirthmark", compareAction));

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(new BirthmarkTree(frame, extraction.getBirthmarkSets(ExtractionTarget.TARGET_BOTH)));

        setLayout(new BorderLayout());
        add(popup);
        add(scroll, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(saveButton);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(compareButton);
        southPanel.add(Box.createHorizontalGlue());

        addMouseListener(new PopupShowAction(popup));
    }
}
