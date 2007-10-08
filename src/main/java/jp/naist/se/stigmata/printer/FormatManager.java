package jp.naist.se.stigmata.printer;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.printer.csv.CsvResultFormatService;
import jp.naist.se.stigmata.spi.ResultPrinterSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FormatManager{
    private static final FormatManager manager = new FormatManager();

    private Map<String, ResultPrinterSpi> formats = new HashMap<String, ResultPrinterSpi>();

    private FormatManager(){
        for(Iterator<ResultPrinterSpi> i = ServiceRegistry.lookupProviders(ResultPrinterSpi.class); i.hasNext(); ){
            ResultPrinterSpi spi = i.next();
            addService(spi);
        }
    }

    public static void updateServices(BirthmarkEnvironment environment){
        FormatManager instance = getInstance();
        for(Iterator<ResultPrinterSpi> i = environment.lookupProviders(ResultPrinterSpi.class); i.hasNext(); ){
            ResultPrinterSpi spi = i.next();
            instance.addService(spi);
        }
    }

    public static ResultPrinterSpi getDefaultFormatService(){
        return new CsvResultFormatService();
    }

    public static FormatManager getInstance(){
        return manager;
    }

    public ResultPrinterSpi getService(String format){
        return formats.get(format);
    }

    private void addService(ResultPrinterSpi service){
        formats.put(service.getFormat(), service);
    }
}
