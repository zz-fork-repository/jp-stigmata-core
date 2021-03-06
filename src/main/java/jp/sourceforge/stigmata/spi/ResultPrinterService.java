package jp.sourceforge.stigmata.spi;

import jp.sourceforge.stigmata.printer.BirthmarkServicePrinter;
import jp.sourceforge.stigmata.printer.ComparisonPairPrinter;
import jp.sourceforge.stigmata.printer.ComparisonResultSetPrinter;
import jp.sourceforge.stigmata.printer.ExtractionResultSetPrinter;

/**
 * Service provider interface for printing comparison/extracting
 * result to certain output stream.
 *
 * @author Haruaki TAMADA
 */
public interface ResultPrinterService{
    /**
     * return a format.
     */
    public String getFormat();

    public String getDescription();

    public BirthmarkServicePrinter getBirthmarkServicePrinter();

    public ComparisonResultSetPrinter getComparisonResultSetPrinter();

    public ComparisonPairPrinter getComparisonPairPrinter();

    public ExtractionResultSetPrinter getExtractionResultSetPrinter();
}
