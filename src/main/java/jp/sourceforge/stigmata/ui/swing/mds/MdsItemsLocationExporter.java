package jp.sourceforge.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;

import jp.sourceforge.stigmata.ui.swing.UnsupportedFormatException;
import jp.sourceforge.stigmata.utils.AsciiDataWritable;
import jp.sourceforge.talisman.mds.Item;
import jp.sourceforge.talisman.mds.ui.swing.MdsPane;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$
 */
public class MdsItemsLocationExporter implements AsciiDataWritable{
    private MdsPane viewer;

    public MdsItemsLocationExporter(MdsPane viewer){
        this.viewer = viewer;
    }

    @Override
    public void writeAsciiData(PrintWriter out, String format)
            throws IOException, UnsupportedFormatException{
        if(!format.equals("csv")){
            throw new UnsupportedFormatException(viewer.getMessages().format("error.unsupportedformat", format));
        }
        for(Item item: viewer.getItems()){
            out.printf("%s,%s,%g,%g%n", item.getName(), item.getGroupId(), item.get(0), item.get(1));
        }
    }

}
