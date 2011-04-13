package jp.sourceforge.stigmata.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TotalElementCountComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new TotalElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "totalelementcount";
    }

    @Override
    public String getDescription(){
        return "Filtering Element Count";
    }
}
