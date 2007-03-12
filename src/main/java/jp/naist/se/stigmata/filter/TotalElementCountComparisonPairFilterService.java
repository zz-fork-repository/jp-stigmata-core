package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ComparisonPairFilter;

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
