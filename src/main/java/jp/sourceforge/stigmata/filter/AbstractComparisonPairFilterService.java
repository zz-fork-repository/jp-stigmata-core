package jp.sourceforge.stigmata.filter;

/*
 * $Id$
 */

import java.util.Locale;

import jp.sourceforge.stigmata.spi.AbstractServiceProvider;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
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
