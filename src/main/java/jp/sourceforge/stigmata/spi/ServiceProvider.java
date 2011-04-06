package jp.sourceforge.stigmata.spi;

import java.util.Locale;

/**
 * Base interface for birthmark SPI.
 *
 * @author Haruaki TAMADA
 */
public interface ServiceProvider{
    public String getDescription(Locale locale);

    public String getDescription();

    public String getVendorName();

    public String getVersion();
}
