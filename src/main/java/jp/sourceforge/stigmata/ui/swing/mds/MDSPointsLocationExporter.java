package jp.sourceforge.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import jp.sourceforge.stigmata.ui.swing.UnsupportedFormatException;
import jp.sourceforge.stigmata.utils.AsciiDataWritable;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$
 */
public class MDSPointsLocationExporter implements AsciiDataWritable{
    private MDSGraphViewer viewer;

    public MDSPointsLocationExporter(MDSGraphViewer viewer){
        this.viewer = viewer;
    }

    public void writeAsciiData(PrintWriter out, String format)
            throws IOException, UnsupportedFormatException{
        if(!format.equals("csv")){
            throw new UnsupportedFormatException(viewer.getMessages().format("error.unsupportedformat", format));
        }
        for(Iterator<Coordinate> i = viewer.coordinates(); i.hasNext(); ){
            Coordinate c = i.next();
            out.printf("%s,%s,%g,%g%n", c.getLabel(), c.getGroupId(), c.getX(), c.getY());
        }
    }

}
