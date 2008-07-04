package jp.sourceforge.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.filter.Criterion;
import jp.sourceforge.stigmata.filter.FilterTarget;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;
import jp.sourceforge.talisman.i18n.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public abstract class ComparisonPairFilterPane extends JPanel{
	private static final long serialVersionUID = -5201125568912008141L;

	private StigmataFrame stigmata;
    private Map<String, Criterion> criterionMap = new HashMap<String, Criterion>();
    private Map<String, FilterTarget> targetMap = new HashMap<String, FilterTarget>();

    public ComparisonPairFilterPane(StigmataFrame frame){
        this.stigmata = frame;
    }

    public Messages getMessages(){
        return stigmata.getMessages();
    }

    public abstract String[] getErrors();

    public abstract void setFilter(ComparisonPairFilter filter);

    public abstract ComparisonPairFilter getFilter();

    public abstract void resetComponents();

    public JComboBox createTargetBox(){
        JComboBox combo = new JComboBox();
        combo.setEditable(false);
        for(FilterTarget target: FilterTarget.values()){
            String value = getMessages().get("target." + target.name());
            combo.addItem(value);
            targetMap.put(value, target);
        }
        return combo;
    }

    public FilterTarget getTarget(String value){
        return targetMap.get(value);
    }

    public String getDisplayTarget(FilterTarget target){
        return getMessages().get("target." + target.name());
    }

    public JComboBox createCriteriaBox(Criterion[] criteria){
        JComboBox combo = new JComboBox();
        combo.setEditable(false);
        for(int i = 0; i < criteria.length; i++){
            String value = getMessages().get("criterion." + criteria[i].name());
            combo.addItem(value);
            criterionMap.put(value, criteria[i]);
        }

        return combo;
    }

    public Criterion getCriterion(String value){
        return criterionMap.get(value);
    }

    public String getDisplayCriterion(Criterion criterion){
        return getMessages().get("criterion." + criterion.name());
    }
}
