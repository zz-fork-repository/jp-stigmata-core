package jp.sourceforge.stigmata.filter;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TargetNameComparisonPairFilterService implements ComparisonPairFilterService{

    @Override
    public ComparisonPairFilter getFilter(){
        return new TargetNameComparisonPairFilter(this);
    }

    @Override
    public String getFilterName(){
        return "name";
    }

    @Override
    public String getDescription(){
        return "Filtering by Target Name";
    }
}
