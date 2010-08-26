package jp.sourceforge.stigmata.printer.csv;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.util.Iterator;

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.ExtractionTarget;
import jp.sourceforge.stigmata.printer.AbstractExtractionResultSetPrinter;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class ExtractionResultSetCsvPrinter extends AbstractExtractionResultSetPrinter{
    @Override
    public void printResult(PrintWriter out, ExtractionResultSet ers){
        printHeader(out);
        for(Iterator<BirthmarkSet> i = ers.birthmarkSets(ExtractionTarget.TARGET_BOTH); i.hasNext(); ){
            printBirthmarkSet(out, i.next());
        }
        printFooter(out);
    }

    protected void printBirthmarkSet(PrintWriter out, BirthmarkSet holder){
        for(String type: holder.getBirthmarkTypes()){
            out.print(holder.getName());
            out.print(",");
            out.print(holder.getLocation());

            Birthmark birthmark = holder.getBirthmark(type);
            out.print(",");
            out.print(birthmark.getType());
            for(Iterator<BirthmarkElement> elements = birthmark.iterator(); elements.hasNext(); ){
                out.print(",");
                out.print(elements.next());
            }
            out.println();
        }
    }
}
