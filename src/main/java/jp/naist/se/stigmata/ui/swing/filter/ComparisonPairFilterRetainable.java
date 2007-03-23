package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.ComparisonPairFilterSet;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ComparisonPairFilterRetainable{
    public void filterSelected(ComparisonPairFilter filter);

    public void addFilterSet(ComparisonPairFilterSet filter);

    public void removeFilterSet(String name);

    public void updateFilterSet(String name, ComparisonPairFilterSet filter);

    public ComparisonPairFilterSet getFilterSet(String name);
}
