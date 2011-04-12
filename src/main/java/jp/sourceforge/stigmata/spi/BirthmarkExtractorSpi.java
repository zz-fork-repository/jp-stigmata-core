package jp.sourceforge.stigmata.spi;

import java.util.Locale;

import jp.sourceforge.stigmata.BirthmarkExtractor;

/**
 * Service provider interface for extracting birhtmark from given class files.
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkExtractorSpi extends ServiceProvider{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    @Override
    public String getDescription(Locale locale);

    /**
     * returns a localized description of the birthmark in default locale.
     */
    @Override
    public String getDescription();

    public String getExtractorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor(BirthmarkService service);
}

