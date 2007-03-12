package jp.naist.se.stigmata.ui.swing.filter;

import java.util.Locale;

import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;

public interface ComparisonPairFilterComponentService{
    public String getDisplayFilterName(Locale locale);

    public String getDisplayFilterName();

    public String getFilterName();

    public ComparisonPairFilterPane createComponent(ComparisonPairFilterSpi service);

    public ComparisonPairFilterSpi getComparisonPairFilterService();
}
