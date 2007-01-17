package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import jp.naist.se.stigmata.BirthmarkSet;

/**
 * 
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkTree extends JTree{
    private static final long serialVersionUID = 68345692177534765L;

    private BirthmarkSet[] birthmarks;

    private DefaultTreeModel model;

    private DefaultMutableTreeNode root;

    public BirthmarkTree(){
        super(new DefaultTreeModel(new DefaultMutableTreeNode(Messages
                .getString("birthmarktree.root.label"), true)));
        model = (DefaultTreeModel)getModel();
        root = (DefaultMutableTreeNode)model.getRoot();
    }

    public BirthmarkTree(BirthmarkSet[] birthmarks){
        this();
        setBirthmarks(birthmarks);
    }

    public BirthmarkSet[] getBirthmarkHolders(){
        return birthmarks;
    }

    public void setBirthmarks(BirthmarkSet[] birthmarks){
        this.birthmarks = birthmarks;

        for(int i = 0; i < birthmarks.length; i++){
            root.add(new BirthmarkTreeNode(birthmarks[i]));
        }
        expandRow(0);
    }
}
