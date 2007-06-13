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
