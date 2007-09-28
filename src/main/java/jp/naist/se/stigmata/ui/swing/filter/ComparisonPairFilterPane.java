package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.filter.Criterion;
import jp.naist.se.stigmata.filter.FilterTarget;
import jp.naist.se.stigmata.ui.swing.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class ComparisonPairFilterPane extends JPanel{
    private Map<String, Criterion> criterionMap = new HashMap<String, Criterion>();
    private Map<String, FilterTarget> targetMap = new HashMap<String, FilterTarget>();
    
    public abstract String[] getErrors();

    public abstract void setFilter(ComparisonPairFilter filter);

    public abstract ComparisonPairFilter getFilter();

    public abstract void resetComponents();

    public JComboBox createTargetBox(){
        JComboBox combo = new JComboBox();
        combo.setEditable(false);
        for(FilterTarget target: FilterTarget.values()){
            String value = Messages.getString("target." + target.name());
            combo.addItem(value);
            targetMap.put(value, target);
        }
        return combo;
    }

    public FilterTarget getTarget(String value){
        return targetMap.get(value);
    }

    public String getDisplayTarget(FilterTarget target){
        return Messages.getString("target." + target.name());
    }

    public JComboBox createCriteriaBox(Criterion[] criteria){
        JComboBox combo = new JComboBox();
        combo.setEditable(false);
        for(int i = 0; i < criteria.length; i++){
            String value = Messages.getString("criterion." + criteria[i].name());
            combo.addItem(value);
            criterionMap.put(value, criteria[i]);
        }

        return combo;
    }

    public Criterion getCriterion(String value){
        return criterionMap.get(value);
    }

    public String getDisplayCriterion(Criterion criterion){
        return Messages.getString("criterion." + criterion.name());
    }
}
