package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.ui.swing.GUIUtility;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ 
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
