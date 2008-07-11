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
