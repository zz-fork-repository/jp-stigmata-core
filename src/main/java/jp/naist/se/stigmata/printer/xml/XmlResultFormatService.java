package jp.naist.se.stigmata.printer.xml;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.printer.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.printer.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.printer.BirthmarkServiceListFormat;
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
public class XmlResultFormatService extends AbstractServiceProvider implements ResultPrinterSpi{
    private BirthmarkExtractionListXmlFormat list = new BirthmarkExtractionListXmlFormat();
    private BirthmarkServiceListXmlFormat serviceList = new BirthmarkServiceListXmlFormat();
    private BirthmarkComparisonResultXmlFormat compare = new BirthmarkComparisonResultXmlFormat(list);

    /**
     * returns a localized description of the birthmark this service provides.
     */
    public String getDescription(Locale locale){
        return LocalizedDescriptionManager.getInstance().getDescription(
            locale, getFormat(), LocalizedDescriptionManager.ServiceCategory.formatter
        );
    }

    public String getFormat(){
        return "xml";
    }

    public BirthmarkComparisonResultFormat getComparisonResultFormat() {
        return compare;
    }

    public BirthmarkExtractionResultFormat getExtractionResultFormat() {
        return list;
    }

    public BirthmarkServiceListFormat getBirthmarkServiceListFormat() {
        return serviceList;
    }
}
