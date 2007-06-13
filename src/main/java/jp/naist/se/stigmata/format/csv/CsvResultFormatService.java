package jp.naist.se.stigmata.format.csv;

/*
 * $Id$
 */

import java.util.Locale;

import jp.naist.se.stigmata.format.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.format.BirthmarkServiceListFormat;
import jp.naist.se.stigmata.spi.AbstractServiceProvider;
import jp.naist.se.stigmata.spi.ResultFormatSpi;
import jp.naist.se.stigmata.utils.LocalizedDescriptionManager;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CsvResultFormatService extends AbstractServiceProvider implements ResultFormatSpi{
    private BirthmarkServiceListCsvFormat serviceList = new BirthmarkServiceListCsvFormat();
    private BirthmarkExtractionResultCsvFormat list = new BirthmarkExtractionResultCsvFormat();
    private BirthmarkComparisonResultCsvFormat compare = new BirthmarkComparisonResultCsvFormat(list);

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
