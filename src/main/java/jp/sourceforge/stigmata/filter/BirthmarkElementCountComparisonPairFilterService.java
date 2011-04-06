package jp.sourceforge.stigmata.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkElementCountComparisonPairFilterService extends AbstractComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new BirthmarkElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterClassName(){
        return BirthmarkElementCountComparisonPairFilter.class.getName();
    }

    @Override
    public String getFilterName(){
        return "elementcount";
    }
}
