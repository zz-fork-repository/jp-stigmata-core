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
public class SimilarityComparisonPairFilterService extends AbstractComparisonPairFilterService{
    public ComparisonPairFilter getFilter(){
        return new SimilarityComparisonPairFilter(this);
    }

    public String getFilterClassName(){
        return SimilarityComparisonPairFilter.class.getName();
    }

    public String getFilterName(){
        return "similarity";
    }
}
