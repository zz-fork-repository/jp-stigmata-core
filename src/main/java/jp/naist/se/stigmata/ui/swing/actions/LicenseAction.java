package jp.naist.se.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Component;

import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.GUIUtility;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class LicenseAction extends ShowTextAction{
    private static final long serialVersionUID = 7727665273988881423L;

    public LicenseAction(Component parent){
        super(parent);
    }

    public String getTitle(){
        return Messages.getString("license.dialog.title");
    }

    public String getMessage(){
        return loadStringFromFile(GUIUtility.getResource("license.file"));
    }
}
