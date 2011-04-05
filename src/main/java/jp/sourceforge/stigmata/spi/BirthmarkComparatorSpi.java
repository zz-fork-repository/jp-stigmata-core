package jp.sourceforge.stigmata.spi;

/*
 * $Id$
 */

import java.util.Locale;

import jp.sourceforge.stigmata.BirthmarkComparator;

/**
 * Service provider interface for comparing birthmarks.
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkComparatorSpi extends ServiceProvider{
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

    public String getComparatorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service);
}

