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
public class TargetNameComparisonPairFilterService extends AbstractComparisonPairFilterService{

    public ComparisonPairFilter getFilter(){
        return new TargetNameComparisonPairFilter(this);
    }

    public String getFilterClassName(){
        return TargetNameComparisonPairFilter.class.getName();
    }

    public String getFilterName(){
        return "name";
    }
}
