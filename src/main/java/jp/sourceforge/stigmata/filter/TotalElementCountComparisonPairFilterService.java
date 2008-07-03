package jp.sourceforge.stigmata.filter;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class TotalElementCountComparisonPairFilterService extends AbstractComparisonPairFilterService{

    public ComparisonPairFilter getFilter(){
        return new TotalElementCountComparisonPairFilter(this);
    }

    public String getFilterClassName(){
        return TotalElementCountComparisonPairFilter.class.getName();
    }

    public String getFilterName(){
        return "totalelementcount";
    }
}
