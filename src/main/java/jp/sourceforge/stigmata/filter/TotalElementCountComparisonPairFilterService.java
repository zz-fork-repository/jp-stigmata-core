package jp.sourceforge.stigmata.filter;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class TotalElementCountComparisonPairFilterService extends AbstractComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new TotalElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterClassName(){
        return TotalElementCountComparisonPairFilter.class.getName();
    }

    @Override
    public String getFilterName(){
        return "totalelementcount";
    }
}
