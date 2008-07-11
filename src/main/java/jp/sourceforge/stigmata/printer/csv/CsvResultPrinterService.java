package jp.sourceforge.stigmata.printer.csv;

/*
 * $Id$
 */

import java.util.Locale;

import jp.sourceforge.stigmata.printer.BirthmarkServicePrinter;
import jp.sourceforge.stigmata.printer.ComparisonPairPrinter;
import jp.sourceforge.stigmata.printer.ComparisonResultSetPrinter;
import jp.sourceforge.stigmata.printer.ExtractionResultSetPrinter;
import jp.sourceforge.stigmata.spi.AbstractServiceProvider;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;
import jp.sourceforge.stigmata.utils.LocalizedDescriptionManager;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
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
