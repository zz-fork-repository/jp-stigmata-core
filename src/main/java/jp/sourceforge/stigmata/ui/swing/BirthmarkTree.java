package jp.sourceforge.stigmata.ui.swing;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import jp.sourceforge.stigmata.BirthmarkSet;

/**
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkTree extends JTree{
    private static final long serialVersionUID = 68345692177534765L;

    private List<BirthmarkSet> birthmarks = new ArrayList<BirthmarkSet>();
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;

    public BirthmarkTree(StigmataFrame frame){
        super(new DefaultTreeModel(new DefaultMutableTreeNode(
            frame.getMessages().get("birthmarktree.root.label"), true)
        ));
        model = (DefaultTreeModel)getModel();
        root = (DefaultMutableTreeNode)model.getRoot();
    }

    public BirthmarkTree(StigmataFrame frame, BirthmarkSet[] birthmarks){
        this(frame);
        setBirthmarks(birthmarks);
    }

    public synchronized BirthmarkSet[] getBirthmarkHolders(){
        return birthmarks.toArray(new BirthmarkSet[birthmarks.size()]);
    }

    public void setBirthmarks(BirthmarkSet[] sets){
        birthmarks.clear();

        for(BirthmarkSet set: sets){
            birthmarks.add(set);
            root.add(new BirthmarkTreeNode(set));
        }
        expandRow(0);
    }
}
