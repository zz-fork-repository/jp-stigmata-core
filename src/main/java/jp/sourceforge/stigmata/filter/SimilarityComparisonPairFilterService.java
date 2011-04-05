package jp.sourceforge.stigmata.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 */
public class SimilarityComparisonPairFilterService extends AbstractComparisonPairFilterService{
    @Override
    public ComparisonPairFilter getFilter(){
        return new SimilarityComparisonPairFilter(this);
    }

    @Override
    public String getFilterClassName(){
        return SimilarityComparisonPairFilter.class.getName();
    }

    @Override
    public String getFilterName(){
        return "similarity";
    }
}
