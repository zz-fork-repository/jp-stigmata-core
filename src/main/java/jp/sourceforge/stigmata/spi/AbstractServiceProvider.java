package jp.sourceforge.stigmata.spi;

import java.util.Locale;

/**
 * Base abstract class for birthmark SPI.
 *
 * @author Haruaki TAMADA
 */
public abstract class AbstractServiceProvider implements ServiceProvider{

    /**
     * returning implementation vendor name of this SPI.
     */
    @Override
    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    /**
     * returning implementation vendor name of this SPI.
     */
    @Override
    public String getVendorName(){
        return getClass().getPackage().getImplementationVendor();
    }

    /**
     * returning version of this SPI.
     */
    @Override
    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

}
