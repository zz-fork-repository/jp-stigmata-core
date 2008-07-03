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
public class BirthmarkElementCountComparisonPairFilterService extends AbstractComparisonPairFilterService{

    public ComparisonPairFilter getFilter(){
        return new BirthmarkElementCountComparisonPairFilter(this);
    }

    public String getFilterClassName(){
        return BirthmarkElementCountComparisonPairFilter.class.getName();
    }

    public String getFilterName(){
        return "elementcount";
    }
}
