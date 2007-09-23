package jp.naist.se.stigmata.format.csv;

/*
 * $Id$
 */

import java.io.PrintWriter;
import java.util.Iterator;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.format.AbstractBirthmarkExtractionResultFormat;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkExtractionResultCsvFormat extends AbstractBirthmarkExtractionResultFormat{
    public void printResult(PrintWriter out, ExtractionResultSet ers){
        for(Iterator<BirthmarkSet> i = ers.birthmarkSets(ExtractionTarget.TARGET_BOTH); i.hasNext(); ){
            printBirthmarkSet(out, i.next());
        }
        out.flush();
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
