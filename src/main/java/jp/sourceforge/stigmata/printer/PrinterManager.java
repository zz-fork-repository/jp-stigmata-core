package jp.sourceforge.stigmata.printer;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.printer.csv.CsvResultPrinterService;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class PrinterManager{
    private static final PrinterManager manager = new PrinterManager();

    private Map<String, ResultPrinterSpi> formats = new HashMap<String, ResultPrinterSpi>();

    private PrinterManager(){
        for(Iterator<ResultPrinterSpi> i = ServiceRegistry.lookupProviders(ResultPrinterSpi.class); i.hasNext(); ){
            ResultPrinterSpi spi = i.next();
            addService(spi);
        }
    }

    public static void updateServices(BirthmarkEnvironment environment){
        PrinterManager instance = getInstance();
        for(Iterator<ResultPrinterSpi> i = environment.lookupProviders(ResultPrinterSpi.class); i.hasNext(); ){
            ResultPrinterSpi spi = i.next();
            instance.addService(spi);
        }
    }

    public static ResultPrinterSpi getDefaultFormatService(){
        return new CsvResultPrinterService();
    }

    public static PrinterManager getInstance(){
        return manager;
    }

    public ResultPrinterSpi getService(String format){
        return formats.get(format);
    }

    private void addService(ResultPrinterSpi service){
        formats.put(service.getFormat(), service);
    }
}
