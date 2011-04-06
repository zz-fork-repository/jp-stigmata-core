package jp.sourceforge.stigmata.printer.xml;

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
 */
public class XmlResultPrinterService extends AbstractServiceProvider implements ResultPrinterSpi{
    private BirthmarkServiceXmlPrinter serviceList = new BirthmarkServiceXmlPrinter();
    private ExtractionResultSetXmlPrinter list = new ExtractionResultSetXmlPrinter();
    private ComparisonPairXmlPrinter pairPrinter = new ComparisonPairXmlPrinter(list);
    private ComparisonResultSetXmlPrinter compare = new ComparisonResultSetXmlPrinter(pairPrinter);

    /**
     * returns a localized description of the birthmark this service provides.
     */
    @Override
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
            locale, getFormat(), LocalizedDescriptionManager.ServiceCategory.formatter
        );
    }

    @Override
    public String getFormat(){
        return "xml";
    }

    @Override
    public ComparisonResultSetPrinter getComparisonResultSetPrinter() {
        return compare;
    }

    @Override
    public ExtractionResultSetPrinter getExtractionResultSetPrinter() {
        return list;
    }

    @Override
    public BirthmarkServicePrinter getBirthmarkServicePrinter() {
        return serviceList;
    }

    @Override
    public ComparisonPairPrinter getComparisonPairPrinter(){
        return pairPrinter;
    }
}
