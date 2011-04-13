package jp.sourceforge.stigmata.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkElementCountComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new BirthmarkElementCountComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "elementcount";
    }

    @Override
    public String getDescription(){
        return "Element count Filter";
    }
}
