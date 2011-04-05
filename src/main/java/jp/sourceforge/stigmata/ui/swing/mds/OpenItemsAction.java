package jp.sourceforge.stigmata.ui.swing.mds;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jp.sourceforge.talisman.i18n.MessageManager;
import jp.sourceforge.talisman.mds.Item;
import jp.sourceforge.talisman.mds.ui.swing.ItemsOpenEvent;
import jp.sourceforge.talisman.mds.ui.swing.ItemsOpenListener;
import jp.sourceforge.talisman.mds.ui.swing.ItemsSelectionEvent;
import jp.sourceforge.talisman.mds.ui.swing.ItemsSelectionListener;
import jp.sourceforge.talisman.mds.ui.swing.MdsPane;

/**
 * 
 * @author Haruaki Tamada
 */
public class OpenItemsAction extends AbstractAction{
    private static final long serialVersionUID = 5956900396146338537L;

    private MdsPane mdsPane;
    private MessageManager mm;
    private boolean selectedItemFlag = false;

    public OpenItemsAction(MdsPane initMdsPane, MessageManager initMm){
        super(initMm.getMessages().get("openallitems.label"));

        this.mdsPane = initMdsPane;
        this.mm = initMm;
        if(initMm.getMessages().hasValue("openitems.icon")){
            Icon icon = initMm.getMessages().getIcon("openitems.icon");
            putValue(SMALL_ICON, icon);
        }
        initMdsPane.addItemsSelectionListener(new ItemsSelectionListener(){
            @Override
            public void valueChanged(ItemsSelectionEvent e){
                Item[] items = mdsPane.getSelectedItems();
                selectedItemFlag = items.length != 0;
                if(selectedItemFlag){
                    putValue(AbstractAction.NAME, mm.getMessages().get("openitems.label"));
                }
                else{
                    putValue(AbstractAction.NAME, mm.getMessages().get("openallitems.label"));
                }
            }
        });
        initMdsPane.addItemsOpenListener(new ItemsOpenListener(){
            @Override
            public void itemOpened(ItemsOpenEvent e){
                showItems(e.getItems());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Item[] items;
        if(selectedItemFlag){
            items = mdsPane.getSelectedItems();
        }
        else{
            items = mdsPane.getItems();
        }
        if(items.length > 0){
            showItems(items);
        }
    }

    private void showItems(Item[] items){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn(mm.getMessages().get("openitems.namelabel"));
        model.addColumn(mm.getMessages().get("openitems.xlabel"));
        model.addColumn(mm.getMessages().get("openitems.ylabel"));

        for(Item item : items){
            Object[] values = new Object[3];
            values[0] = item.getName();
            values[1] = item.get(0);
            values[2] = item.get(1);
            model.addRow(values);
        }
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(
            table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        JOptionPane.showMessageDialog(
            mdsPane, scroll, mm.getMessages().get("selected.items.title"),
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
