package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class TargetSelectionPane extends JPanel{
    private static final long serialVersionUID = 3209435745432235432L;

    private CurrentDirectoryHolder currentDirectoryHolder;

    private FileFilter[] filters;

    private List<String> extensions = new ArrayList<String>();

    private String description;

    private DefaultListModel model = new DefaultListModel();

    private List<DataChangeListener> listeners = new ArrayList<DataChangeListener>();

    private boolean directorySelectable = false;

    private boolean multipleSelectable = true;

    private JList list;

    private JButton addButton;

    private JButton removeButton;

    public TargetSelectionPane(CurrentDirectoryHolder cdh){
        this.currentDirectoryHolder = cdh;
        initComponents();
        list.setModel(model);

        DropTarget dropTarget = new TargetSelectionDropTarget();
        list.setDropTarget(dropTarget);
    }

    public void addDataChangeListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void removeAllElements(){
        model.removeAllElements();
        fireEvent();
    }

    public void setFileFilter(FileFilter filter){
        setFileFilters(new FileFilter[] { filter, });
    }

    public void setFileFilters(FileFilter[] filters){
        this.filters = filters;
    }

    public FileFilter[] getFileFilters(){
        if(filters == null){
            String[] exts = getExtensions();
            filters = new FileFilter[exts.length];
            for(int i = 0; i < exts.length; i++){
                filters[i] = new ExtensionFilter(exts[i], MessageFormat.format(getSelectDescription(), exts[i]));
            };
        }
        return filters;
    }

    public void addTargetExtension(String ext){
        if(filters != null){
            filters = null;
        }
        extensions.add(ext);
    }

    public void addTargetExtensions(String[] exts){
        if(filters != null){
            filters = null;
        }
        for(String ext : exts){
            extensions.add(ext);
        }
    }

    public synchronized String[] getExtensions(){
        return extensions.toArray(new String[extensions.size()]);
    }

    public void setSelectDescription(String description){
        this.description = description;
    }

    public String getSelectDescription(){
        return description;
    }

    public String[] getValues(){
        String[] strings = new String[model.getSize()];
        for(int i = 0; i < strings.length; i++){
            strings[i] = (String)model.getElementAt(i);
        }
        return strings;
    }

    public String[] getSelectedValues(){
        int[] indeces = list.getSelectedIndices();
        String[] strings = new String[indeces.length];
        for(int i = 0; i < strings.length; i++){
            strings[i] = (String)model.getElementAt(indeces[i]);
        }
        return strings;
    }

    public void addValues(String[] values){
        for(String value : values){
            addValue(value);
        }
    }

    public void addValue(String value){
        model.addElement(value);
        fireEvent();
    }

    @Override
    public void setEnabled(boolean flag){
        super.setEnabled(flag);
        addButton.setEnabled(flag);
        removeButton.setEnabled(flag);
        list.setEnabled(flag);
    }

    public void setDirectorySelectable(boolean flag){
        this.directorySelectable = flag;
    }

    public boolean isDirectorySelectable(){
        return directorySelectable;
    }

    public void setMultipleSelectable(boolean flag){
        this.multipleSelectable = flag;
    }

    public boolean isMultipleSelectable(){
        return multipleSelectable;
    }

    private void fireEvent(){
        for(int i = listeners.size() - 1; i >= 0; i--){
            DataChangeListener listener = listeners.get(i);
            listener.valueChanged(model);
        }
    }

    private void initComponents(){
        JScrollPane scroll = new JScrollPane();
        JComponent south = Box.createHorizontalBox();
        list = new JList();
        addButton = Utility.createButton("addpackage");
        removeButton = Utility.createButton("removepackage");
        removeButton.setEnabled(false);

        setLayout(new BorderLayout());

        scroll.setViewportView(list);
        add(scroll, java.awt.BorderLayout.CENTER);
        south.add(Box.createHorizontalGlue());
        south.add(addButton);
        south.add(Box.createHorizontalGlue());
        south.add(removeButton);
        south.add(Box.createHorizontalGlue());

        list.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent evt){
                listValueChanged(evt);
            }
        });

        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                addButtonActionPerformed(evt);
            }
        });

        removeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                removeButtonActionPerformed(evt);
            }
        });

        add(south, BorderLayout.SOUTH);

    }

    private void removeButtonActionPerformed(ActionEvent evt){
        int[] indeces = list.getSelectedIndices();
        if(indeces != null && indeces.length >= 1){
            for(int i = indeces.length - 1; i >= 0; i--){
                model.removeElementAt(indeces[i]);
                fireEvent();
            }
        }
        removeButton.setEnabled(false);
    }

    private void listValueChanged(ListSelectionEvent evt){
        int[] indeces = list.getSelectedIndices();
        removeButton.setEnabled(isEnabled() && indeces != null);
    }

    private void addButtonActionPerformed(ActionEvent evt){
        JFileChooser chooser = new JFileChooser(currentDirectoryHolder.getCurrentDirectory());
        FileFilter[] filters = getFileFilters();

        for(FileFilter filter: filters){
            chooser.addChoosableFileFilter(filter);
        }
        chooser.setMultiSelectionEnabled(isMultipleSelectable());
        if(isDirectorySelectable()){
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        int returnCode = chooser.showOpenDialog(SwingUtilities.getRootPane(this));
        if(returnCode == JFileChooser.APPROVE_OPTION){
            currentDirectoryHolder.setCurrentDirectory(chooser.getCurrentDirectory());
            File[] files = chooser.getSelectedFiles();
            for(File file : files){
                addValue(file.getPath());
            }
        }
    }

    private class TargetSelectionDropTarget extends DropTarget{
        private static final long serialVersionUID = 3204457621345L;

        public void dragEnter(DropTargetDragEvent arg0){
        }

        public void dragExit(DropTargetEvent arg0){
        }

        public void dragOver(DropTargetDragEvent arg0){
        }

        public void dropActionChanged(DropTargetDragEvent arg0){
        }

        public void drop(DropTargetDropEvent dtde){
            dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
            Transferable trans = dtde.getTransferable();
            try{
                if(trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
                    List list = (List)trans.getTransferData(DataFlavor.javaFileListFlavor);
                    filters = getFileFilters();
                    List<String> errorList = new ArrayList<String>();
                    for(int i = 0; i < list.size(); i++){
                        File file = (File)list.get(i);
                        for(FileFilter filter: filters){
                            if(filter.accept(file)){
                                addValue(file.getPath());
                            }
                            else{
                                errorList.add(file.getName());
                            }
                        }
                    }
                    if(errorList.size() > 0){
                        StringBuilder builder = new StringBuilder("<html><body>");
                        builder.append(Messages.getString("unsupportedfiletype.dialog.message"));
                        builder.append("<ul>");
                        for(int i = 0; i < errorList.size(); i++){
                            builder.append("<li>").append(errorList.get(i)).append("</li>");
                        }
                        builder.append("</ul></body></html>");
                        JOptionPane.showMessageDialog(TargetSelectionPane.this,
                                new String(builder), Messages
                                        .getString("unsupportedfiletype.dialog.title"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch(UnsupportedFlavorException e){
            }
            catch(IOException e){
            }
        }
    }
}
