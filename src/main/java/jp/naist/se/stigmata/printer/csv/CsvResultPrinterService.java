package jp.naist.se.stigmata.printer.csv;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.printer.BirthmarkServicePrinter;
import jp.naist.se.stigmata.printer.ComparisonPairPrinter;
import jp.naist.se.stigmata.printer.ComparisonResultSetPrinter;
import jp.naist.se.stigmata.printer.ExtractionResultSetPrinter;
import jp.naist.se.stigmata.spi.AbstractServiceProvider;
import jp.naist.se.stigmata.spi.ResultPrinterSpi;
import jp.naist.se.stigmata.utils.LocalizedDescriptionManager;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CsvResultPrinterService extends AbstractServiceProvider implements ResultPrinterSpi{
    private BirthmarkServiceCsvPrinter serviceList = new BirthmarkServiceCsvPrinter();
    private ExtractionResultSetCsvPrinter list = new ExtractionResultSetCsvPrinter();
    private ComparisonPairCsvPrinter pairPrinter = new ComparisonPairCsvPrinter(list);
    private ComparisonResultSetCsvPrinter compare = new ComparisonResultSetCsvPrinter();

    /**
     * returns a localized description of the birthmark this service provides.
     */
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
            locale, getFormat(), LocalizedDescriptionManager.ServiceCategory.formatter
        );
    }

    public String getFormat(){
        return "csv";
    }
    
    public ComparisonResultSetPrinter getComparisonResultSetPrinter() {
        return compare;
    }

    public ExtractionResultSetPrinter getExtractionResultSetPrinter() {
        return list;
    }

    public BirthmarkServicePrinter getBirthmarkServicePrinter() {
        return serviceList;
    }

    public ComparisonPairPrinter getComparisonPairPrinter(){
        return pairPrinter;
    }
}
