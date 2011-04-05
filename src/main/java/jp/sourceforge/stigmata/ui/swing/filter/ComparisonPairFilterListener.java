package jp.sourceforge.stigmata.ui.swing.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 */
public interface ComparisonPairFilterListener{
    public void filterAdded(ComparisonPairFilter filter);

    public void filterRemoved(ComparisonPairFilter filter);

    public void filterUpdated(ComparisonPairFilter oldfilter, ComparisonPairFilter newfilter);
}
