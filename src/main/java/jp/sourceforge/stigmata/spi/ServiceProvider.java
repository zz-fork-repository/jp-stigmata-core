package jp.sourceforge.stigmata.spi;

/*
 * $Id$
 */

import java.util.Locale;

/**
 * Base interface for birthmark SPI.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ServiceProvider{
    public String getDescription(Locale locale);

    public String getDescription();

    public String getVendorName();

    public String getVersion();
}
