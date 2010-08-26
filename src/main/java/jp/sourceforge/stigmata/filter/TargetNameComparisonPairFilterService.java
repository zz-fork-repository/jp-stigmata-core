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

    @Override
    public ComparisonPairFilter getFilter(){
        return new TargetNameComparisonPairFilter(this);
    }

    @Override
    public String getFilterClassName(){
        return TargetNameComparisonPairFilter.class.getName();
    }

    @Override
    public String getFilterName(){
        return "name";
    }
}
