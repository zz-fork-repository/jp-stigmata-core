package jp.naist.se.stigmata.spi;

/*
 * $Id$
 */

import jp.naist.se.stigmata.printer.BirthmarkServicePrinter;
import jp.naist.se.stigmata.printer.ComparisonPairPrinter;
import jp.naist.se.stigmata.printer.ComparisonResultSetPrinter;
import jp.naist.se.stigmata.printer.ExtractionResultSetPrinter;

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

    public BirthmarkServicePrinter getBirthmarkServicePrinter();

    public ComparisonResultSetPrinter getComparisonResultSetPrinter();

    public ComparisonPairPrinter getComparisonPairPrinter();

    public ExtractionResultSetPrinter getExtractionResultSetPrinter();
}
