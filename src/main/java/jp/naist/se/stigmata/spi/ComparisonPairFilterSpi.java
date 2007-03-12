package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.ComparisonPairFilter;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ComparisonPairFilterSpi extends ServiceProvider{
    public String getDisplayFilterName(Locale locale);

    public String getDisplayFilterName();

    public String getFilterName();

    public String getFilterClassName();

    public ComparisonPairFilter getFilter();
}
