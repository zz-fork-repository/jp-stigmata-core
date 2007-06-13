package jp.naist.se.stigmata.ui.swing.mds;
/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class LabelMap{
    private List<String> labels = new ArrayList<String>();
    private Map<String, String> groups = new HashMap<String, String>();
    private Map<String, Integer> gids = new HashMap<String, Integer>();

    public LabelMap(){
        gids.put("", 0);
    }

    public LabelMap(String[] labels){
        this();
        for(String label: labels){
            addLabel(label);
        }
    }

    public boolean isAvailableLabel(int index){
        return index >= 0 && index < labels.size();
    }

    public void addLabel(String label){
        labels.add(label);
    }

    public String getLabel(int index){
        return labels.get(index);
    }

    public void setGroup(String label, String groupLabel){
        groups.put(label, groupLabel);
        if(gids.get(groupLabel) == null){
            gids.put(groupLabel, groups.size());
        }
    }

    public String getGroup(String label){
        String group = groups.get(label);
        if(group == null){
            group = "";
        }
        return group;
    }

    public int getGroupId(String label){
        String glabel = groups.get(label);
        Integer i = gids.get(glabel);
        if(i == null){
            i = new Integer(0);
        }
        return i;
    }

    public int getGroupCount(){
        return groups.size();
    }

    public synchronized String[] getGroupElements(String group){
        List<String> elements = new ArrayList<String>();
        for(Map.Entry<String, String> entry: groups.entrySet()){
            if(group.equals(entry.getValue())){
                elements.add(entry.getKey());
            }
        }
        return elements.toArray(new String[elements.size()]);
    }
}
