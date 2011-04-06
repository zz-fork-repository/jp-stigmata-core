package jp.sourceforge.stigmata.printer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.printer.csv.CsvResultPrinterService;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;

/**
 *
 * @author Haruaki TAMADA
 */
public class PrinterManager{
    private static final PrinterManager manager = new PrinterManager();
    private ServiceLoader<ResultPrinterSpi> serviceLoader;

    private Map<String, ResultPrinterSpi> formats = new HashMap<String, ResultPrinterSpi>();

    private PrinterManager(){
        serviceLoader = ServiceLoader.load(ResultPrinterSpi.class);
        load();
    }

    public void refresh(){
        serviceLoader.reload();
        load();
    }

    public static void refresh(BirthmarkEnvironment env){
        PrinterManager instance = getInstance();
        instance.formats.clear();
        for(Iterator<ResultPrinterSpi> i = env.lookupProviders(ResultPrinterSpi.class); i.hasNext(); ){
            instance.addService(i.next());
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

    private void load(){
        formats.clear();
        for(Iterator<ResultPrinterSpi> i = serviceLoader.iterator(); i.hasNext(); ){
            ResultPrinterSpi spi = i.next();
            addService(spi);
        }
    }

    private void addService(ResultPrinterSpi service){
        formats.put(service.getFormat(), service);
    }
}
