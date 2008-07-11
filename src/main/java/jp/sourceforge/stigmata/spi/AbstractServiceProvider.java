package jp.sourceforge.stigmata.spi;

/*
 * $Id$
 */

import java.util.Locale;

/**
 * Base abstract class for birthmark SPI.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public abstract class AbstractServiceProvider implements ServiceProvider{

    /**
     * returning implementation vendor name of this SPI.
     */
    public String getDescription(){
        return getDescription(Locale.getDefault());
    }

    /**
     * returning implementation vendor name of this SPI.
     */
    public String getVendorName(){
        return getClass().getPackage().getImplementationVendor();
    }

    /**
     * returning version of this SPI.
     */
    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

}
