package jp.naist.se.stigmata.ui.swing.mds;

/*
 * $Id$
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jp.naist.se.stigmata.ui.swing.Messages;
import jp.naist.se.stigmata.ui.swing.UnsupportedFormatException;
import jp.naist.se.stigmata.utils.BinaryDataWritable;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class MDSImageExporter implements BinaryDataWritable{
    private MDSGraphViewer viewer;

    public MDSImageExporter(MDSGraphViewer viewer){
        this.viewer = viewer;
    }

    public void writeBinaryData(OutputStream out, String format)
            throws IOException, UnsupportedFormatException{
        Dimension size = viewer.getSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);
        viewer.update(g);

        try{
            ImageIO.write(image, format, out);
            out.flush();
        } catch(IOException e){
            JOptionPane.showMessageDialog(
                SwingUtilities.getRoot(viewer), e.getMessage(),
                Messages.getString("error.dialog.title"),
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

}
