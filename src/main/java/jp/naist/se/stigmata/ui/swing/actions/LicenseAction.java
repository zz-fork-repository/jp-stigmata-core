package jp.naist.se.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ui.swing.GUIUtility;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class LicenseAction extends ShowTextAction{
    private static final long serialVersionUID = 7727665273988881423L;

    public LicenseAction(StigmataFrame parent){
        super(parent);
    }

    public String getTitle(){
        return getMessages().get("license.dialog.title");
    }

    public String getMessage(){
        return loadStringFromFile(GUIUtility.getResource(getMessages(), "license.file"));
    }
}
