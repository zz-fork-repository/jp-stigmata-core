package jp.naist.se.stigmata.spi;

import java.util.Locale;

public interface ServiceProvider{
    public String getDescription(Locale locale);

    public String getDescription();

    public String getVendorName();

    public String getVersion();
}
