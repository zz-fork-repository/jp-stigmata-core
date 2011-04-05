package jp.sourceforge.stigmata.spi;

import java.util.Locale;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.BirthmarkPreprocessor;

/**
 * Birthmark service provider interface.
 *
 * @author Haruaki TAMADA
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
    @Override
    public String getDescription(Locale locale);

    /**
     * returns a localized description of the birthmark in default locale.
     */
    @Override
    public String getDescription();

    public String getExtractorClassName();

    public Birthmark buildBirthmark();

    public BirthmarkElement buildBirthmarkElement(String elementValue);

    public BirthmarkPreprocessor getPreprocessor();

    public String getPreprocessorClassName();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkExtractor getExtractor();

    public String getComparatorClassName();

    /**
     * returns a comparator for the birthmark of this service.
     */
    public BirthmarkComparator getComparator();

    public boolean isExperimental();

    public boolean isUserDefined();

}

