package jp.naist.se.stigmata.filter;

import jp.naist.se.stigmata.ComparisonPairFilter;

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
