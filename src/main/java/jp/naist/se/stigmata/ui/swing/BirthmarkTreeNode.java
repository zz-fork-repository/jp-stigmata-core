package jp.naist.se.stigmata.ui.swing;

/*
 * $Id$
 */

import javax.swing.tree.DefaultMutableTreeNode;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkTreeNode extends DefaultMutableTreeNode{
    private static final long serialVersionUID = -12323457653245L;
    private Birthmark birthmark;

    public BirthmarkTreeNode(Birthmark birthmark){
        super(String.format("%s (%d)", birthmark.getType(), birthmark.getElementCount()));
        setBirthmark(birthmark);
    }

    public BirthmarkTreeNode(BirthmarkSet birthmark){
        super(String.format("%s (%d)", birthmark.getName(), birthmark.getSumOfElementCount()));
        setBirthmark(birthmark);
    }

    public Birthmark getBirthmark(){
        return birthmark;
    }

    public void setBirthmark(BirthmarkSet holder){
        addChildBirthmarks(holder, this);
    }

    public void setBirthmark(Birthmark birthmark){
        this.birthmark = birthmark;
        addChildren(birthmark, this);
    }

    private void addChildren(Birthmark birthmark, DefaultMutableTreeNode parent){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(
            birthmark.getType() + "(" +
            birthmark.getElementCount() + ")");
        parent.add(node);

        BirthmarkElement[] elements = birthmark.getElements();
        for(int i = 0; i < elements.length; i++){
            node.add(new DefaultMutableTreeNode(elements[i]));
        }
    }

    private void addChildBirthmarks(BirthmarkSet holder, DefaultMutableTreeNode parent){
        Birthmark[] birthmarks = holder.getBirthmarks();
        for(Birthmark birthmark: birthmarks){
            addChildren(birthmark, parent);
        }
    }
}
