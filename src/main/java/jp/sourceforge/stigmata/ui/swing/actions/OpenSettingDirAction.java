package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki Tamada
 */
public class OpenSettingDirAction extends AbstractAction{
    private static final long serialVersionUID = -8347328823893358927L;

    private Component parent;
    private Messages messages;

    public OpenSettingDirAction(Component parent, Messages messages){
        this.parent = parent;
        this.messages = messages;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String home = BirthmarkEnvironment.getStigmataHome();
        Desktop desktop = Desktop.getDesktop();
        try{
            desktop.open(new File(home));
        } catch(IOException e1){
            JOptionPane.showMessageDialog(
                parent, messages.format("opensettingdir.fail", e1.getMessage()),
                messages.get("opensettingdir.fail.title"),
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
