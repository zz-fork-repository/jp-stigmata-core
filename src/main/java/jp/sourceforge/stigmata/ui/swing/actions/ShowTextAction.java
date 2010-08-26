package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jp.sourceforge.stigmata.ui.swing.StigmataFrame;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
abstract class ShowTextAction extends AbstractAction{
	private static final long serialVersionUID = 7341139545864209076L;

	private StigmataFrame parent;

    public ShowTextAction(StigmataFrame parent){
        this.parent = parent;
    }

    public abstract String getMessage();

    protected void updatePanel(JPanel panel){
    }

    public abstract String getTitle();

    public boolean isHtmlDocument(){
        return false;
    }

    public Messages getMessages(){
        return parent.getMessages();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String message = getMessage();
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane();
        String mimeType = "text/plain";
        if(isHtmlDocument()){
            mimeType = "text/html";
        }
        JEditorPane text = new JEditorPane(mimeType, message);
        text.addHyperlinkListener(new LinkFollower());

        text.setEditable(false);
        text.setCaretPosition(0);
        text.setBackground(panel.getBackground());
        scroll.setViewportView(text);

        panel.add(scroll, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(500, 300));

        updatePanel(panel);

        JOptionPane.showMessageDialog(
            parent, panel, getTitle(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected String loadStringFromFile(URL url){
        try{
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            while((line = in.readLine()) != null){
                out.print(line);
                out.println();
            }
            out.close();
            in.close();

            return writer.toString();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
