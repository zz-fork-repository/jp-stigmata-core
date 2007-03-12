package jp.naist.se.stigmata.spi;

/*
 * $Id: BirthmarkSpi.java 20 2007-01-17 02:06:01Z tama3 $
 */

import java.util.Locale;

import jp.naist.se.stigmata.BirthmarkComparator;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision: 20 $ $Date: 2007-01-17 11:06:01 +0900 (Wed, 17 Jan 2007) $
 */
public interface BirthmarkComparatorSpi extends ServiceProvider{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    public String getDescription(Locale locale);

    /**
     * returns a localized description of the birthmark in default locale.
     */
    public String getDescription();

    public String getComparatorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkSpi service);
}

