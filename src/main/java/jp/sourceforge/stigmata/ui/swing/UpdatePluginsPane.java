package jp.sourceforge.stigmata.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.cli.ParseException;

import jp.sourceforge.stigmata.Main;
import jp.sourceforge.stigmata.utils.HermesUtility;
import jp.sourceforge.talisman.hermes.HermesEvent;
import jp.sourceforge.talisman.hermes.HermesException;
import jp.sourceforge.talisman.hermes.HermesPercentageListener;
import jp.sourceforge.talisman.hermes.InvalidHermesConfigException;
import jp.sourceforge.talisman.hermes.maven.Artifact;
import jp.sourceforge.talisman.i18n.Messages;

public class UpdatePluginsPane extends JPanel{
    private static final long serialVersionUID = 7595296740059360819L;

    private StigmataFrame stigmata;
    private HermesUtility hermes;
    private DefaultTableModel model;
    private JTable table;

    public UpdatePluginsPane(StigmataFrame stigmata){
        this.stigmata = stigmata;
        hermes = new HermesUtility();

        initLayout();
        reload();
    }

    public void reload(){
        try{
            hermes.loadHermesContext(stigmata.getEnvironment());
        } catch(InvalidHermesConfigException e){
            GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e);
        } catch(IOException e){
            GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e);
        }
    }

    private void updateArtifacts() throws IOException, HermesException{
        UpdatePluginsPaneHermesListener listener = new UpdatePluginsPaneHermesListener(this, model);
        hermes.getHermes().addHermesListener(listener);
        hermes.update();
        hermes.getHermes().removeHermesListener(listener);
        hermes.updateContext(stigmata.getEnvironment());
        Messages m = stigmata.getMessages();
        int value = JOptionPane.showOptionDialog(
            stigmata, m.get("restart.stigmata.requrested"),
            m.get("message.dialog.title"),
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
            new String[] { m.get("restart.now"), m.get("restart.later"), }, null
        );
        if(value == JOptionPane.YES_OPTION){
            stigmata.dispose();
            try{
                new Main(new String[0]);
            } catch(ParseException e){
            }
        }
    }

    private void showLists() throws IOException, HermesException{
        Artifact[] artifacts = hermes.getUpdateTarget();
        showLists(artifacts);
    }

    private void showLists(Artifact[] artifacts){
        model.setRowCount(0);
        for(Artifact artifact: artifacts){
            Artifact original = hermes.getCurrentArtifact(artifact.getGroupId(), artifact.getArtifactId());
            model.addRow(new Object[] { 
                artifact.getGroupId(), artifact.getArtifactId(),
                original.getVersion(), artifact.getVersion(), 
                artifact.getScope(), ProgressRenderer.NOT_STARTED
            });
        }
        if(artifacts.length == 0){
            JOptionPane.showMessageDialog(stigmata, stigmata.getMessages().get("availabe.artifacts.notfound"));
        }
    }

    private void initLayout(){
        Box buttonPane = Box.createHorizontalBox();

        JButton checkButton = GUIUtility.createButton(stigmata.getMessages(), "hermes.check");
        final JButton updateButton = GUIUtility.createButton(stigmata.getMessages(), "hermes.update");

        checkButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    showLists();
                    updateButton.setEnabled(model.getRowCount() > 0);
                } catch(IOException e1){
                    GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e1);
                } catch(HermesException e1){
                    GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e1);
                }
            }
        });
        updateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    updateArtifacts();
                    updateButton.setEnabled(false);
                } catch(IOException e1){
                    GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e1);
                } catch(HermesException e1){
                    GUIUtility.showErrorDialog(stigmata, stigmata.getMessages(), e1);
                }
            }
        });
        updateButton.setEnabled(false);

        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(checkButton);
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(updateButton);
        buttonPane.add(Box.createHorizontalGlue());

        model = new DefaultTableModel(){
            private static final long serialVersionUID = -2538706137335748099L;

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            public Class<?> getColumnClass(int column){
                Class<?> clazz = String.class;
                if(column == ProgressRenderer.PROGRESS_COLUMN){
                    clazz = Integer.class;
                }
                return clazz;
            }
        };
        table = new JTable(model);
        table.setDefaultRenderer(Integer.class, new ProgressRenderer());

        model.setColumnIdentifiers(stigmata.getMessages().getArray("hermes.artifacts.labels"));

        setLayout(new BorderLayout());
        add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
    }

    private static class UpdatePluginsPaneHermesListener implements HermesPercentageListener{
        private DefaultTableModel model;
        private UpdatePluginsPane pane;
        private Map<String, Map<String, Integer>> map;

        public UpdatePluginsPaneHermesListener(UpdatePluginsPane pane, DefaultTableModel model){
            this.model = model;
            this.pane = pane;
        }

        public void downloadDone(HermesEvent e){
            Artifact a = e.getArtifact();
            model.setValueAt(ProgressRenderer.DONE, map.get(a.getGroupId()).get(a.getArtifactId()), ProgressRenderer.PROGRESS_COLUMN);
        }

        public void fileSizeGotten(HermesEvent e){
        }

        public void progress(HermesEvent e, double progress){
            Artifact a = e.getArtifact();
            model.setValueAt(
                (int)(progress * 100), map.get(a.getGroupId()).get(a.getArtifactId()),
                ProgressRenderer.PROGRESS_COLUMN
            );
        }

        public void finish(HermesEvent e){
        }

        public void targetResolved(HermesEvent e){
            Artifact[] artifacts = e.getArtifacts();
            map = buildArtifactsIndexMap(artifacts);
            pane.showLists(artifacts);
        }

        private Map<String, Map<String, Integer>> buildArtifactsIndexMap(Artifact[] artifacts){
            Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
        
            for(int i = 0; i < artifacts.length; i++){
                String groupId = artifacts[i].getGroupId();
                Map<String, Integer> submap = map.get(groupId);
                if(submap == null){
                    submap = new HashMap<String, Integer>();
                    map.put(groupId, submap);
                }
                submap.put(artifacts[i].getArtifactId(), i);
            }
        
            return map;
        }
    }


    private static class ProgressRenderer extends DefaultTableCellRenderer{
        private static final long serialVersionUID = 3098530332351108648L;

        private static final int PROGRESS_COLUMN = 5;
        public static final int DONE = 100;
        public static final int NOT_STARTED = -1;
        public static final int CANCELED = -2;

        private JProgressBar progressBar = new JProgressBar(0, 100);

        public ProgressRenderer(){
            super();
            setOpaque(true);
            progressBar.setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object originalValue, boolean isSelected, boolean hasFocus, int row, int column){
            int value = ((Integer)originalValue).intValue();
            progressBar.setValue(value);

            return progressBar;
        }
    }
}
