package jp.naist.se.stigmata.ui.swing.filter;

import jp.naist.se.stigmata.ComparisonPairFilter;

public interface ComparisonPairFilterListener{
    public void filterAdded(ComparisonPairFilter filter);

    public void filterRemoved(ComparisonPairFilter filter);

    public void filterUpdated(ComparisonPairFilter oldfilter, ComparisonPairFilter newfilter);
}
