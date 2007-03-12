package jp.naist.se.stigmata.spi;

import java.util.Locale;


public abstract class AbstractServiceProvider implements ServiceProvider{

    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    public String getVendorName(){
        return getClass().getPackage().getImplementationVendor();
    }

    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

}
