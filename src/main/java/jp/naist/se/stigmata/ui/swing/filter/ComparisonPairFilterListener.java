package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ComparisonPairFilterListener{
    public void filterAdded(ComparisonPairFilter filter);

    public void filterRemoved(ComparisonPairFilter filter);

    public void filterUpdated(ComparisonPairFilter oldfilter, ComparisonPairFilter newfilter);
}
