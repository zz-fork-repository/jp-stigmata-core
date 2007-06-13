package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.BirthmarkComparator;
import jp.naist.se.stigmata.BirthmarkExtractor;

/**
 * Birthmark service provider interface.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface BirthmarkSpi extends ServiceProvider{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType();

    /**
     * returns a type of the birthmark for display.
     */
    public String getDisplayType(Locale locale);

    /**
     * returns a type of the birthmark for display in default locale.
     */
    public String getDisplayType();

    /**
     * returns a description of the birthmark this service provides.
     */
    public String getDefaultDescription();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    public String getDescription(Locale locale);

    /**
     * returns a localized description of the birthmark in default locale.
     */
    public String getDescription();

    public String getExtractorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor();

    public String getComparatorClassName();

    /**
     * returns a comparator for the birthmark of this service.
     */
    public BirthmarkComparator getComparator();

    public boolean isExpert();

    public boolean isUserDefined();

}

