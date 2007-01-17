package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import jp.naist.se.stigmata.format.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.format.BirthmarkServiceListFormat;

/**
 * comparison/extracting result output service.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ResultFormatSpi{
    /**
     * return a format.
     */
    public String getFormat();

    public BirthmarkServiceListFormat getBirthmarkServiceListFormat();

    public BirthmarkComparisonResultFormat getComparisonResultFormat();

    public BirthmarkExtractionResultFormat getExtractionResultFormat();
}
