package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.spi.AbstractServiceProvider;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;
import jp.naist.se.stigmata.utils.LocalizedDescriptionManager;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
abstract class AbstractComparisonPairFilterService extends AbstractServiceProvider implements ComparisonPairFilterSpi{
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
                locale, getFilterName(), LocalizedDescriptionManager.ServiceCategory.filter
            );
    }

    public String getDisplayFilterName(){
        return getDisplayFilterName(Locale.getDefault());
    }

    public String getDisplayFilterName(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDisplayType(
                locale, getFilterName(), LocalizedDescriptionManager.ServiceCategory.filter
            );
    }
}
