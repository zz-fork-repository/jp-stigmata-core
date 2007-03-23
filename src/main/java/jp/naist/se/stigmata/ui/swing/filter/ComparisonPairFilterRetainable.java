package jp.naist.se.stigmata.ui.swing.filter;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;

public interface ComparisonPairFilterRetainable{
    public void filterSelected(ComparisonPairFilter filter);

    public void addFilterSet(ComparisonPairFilterSet filter);
    public void removeFilterSet(String name);

    public void updateFilterSet(String name, ComparisonPairFilterSet filter);

    public ComparisonPairFilterSet getFilterSet(String name);
}
