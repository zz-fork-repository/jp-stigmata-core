package jp.naist.se.stigmata.ui.swing.filter;

import java.util.Locale;

import jp.naist.se.stigmata.utils.LocalizedDescriptionManager;

public abstract class AbstractComparisonPairFilterComponentService implements ComparisonPairFilterComponentService{
    public String getDisplayFilterName(){
        return getDisplayFilterName(Locale.getDefault());
    }
    
    public String getDisplayFilterName(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDisplayType(
            locale, getFilterName(), LocalizedDescriptionManager.ServiceCategory.filter
        );
    }
}
