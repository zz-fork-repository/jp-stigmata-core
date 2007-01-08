package jp.naist.se.stigmata.format.xml;

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
public class XmlResultFormatService implements ResultFormatSpi{
    private BirthmarkExtractionListXmlFormat list = new BirthmarkExtractionListXmlFormat();
    private BirthmarkServiceListXmlFormat serviceList = new BirthmarkServiceListXmlFormat();
    private BirthmarkComparisonResultXmlFormat compare = new BirthmarkComparisonResultXmlFormat(list);

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
