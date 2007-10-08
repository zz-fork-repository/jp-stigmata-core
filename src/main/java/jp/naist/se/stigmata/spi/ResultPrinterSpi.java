package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import jp.naist.se.stigmata.printer.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.printer.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.printer.BirthmarkServiceListFormat;

/**
 * Service provider interface for printing comparison/extracting
 * result to certain output stream.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public interface ResultPrinterSpi extends ServiceProvider{
    /**
     * return a format.
     */
    public String getFormat();

    public BirthmarkServiceListFormat getBirthmarkServiceListFormat();

    public BirthmarkComparisonResultFormat getComparisonResultFormat();

    public BirthmarkExtractionResultFormat getExtractionResultFormat();
}
