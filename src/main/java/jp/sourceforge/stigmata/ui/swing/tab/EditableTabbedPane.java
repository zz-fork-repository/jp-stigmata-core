package jp.sourceforge.stigmata.ui.swing.tab;

/*
 * $Id$
 */

import java.awt.FocusTraversalPolicy;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * This program is copied from below URL.
 * http://terai.xrea.jp/Swing/EditTabTitle.html
 * 
 * @author Haruaki Tamada
 * @author Terai Atsuhiro
 * @version $Revision$ $Date$
 */
public class EditableTabbedPane extends JTabbedPane{
    private static final long serialVersionUID = -66174062280771547L;

    private final EditableGlassPane panel;
    private final JTextField editor;
    private final JFrame frame;
    private final FocusTraversalPolicy policy;
    private FocusTraversalPolicy ftp;

    public EditableTabbedPane(JFrame frame){
        this.frame = frame;
        this.panel = new EditableGlassPane(this);
        this.editor = new JTextField();

        policy = new EditableTabbedPaneFocusTraversalPolicy(editor);

        editor.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        editor.addFocusListener(new FocusAdapter(){
            public void focusGained(final FocusEvent e){
                ((JTextField)e.getSource()).selectAll();
            }
        });
        editor.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    renameTab();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    cancelEditing();
                }
            }
        });
        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                if(me.getClickCount() == 2){
                    startEditing();
                }
            }
        });
        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    startEditing();
                }
            }
        });

        frame.setGlassPane(panel);
        panel.add(editor);
        panel.setVisible(false);
    }

    void renameTab(){
        frame.setFocusTraversalPolicy(ftp);
        if(editor.getText().trim().length() > 0){
            setTitleAt(getSelectedIndex(), editor.getText());
        }
        panel.setVisible(false);
    }

    private void startEditing(){
        initEditor();
        ftp = frame.getFocusTraversalPolicy();
        panel.setVisible(true);
        editor.requestFocusInWindow();
        frame.setFocusTraversalPolicy(policy);
    }

    private void cancelEditing(){
        frame.setFocusTraversalPolicy(ftp);
        panel.setVisible(false);
    }

    private void initEditor(){
        JMenuBar bar = frame.getJMenuBar();
        Rectangle rect = getUI().getTabBounds(this, getSelectedIndex());
        rect.setRect(rect.x + 2, rect.y + 2, rect.width - 2, rect.height - 2);
        if(bar != null){
            rect.y += bar.getSize().height;
        }
        panel.setRectangle(rect);

        editor.setBounds(rect);
        editor.setText(getTitleAt(getSelectedIndex()));
    }
}
