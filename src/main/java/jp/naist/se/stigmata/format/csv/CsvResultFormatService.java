package jp.naist.se.stigmata.format.csv;

/*
 * $Id$
 */

import jp.naist.se.stigmata.format.BirthmarkComparisonResultFormat;
import jp.naist.se.stigmata.format.BirthmarkExtractionResultFormat;
import jp.naist.se.stigmata.format.BirthmarkServiceListFormat;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class CsvResultFormatService implements ResultFormatSpi{
    private BirthmarkServiceListCsvFormat serviceList = new BirthmarkServiceListCsvFormat();
    private BirthmarkExtractionResultCsvFormat list = new BirthmarkExtractionResultCsvFormat();
    private BirthmarkComparisonResultCsvFormat compare = new BirthmarkComparisonResultCsvFormat(list);

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
