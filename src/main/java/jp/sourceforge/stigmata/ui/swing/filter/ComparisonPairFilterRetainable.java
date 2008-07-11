package jp.sourceforge.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.ComparisonPairFilterSet;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public interface ComparisonPairFilterRetainable{
    public void filterSelected(ComparisonPairFilter filter);

    public void addFilterSet(ComparisonPairFilterSet filter);

    public void removeFilterSet(String name);

    public void updateFilterSet(String name, ComparisonPairFilterSet filter);

    public ComparisonPairFilterSet getFilterSet(String name);
}
