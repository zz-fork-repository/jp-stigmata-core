package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$
 */
public interface ComparisonPairFilterComponentService{
    public String getDisplayFilterName(Locale locale);

    public String getDisplayFilterName();

    public String getFilterName();

    public ComparisonPairFilterPane createComponent(StigmataFrame frame, ComparisonPairFilterSpi service);

    public ComparisonPairFilterSpi getComparisonPairFilterService();
}
