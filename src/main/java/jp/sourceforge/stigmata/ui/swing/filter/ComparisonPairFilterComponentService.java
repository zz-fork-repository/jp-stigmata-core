package jp.sourceforge.stigmata.ui.swing.filter;

import java.util.Locale;

import jp.sourceforge.stigmata.spi.ComparisonPairFilterSpi;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki TAMADA
 */
public interface ComparisonPairFilterComponentService{
    public String getDisplayFilterName(Locale locale);

    public String getDisplayFilterName();

    public String getFilterName();

    public ComparisonPairFilterPane createComponent(StigmataFrame frame, ComparisonPairFilterSpi service);

    public ComparisonPairFilterSpi getComparisonPairFilterService();
}
