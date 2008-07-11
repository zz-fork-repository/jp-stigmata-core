package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.sourceforge.stigmata.ui.swing.GUIUtility;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class AboutAction extends ShowTextAction{
    private static final long serialVersionUID = -7060581883871662749L;

    public AboutAction(StigmataFrame stigmata){
        super(stigmata);
    }

    @Override
    public boolean isHtmlDocument(){
        return true;
    }

    @Override
    public String getTitle(){
        return getMessages().get("about.dialog.title");
    }

    @Override
    public String getMessage(){
        String aboutMessage = loadStringFromFile(GUIUtility.getResource(getMessages(), "about.message.file"));

        Package p = getClass().getPackage();
        aboutMessage = aboutMessage.replace("${implementation.version}", p.getImplementationVersion());
        aboutMessage = aboutMessage.replace("${implementation.vendor}",  p.getImplementationVendor());
        aboutMessage = aboutMessage.replace("${implementation.title}",   p.getImplementationTitle());

        return aboutMessage;
    }

    @Override
    protected void updatePanel(JPanel panel){
        JLabel logo = new JLabel(GUIUtility.getIcon(getMessages(), "stigmata.logo"));
        panel.add(logo, BorderLayout.NORTH);
    }
}
