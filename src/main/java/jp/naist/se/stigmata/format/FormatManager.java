package jp.naist.se.stigmata.format;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.spi.ServiceRegistry;
import jp.naist.se.stigmata.format.csv.CsvResultFormatService;
import jp.naist.se.stigmata.spi.ResultFormatSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FormatManager{
    private static final FormatManager manager = new FormatManager();

    private Map<String, ResultFormatSpi> formats = new HashMap<String, ResultFormatSpi>();

    private FormatManager(){
        for(Iterator<ResultFormatSpi> i = ServiceRegistry.lookupProviders(ResultFormatSpi.class); i.hasNext(); ){
            ResultFormatSpi spi = i.next();
            formats.put(spi.getFormat(), spi);
        }
    }

    public static ResultFormatSpi getDefaultFormatService(){
        return new CsvResultFormatService();
    }

    public static FormatManager getInstance(){
        return manager;
    }

    public ResultFormatSpi getService(String format){
        return formats.get(format);
    }
}