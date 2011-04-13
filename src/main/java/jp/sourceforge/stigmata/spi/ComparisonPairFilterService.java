package jp.sourceforge.stigmata.spi;

import jp.sourceforge.stigmata.ComparisonPairFilter;

/**
 * Service provider interface for filtering comparison pair.
 * 
 * @author Haruaki TAMADA
 */
public interface ComparisonPairFilterService{
    public String getFilterName();

    public String getDescription();

    public ComparisonPairFilter getFilter();
}
