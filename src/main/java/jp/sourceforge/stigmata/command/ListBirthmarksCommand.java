package jp.sourceforge.stigmata.command;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.Stigmata;
import jp.sourceforge.stigmata.printer.BirthmarkServicePrinter;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;
import jp.sourceforge.stigmata.spi.ResultPrinterSpi;

/**
 * 
 * @author Haruaki Tamada
 */
public class ListBirthmarksCommand extends AbstractStigmataCommand{
    @Override
    public String getCommandString(){
        return "list-birthmarks";
    }

    @Override
    public void perform(Stigmata stigmata, BirthmarkContext context, String[] args){
        BirthmarkSpi[] spis = context.getEnvironment().findServices();
        ResultPrinterSpi spi = stigmata.getPrinterManager().getService(context.getFormat());
        BirthmarkServicePrinter formatter = spi.getBirthmarkServicePrinter();

        try{
            PrintWriter out;
            if(args.length == 0){
                out = new PrintWriter(System.out);
            }
            else{
                String target = validateTarget(args[0], context.getFormat());
                out = new PrintWriter(new FileWriter(target));
            }
            formatter.printResult(out, spis);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String validateTarget(String fileName, String format){
        if(!fileName.endsWith("." + format)){
            fileName = fileName + "." + format;
        }
        return fileName;
    }
}
