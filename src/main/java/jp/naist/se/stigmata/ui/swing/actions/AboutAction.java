package jp.naist.se.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.Utility;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class AboutAction extends ShowTextAction{
    private static final long serialVersionUID = -7060581883871662749L;

    public AboutAction(Component parent){
        super(parent);
    }

    @Override
    public boolean isHtmlDocument(){
        return true;
    }

    @Override
    public String getTitle(){
        return Messages.getString("about.dialog.title");
    }

    @Override
    public String getMessage(){
        String aboutMessage = loadStringFromFile(getClass().getResource("/resources/about.html"));

        Package p = getClass().getPackage();
        aboutMessage = aboutMessage.replace("${implementation.version}", p.getImplementationVersion());
        aboutMessage = aboutMessage.replace("${implementation.vendor}",  p.getImplementationVendor());
        aboutMessage = aboutMessage.replace("${implementation.title}",   p.getImplementationTitle());

        return aboutMessage;
    }

    @Override
    protected void updatePanel(JPanel panel){
        JLabel logo = new JLabel(Utility.getIcon("stigmata.logo"));
        panel.add(logo, BorderLayout.NORTH);
    }
}
