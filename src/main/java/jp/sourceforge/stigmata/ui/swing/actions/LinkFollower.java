package jp.sourceforge.stigmata.ui.swing.actions;

/*
 * $Id$
 */

import java.awt.Desktop;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
class LinkFollower implements HyperlinkListener{
    public void hyperlinkUpdate(HyperlinkEvent e){
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
            URL url = null;
            try{
                url = e.getURL();
                browse(url);
            } catch(RuntimeException ee){
                throw ee;
            } catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }

    private void browse(URL url) throws Exception{
    	Desktop desktop = Desktop.getDesktop();
    	if(desktop.isSupported(Desktop.Action.BROWSE)){
    		desktop.browse(url.toURI());
    	}
    }
}